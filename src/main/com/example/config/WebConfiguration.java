package com.example.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.CacheControl;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
@ComponentScan("com.example")
public class WebConfiguration implements WebMvcConfigurer {

    /*private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/public/",
            "classpath:/css/"
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        *//*registry
                .addResourceHandler("/css/**")
                .addResourceLocations("/WEB-INFO/static/css/");
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations("/WEB-INFO/static/images/");
        registry
                .addResourceHandler("/js/**")
                .addResourceLocations("/WEB-INFO/static/js/");*//*
                *//*.addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);*//*
        System.out.println("!!!!!!!!!!!!!!!!!!!!!" + registry);
    }*/

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:validation");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

   /* @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }*/

    /*@Bean(name = "multipartResolver")
    public CommonsMultipartResolver getMultipartResolver() {
        return new CommonsMultipartResolver();
    }*/



    /*public void addResourceHandlers(ResourceHandlerRegistry registry) {

        int cacheMaxAge = 5 * 60;

        registry
                .addResourceHandler("/js/**")
                .addResourceLocations("/resources/static/js/");
        registry
                .addResourceHandler("/resources/css/**")
                .addResourceLocations("/resources/static/css/");
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations("/resources/static/images/");
        WebMvcConfigurer.super.addResourceHandlers(registry);

    }*/


    /*@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }*/

    /*@Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/resources/static/js/");
        resolver.setSuffix(".js");
        resolver.setViewClass(JstlView.class);

        return resolver;
    }*/


}