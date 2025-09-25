package com.donation.ddb;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/DDADDABLCH/")
                .setCachePeriod(3600);
//    }
//@Value("${app.files.nft-dir}")
//private String nftDir; // 예: C:/DDADDABLCH/nft/
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // http://localhost:8080/nft/**  ->  C:/DDADDABLCH/nft/**
//        registry.addResourceHandler("/nft/**")
//                .addResourceLocations("file:" + nftDir)  // file: 접두사 + 끝 슬래시 주의
//                .setCachePeriod(3600);
//    }
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // 프론트(5173)에서 이미지/파일 접근 허용
//        registry.addMapping("/nft/**")
//                .allowedOrigins("http://localhost:5173")
//                .allowedMethods("GET");
//
//        // API도 같이 열어두면 편함
//        registry.addMapping("/api/**")
//                .allowedOrigins("http://localhost:5173")
//                .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS");
//    }

}
}
