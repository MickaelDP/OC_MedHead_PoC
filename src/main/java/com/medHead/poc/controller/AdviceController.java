package com.medHead.poc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestionnaire global des exceptions pour les contrôleurs REST.
 */
@RestControllerAdvice
public class AdviceController {
    /**
     * Gère les exceptions IllegalArgumentException.
     * Retourne une réponse HTTP 400 avec le message d'erreur.
     *
     * @param ex L'exception capturée.
     * @return Une réponse avec le statut 400 et le message d'erreur.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
