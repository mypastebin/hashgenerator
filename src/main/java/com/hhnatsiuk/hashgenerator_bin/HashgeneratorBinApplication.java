package com.hhnatsiuk.hashgenerator_bin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HashgeneratorBinApplication {

    private static final Logger logger = LogManager.getLogger(HashgeneratorBinApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HashgeneratorBinApplication.class, args);
        logger.info("Application started successfully.");
    }

}
