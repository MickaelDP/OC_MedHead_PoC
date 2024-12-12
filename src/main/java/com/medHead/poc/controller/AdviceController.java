package com.medHead.poc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestionnaire global des exceptions pour les contrôleurs REST.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestControllerAdvice
public class AdviceController {
    private static final Logger logger = LoggerFactory.getLogger(AdviceController.class);
    private static final Marker HTTP_MARKER = MarkerFactory.getMarker("HTTP_FILE");

    /**
     * Gère les exceptions IllegalArgumentException.
     * Retourne une réponse HTTP 400 avec le message d'erreur.
     * @param ex L'exception capturée.
     * @return Une réponse avec le statut 400 et le message d'erreur.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        String safeMessage = ex.getMessage().length() > 200
                ? ex.getMessage().substring(0, 200) + "..."
                : ex.getMessage();

        // Journaliser l'erreur avec le marqueur HTTP_FILE
        logger.error(HTTP_MARKER, "Erreur dans la requête: {}", safeMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(safeMessage);
    }
}
