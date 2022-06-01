package com.project.reddit.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryBean {

    @Value("${cloudinary-name}")
    private String cloudinaryName;

    @Value("${cloudinary-api-key}")
    private String cloudinaryKey;

    @Value("${cloudinary-secret-key}")
    private String cloudinarySecretKey;

    @Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudinaryName);
        config.put("api_key", cloudinaryKey);
        config.put("api_secret", cloudinarySecretKey);
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

}
