package com.medHead.poc.config;

import org.apache.catalina.core.StandardThreadExecutor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * Configuration pour le serveur Tomcat intégré.
 */
public class TomcatConfig {

    /**
     * Cette configuration ajuste le gestionnaire de threads de Tomcat en définissant :
     * - Un nombre minimum de threads inactifs pour maintenir une disponibilité optimale.
     * - Un nombre maximum de threads pour gérer les charges élevées.
     * @return Une instance configurée de TomcatServletWebServerFactory.
     */
    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            StandardThreadExecutor executor = (StandardThreadExecutor) connector.getProtocolHandler().getExecutor();
            // Définir le nombre maximum de threads pour traiter les requêtes
            executor.setMaxThreads(3000);
            // Définir le nombre minimum de threads inactifs pour maintenir la disponibilité
            executor.setMinSpareThreads(500);
        });
        return factory;
    }
}
