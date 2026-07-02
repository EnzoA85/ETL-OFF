package com.example.etl_off;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

@SpringBootApplication
@EnableCaching
public class EtlOffApplication {

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("import-lookups");
	}

	public static void main(String[] args) {
		SpringApplication.run(EtlOffApplication.class, args);
	}

}
