package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventsOnMapApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventsOnMapApplication.class, args);
    }
}