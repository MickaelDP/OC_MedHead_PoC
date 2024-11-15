package com.medHead.poc.controller;

import com.medHead.poc.entities.Result;
import com.medHead.poc.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les résultats des recherches d'hôpitaux pour les patients.
 */
@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    /**
     * Récupère tous les résultats.
     * @return Une liste de tous les résultats enregistrés.
     */
    @GetMapping
    public ResponseEntity<List<Result>> getAllResults() {
        List<Result> results = resultService.getAllResults();
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();                      // 204 No Content si la liste est vide
        }
        return ResponseEntity.ok(results);
    }

    /**
     * Récupère un résultat spécifique par ID.
     * @param id L'ID du résultat à récupérer.
     * @return Le résultat correspondant à l'ID, ou une réponse 404 si non trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result> getResultById(@PathVariable Long id) {
        Result result = resultService.getResultById(id);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();                                                       // 404 Not Found si le résultat n'existe pas
    }

    /**
     * Crée un nouveau résultat.
     * @param result Les détails du résultat à créer (fournis dans le corps de la requête).
     * @return Le résultat créé avec un ID unique.
     */
    @PostMapping
    public ResponseEntity<Result> createResult(@RequestBody Result result) {
        Result savedResult = resultService.saveResult(result);
        return ResponseEntity.ok(savedResult);                                                          // 200 OK avec le résultat créé
    }

    /**
     * Supprime un résultat par ID.
     * @param id L'ID du résultat à supprimer.
     * @return Une réponse indiquant le succès ou l'échec de la suppression.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResult(@PathVariable Long id) {
        boolean deleted = resultService.deleteResult(id);
        if (deleted) {
            return ResponseEntity.ok("Result supprimé avec succès.");                              // 200 OK pour suppression réussie
        }
        return ResponseEntity.status(404).body("Échec de la suppression : Aucun Result trouvé.");        // 404 Not Found si non trouvé
    }
}