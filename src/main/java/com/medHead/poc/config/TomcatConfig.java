package com.medHead.poc.config;

import org.apache.catalina.core.StandardThreadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * Configuration pour le serveur Tomcat intégré.
 */
public class TomcatConfig {

    private static final Logger logger = LoggerFactory.getLogger(TomcatConfig.class);
    private static final Marker APP_MARKER = MarkerFactory.getMarker("APP_FILE");

    /**
     * Cette configuration ajuste le gestionnaire de threads de Tomcat en définissant :
     * - Un nombre minimum de threads inactifs pour maintenir une disponibilité optimale.
     * - Un nombre maximum de threads pour gérer les charges élevées.
     * @return Une instance configurée de TomcatServletWebServerFactory.
     */
    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        logger.info(APP_MARKER, "Configuration du gestionnaire de threads de Tomcat en cours.");

        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            StandardThreadExecutor executor = (StandardThreadExecutor) connector.getProtocolHandler().getExecutor();
            // Définir le nombre maximum de threads pour traiter les requêtes
            executor.setMaxThreads(3000);
            // Définir le nombre minimum de threads inactifs pour maintenir la disponibilité
            executor.setMinSpareThreads(500);

            logger.info(APP_MARKER, "Tomcat thread pool configuré avec maxThreads={} et minSpareThreads={}",
                    executor.getMaxThreads(), executor.getMinSpareThreads());
        });
        return factory;
    }
}
