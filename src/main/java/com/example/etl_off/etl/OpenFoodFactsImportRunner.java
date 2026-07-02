package com.example.etl_off.etl;

import java.nio.file.Path;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Runs the optimized import when explicitly enabled.
 */
@Component
@ConditionalOnProperty(name = "etl.import.enabled", havingValue = "true")
public class OpenFoodFactsImportRunner implements ApplicationRunner {

    private final JobLauncher jobLauncher;
    private final Job importJob;

    @Value("${etl.import.file:open-food-facts.csv}")
    private String importFile;

    public OpenFoodFactsImportRunner(JobLauncher jobLauncher, Job importJob) {
        this.jobLauncher = jobLauncher;
        this.importJob = importJob;
    }

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) throws Exception {
        System.out.println("Starting Open Food Facts batch import from " + importFile);
        JobExecution execution = jobLauncher.run(
                importJob,
                new JobParametersBuilder()
                        .addLong("timestamp", System.currentTimeMillis())
                        .addString("inputFile", importFile)
                        .toJobParameters());
        System.out.println("Batch import finished with status: " + execution.getStatus());
    }
}
