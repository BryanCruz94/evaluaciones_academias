package com.nairbdev.academiasbackend.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret
    ) {
        Map<String, String> cfg = new HashMap<>();
        cfg.put("cloud_name", cloudName);
        cfg.put("api_key", apiKey);
        cfg.put("api_secret", apiSecret);
        return new Cloudinary(cfg);
    }
}