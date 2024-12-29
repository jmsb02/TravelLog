package com.travellog;

import com.travellog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class TravelLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelLogApplication.class, args);
    }

}
