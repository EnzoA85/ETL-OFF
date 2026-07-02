package com.example.etl_off.etl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImportLookupCacheTest {

    @Autowired
    private ImportLookupCache importLookupCache;

    @Test
    void shouldCacheValuesForTheSameKey() {
        String first = importLookupCache.getOrCreate("test-cache", "category:chocolate", () -> "chocolate");
        String second = importLookupCache.getOrCreate("test-cache", "category:chocolate", () -> "other");

        assertThat(first).isEqualTo("chocolate");
        assertThat(second).isEqualTo("chocolate");
    }
}
