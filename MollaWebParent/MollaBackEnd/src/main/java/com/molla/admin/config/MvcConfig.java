package com.molla.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "user-photos";
        Path userPhotoDir = Paths.get(dirName);
        String userPhotosPath = userPhotoDir.toFile().getAbsolutePath();
        registry.addResourceHandler(dirName + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");

        String categoryImagesDirName = "../category-images";
        Path categoryImagesDir = Paths.get(categoryImagesDirName);
        String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/category-images/**")
                .addResourceLocations("file:/" + categoryImagesPath + "/");

        String brandLogosDirName = "../brand-logos";
        Path brandLogosDir = Paths.get(brandLogosDirName);
        String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/brand-logos/**")
                .addResourceLocations("file:/" + brandLogosPath + "/");
    }
}
