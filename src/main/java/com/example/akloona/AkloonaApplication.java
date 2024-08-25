package com.example.akloona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class AkloonaApplication {


    public static void main(String[] args) {
        SpringApplication.run(AkloonaApplication.class, args);
    }

}
