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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import org.springframework.stereotype.Service;

/**
 * Imports Open Food Facts data with cached lookups, virtual-thread parsing and batch persistence.
 */
@Service
public class OptimizedOpenFoodFactsImportService {

    private final OpenFoodFactsCsvParser csvParser;
    private final CategorieService categorieService;
    private final MarqueService marqueService;
    private final IngredientService ingredientService;
    private final AllergeneService allergeneService;
    private final AdditifService additifService;
    private final ProduitService produitService;

    public OptimizedOpenFoodFactsImportService(OpenFoodFactsCsvParser csvParser,
                                               CategorieService categorieService,
                                               MarqueService marqueService,
                                               IngredientService ingredientService,
                                               AllergeneService allergeneService,
                                               AdditifService additifService,
                                               ProduitService produitService) {
        this.csvParser = csvParser;
        this.categorieService = categorieService;
        this.marqueService = marqueService;
        this.ingredientService = ingredientService;
        this.allergeneService = allergeneService;
        this.additifService = additifService;
        this.produitService = produitService;
    }

    /**
     * Imports the given CSV file and returns a performance report.
     *
     * @param csvPath CSV file path
     * @return import performance report
     * @throws IOException when the file cannot be read
     */
    public ImportPerformanceReport importCsv(Path csvPath) throws IOException {
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = usedMemoryMb(runtime);
        long cpuBefore = currentProcessCpuTime();
        long importStart = System.nanoTime();

        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        List<String> dataLines = lines.size() <= 1 ? List.of() : lines.subList(1, lines.size());

        long parsingStart = System.nanoTime();
        ParsedRows parsedRows = parseRowsWithVirtualThreads(dataLines);
        long parsingEnd = System.nanoTime();

        long persistenceStart = System.nanoTime();
        int importedProducts = persistRows(parsedRows.getRows());
        long persistenceEnd = System.nanoTime();

        long importEnd = System.nanoTime();
        long cpuAfter = currentProcessCpuTime();

        return new ImportPerformanceReport(
                dataLines.size(),
                importedProducts,
                parsedRows.getRejectedLines(),
                Duration.ofNanos(parsingEnd - parsingStart),
                Duration.ofNanos(persistenceEnd - persistenceStart),
                Duration.ofNanos(importEnd - importStart),
                Duration.ofNanos(Math.max(0, cpuAfter - cpuBefore)),
                usedMemoryBefore,
                usedMemoryMb(runtime),
                threadMxBean.getPeakThreadCount(),
                dataLines.size());
    }

    private ParsedRows parseRowsWithVirtualThreads(List<String> dataLines) {
        ThreadFactory threadFactory = Thread.ofVirtual().name("off-row-", 0).factory();
        List<ProductCsvRow> rows = new ArrayList<>(dataLines.size());
        int rejectedLines = 0;

        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(threadFactory)) {
            List<Future<Optional<ProductCsvRow>>> futures = new ArrayList<>(dataLines.size());
            for (String line : dataLines) {
                futures.add(executorService.submit(() -> csvParser.parseLine(line)));
            }
            for (Future<Optional<ProductCsvRow>> future : futures) {
                Optional<ProductCsvRow> parsedRow = future.get();
                if (parsedRow.isPresent()) {
                    rows.add(parsedRow.get());
                } else {
                    rejectedLines++;
                }
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Import interrompu pendant le traitement parallele.", exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Erreur pendant le traitement parallele du CSV.", exception);
        }

        return new ParsedRows(rows, rejectedLines);
    }

    private int persistRows(List<ProductCsvRow> rows) {
        Map<String, Categorie> categories = new LinkedHashMap<>();
        Map<String, Marque> marques = new LinkedHashMap<>();
        Map<String, Ingredient> ingredients = new LinkedHashMap<>();
        Map<String, Allergene> allergenes = new LinkedHashMap<>();
        Map<String, Additif> additifs = new LinkedHashMap<>();

        for (ProductCsvRow row : rows) {
            Categorie categorie = categories.computeIfAbsent(
                    cacheKey(row.getCategorie()),
                    ignored -> categorieService.findOrCreateByNom(row.getCategorie()));
            Marque marque = marques.computeIfAbsent(
                    cacheKey(row.getMarque()),
                    ignored -> marqueService.findOrCreateByNom(row.getMarque()));

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
                            ignored -> ingredientService.findOrCreateByNom(ingredientName))));
            row.getAllergenes().forEach(allergeneName -> produit.getAllergenes().add(
                    allergenes.computeIfAbsent(
                            cacheKey(allergeneName),
                            ignored -> allergeneService.findOrCreateByNom(allergeneName))));
            row.getAdditifs().forEach(additifName -> produit.getAdditifs().add(
                    additifs.computeIfAbsent(
                            cacheKey(additifName),
                            ignored -> additifService.findOrCreateByNom(additifName))));

            produitService.save(produit);
        }
        return rows.size();
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
        return value.toLowerCase(Locale.ROOT);
    }

    private static class ParsedRows {
        private final List<ProductCsvRow> rows;
        private final int rejectedLines;

        ParsedRows(List<ProductCsvRow> rows, int rejectedLines) {
            this.rows = rows;
            this.rejectedLines = rejectedLines;
        }

        public List<ProductCsvRow> getRows() {
            return rows;
        }

        public int getRejectedLines() {
            return rejectedLines;
        }
    }
}
