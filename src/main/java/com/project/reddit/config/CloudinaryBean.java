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

    @Value("${CLOUDINARY_NAME}")
    private String cloudinaryName;

    @Value("${CLOUDINARY_API_KEY}")
    private String cloudinaryKey;

    @Value("${CLOUDINARY_SECRET_KEY}")
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
