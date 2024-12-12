package com.medHead.poc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface TokenControllerInterface {
    ResponseEntity<String> generateToken(@RequestParam String role);
}
