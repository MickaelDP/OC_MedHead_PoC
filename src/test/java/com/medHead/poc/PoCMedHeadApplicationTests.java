package com.medHead.poc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Locale;


/**
 * Classe de tests pour l'application PoCMedHeadApplication.
 *
 * Contient des tests pour vérifier le bon démarrage de l'application et le chargement du contexte Spring.
 */
@SpringBootTest
class PoCMedHeadApplicationTests {
	@Autowired
	private ApplicationContext context;

	/**
	 * Test qui vérifie que le contexte de l'application est correctement initialisé.
	 *
	 * Ce test garantit que l'environnement Spring Boot est fonctionnel et que les beans
	 * nécessaires sont créés sans erreurs.
	 */
	@Test
	void contextLoads() {
		// Vérifie que le contexte de l'application est chargé
		assertNotNull(context, "Le contexte Spring n'a pas été initialisé.");
	}

	/**
	 * Test de la méthode principale (main) de l'application.
	 *
	 * Ce test vérifie que la méthode main peut s'exécuter sans lancer d'exceptions.
	 * Il simule un démarrage standard de l'application.
	 */
	@Test
	void testMainMethod() {
		// Vérifie que la méthode main ne lance pas d'exception
		assertDoesNotThrow(() -> {
			PoCMedHeadApplication.main(new String[]{});
		});
	}
}
