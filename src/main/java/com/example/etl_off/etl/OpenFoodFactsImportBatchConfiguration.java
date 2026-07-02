package com.example.etl_off.etl;

import com.example.etl_off.entity.Additif;
import com.example.etl_off.entity.Allergene;
import com.example.etl_off.entity.Categorie;
import com.example.etl_off.entity.Ingredient;
import com.example.etl_off.entity.Marque;
import com.example.etl_off.entity.Produit;
import com.example.etl_off.service.AdditifService;
import com.example.etl_off.service.AllergeneService;
import com.example.etl_off.service.CategorieService;
import com.example.etl_off.service.IngredientService;
import com.example.etl_off.service.MarqueService;
import com.example.etl_off.service.ProduitService;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch configuration for importing Open Food Facts CSV files.
 */
@Configuration
public class OpenFoodFactsImportBatchConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenFoodFactsImportBatchConfiguration.class);
    private static final int BATCH_SIZE = 500;

    @Bean
    public Job openFoodFactsImportJob(JobRepository jobRepository, Step openFoodFactsImportStep) {
        return new JobBuilder("openFoodFactsImportJob", jobRepository)
                .start(openFoodFactsImportStep)
                .build();
    }

    @Bean
    public Step openFoodFactsImportStep(JobRepository jobRepository,
                                        PlatformTransactionManager transactionManager,
                                        @Value("${etl.import.file:open-food-facts.csv}") String importFile,
                                        OpenFoodFactsCsvParser parser,
                                        CategorieService categorieService,
                                        MarqueService marqueService,
                                        IngredientService ingredientService,
                                        AllergeneService allergeneService,
                                        AdditifService additifService,
                                        ProduitService produitService,
                                        ImportLookupCache importLookupCache) {
        Tasklet tasklet = (contribution, chunkContext) -> {
            Instant start = Instant.now();
            Runtime runtime = Runtime.getRuntime();
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            long usedMemoryBefore = usedMemoryMb(runtime);
            long cpuBefore = currentProcessCpuTime();

            Path csvPath = Path.of(importFile);
            List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
            List<String> dataLines = lines.size() <= 1 ? List.of() : lines.subList(1, lines.size());

            List<Produit> productsBuffer = new ArrayList<>(BATCH_SIZE);
            Map<String, Categorie> categories = indexEntities(categorieService.findAll(), Categorie::getNomCategorie);
            Map<String, Marque> marques = indexEntities(marqueService.findAll(), Marque::getNomMarque);
            Map<String, Ingredient> ingredients = indexEntities(ingredientService.findAll(), Ingredient::getNomIngredient);
            Map<String, Allergene> allergenes = indexEntities(allergeneService.findAll(), Allergene::getNomAllergene);
            Map<String, Additif> additifs = indexEntities(additifService.findAll(), Additif::getNomAdditif);

            ThreadFactory threadFactory = Thread.ofVirtual().name("off-row-", 0).factory();
            try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(threadFactory)) {
                List<Future<Optional<Produit>>> futures = new ArrayList<>(dataLines.size());
                for (String line : dataLines) {
                    futures.add(executorService.submit(() -> parseLineToProduit(
                            line,
                            parser,
                            categorieService,
                            marqueService,
                            ingredientService,
                            allergeneService,
                            additifService,
                            importLookupCache,
                            categories,
                            marques,
                            ingredients,
                            allergenes,
                            additifs)));
                }

                for (Future<Optional<Produit>> future : futures) {
                    future.get().ifPresent(produit -> {
                        productsBuffer.add(produit);
                        if (productsBuffer.size() >= BATCH_SIZE) {
                            produitService.saveAll(productsBuffer);
                            productsBuffer.clear();
                        }
                    });
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Import interrompu pendant le traitement parallele.", exception);
            } catch (ExecutionException exception) {
                throw new IllegalStateException("Erreur pendant le traitement parallele du CSV.", exception);
            }

            if (!productsBuffer.isEmpty()) {
                produitService.saveAll(productsBuffer);
            }

            long cpuAfter = currentProcessCpuTime();
            LOGGER.info("Batch import completed in {} with {} products. Memory delta={}MB, CPU delta={}ms, peak threads={}",
                    Duration.between(start, Instant.now()),
                    dataLines.size(),
                    usedMemoryMb(runtime) - usedMemoryBefore,
                    Duration.ofNanos(Math.max(0, cpuAfter - cpuBefore)).toMillis(),
                    threadMXBean.getPeakThreadCount());
            return RepeatStatus.FINISHED;
        };

        return new StepBuilder("openFoodFactsImportStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    private Optional<Produit> parseLineToProduit(String line,
                                                 OpenFoodFactsCsvParser parser,
                                                 CategorieService categorieService,
                                                 MarqueService marqueService,
                                                 IngredientService ingredientService,
                                                 AllergeneService allergeneService,
                                                 AdditifService additifService,
                                                 ImportLookupCache importLookupCache,
                                                 Map<String, Categorie> categories,
                                                 Map<String, Marque> marques,
                                                 Map<String, Ingredient> ingredients,
                                                 Map<String, Allergene> allergenes,
                                                 Map<String, Additif> additifs) {
        return parser.parseLine(line).map(row -> {
            Categorie categorie = categories.computeIfAbsent(
                    cacheKey(row.getCategorie()),
                    ignored -> importLookupCache.getOrCreate("categories", row.getCategorie(), () -> categorieService.findOrCreateByNom(row.getCategorie())));
            Marque marque = marques.computeIfAbsent(
                    cacheKey(row.getMarque()),
                    ignored -> importLookupCache.getOrCreate("marques", row.getMarque(), () -> marqueService.findOrCreateByNom(row.getMarque())));

            Produit produit = new Produit(
                    row.getNomProduit(),
                    row.getScoreNutritionnel(),
                    row.getEnergie(),
                    row.getMatiereGrasse(),
                    categorie,
                    marque);

            row.getIngredients().forEach(ingredientName -> produit.getIngredients().add(
                    ingredients.computeIfAbsent(
                            cacheKey(ingredientName),
                            ignored -> importLookupCache.getOrCreate("ingredients", ingredientName, () -> ingredientService.findOrCreateByNom(ingredientName)))));
            row.getAllergenes().forEach(allergeneName -> produit.getAllergenes().add(
                    allergenes.computeIfAbsent(
                            cacheKey(allergeneName),
                            ignored -> importLookupCache.getOrCreate("allergenes", allergeneName, () -> allergeneService.findOrCreateByNom(allergeneName)))));
            row.getAdditifs().forEach(additifName -> produit.getAdditifs().add(
                    additifs.computeIfAbsent(
                            cacheKey(additifName),
                            ignored -> importLookupCache.getOrCreate("additifs", additifName, () -> additifService.findOrCreateByNom(additifName)))));
            return produit;
        });
    }

    private <T> Map<String, T> indexEntities(List<T> entities, Function<T, String> nameExtractor) {
        Map<String, T> indexedEntities = new ConcurrentHashMap<>();
        entities.forEach(entity -> {
            String name = nameExtractor.apply(entity);
            if (name != null) {
                indexedEntities.put(cacheKey(name), entity);
            }
        });
        return indexedEntities;
    }

    private long usedMemoryMb(Runtime runtime) {
        return (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    }

    private long currentProcessCpuTime() {
        java.lang.management.OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
        if (bean instanceof com.sun.management.OperatingSystemMXBean operatingSystemMXBean) {
            return operatingSystemMXBean.getProcessCpuTime();
        }
        return 0;
    }

    private String cacheKey(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }
}
