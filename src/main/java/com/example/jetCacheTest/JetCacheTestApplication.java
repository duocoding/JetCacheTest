package com.example.jetCacheTest;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.alicp.jetcache.autoconfigure"})
@EnableMethodCache(basePackages = "com.example.jetCacheTest")
@EnableCreateCacheAnnotation
public class JetCacheTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JetCacheTestApplication.class, args);
	}

}
