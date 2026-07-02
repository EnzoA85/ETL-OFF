package com.example.etl_off.etl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class OpenFoodFactsImportBatchConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldCreateBatchImportJob() {
        assertThat(applicationContext.getBeanNamesForType(Job.class))
                .contains("openFoodFactsImportJob");
    }
}
