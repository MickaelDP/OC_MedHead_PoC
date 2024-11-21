package com.medHead.poc.testUnitaire.config;

import com.medHead.poc.services.PopulateHopitalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RestTemplateConfigUTest {
    @Autowired
    private PopulateHopitalService populateHopitalService;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void testRestTemplateInjection() {
        assertNotNull(populateHopitalService, "Le service PopulateHopitalService n'a pas été injecté.");
        assertNotNull(restTemplate, "Le RestTemplate n'a pas été injecté.");
    }
}
