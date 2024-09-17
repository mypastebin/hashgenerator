package com.hhnatsiuk.hashgenerator_bin.controller;

import com.hhnatsiuk.hashgenerator_bin.service.HashService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate")
public class HashController {

    private static final Logger logger = LogManager.getLogger(HashController.class);

    @Autowired
    private HashService hashService;

    @GetMapping
    public String generateHash() {
        logger.info("Received request to generate hash");
        String hash = hashService.generateUniqueHash();
        logger.info("Hash generated successfully: {}", hash);
        return hash;
    }
}
