package com.example.etl_off.etl;

import java.util.function.Supplier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ImportLookupCache {

    @Cacheable(cacheNames = "import-lookups", key = "#namespace + ':' + #key")
    public <T> T getOrCreate(String namespace, String key, Supplier<T> supplier) {
        return supplier.get();
    }
}
