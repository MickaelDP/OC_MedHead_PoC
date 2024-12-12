package com.medHead.poc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration pour le gestionnaire de threads (ThreadPoolTaskExecutor).
 */
public class TheadConfig {

    private static final Logger logger = LoggerFactory.getLogger(TheadConfig.class);
    private static final Marker APP_MARKER = MarkerFactory.getMarker("APP_FILE");


    /**
     * Le gestionnaire de threads est utilisé pour exécuter des tâches asynchrones
     * ou parallèles dans l'application.
     * @return Une instance configurée de ThreadPoolTaskExecutor.
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        logger.info(APP_MARKER, "Configuration du ThreadPoolTaskExecutor en cours.");


        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Nombre de threads de base disponibles dans le pool
        executor.setCorePoolSize(500);
        // Nombre maximum de threads dans le pool
        executor.setMaxPoolSize(3000);
        // Taille de la file d'attente pour les tâches en attente
        executor.setQueueCapacity(1000);
        // Préfixe utilisé pour nommer les threads
        executor.setThreadNamePrefix("MyExecutor-");
        // Initialisation du ThreadPoolTaskExecutor
        executor.initialize();

        logger.info(APP_MARKER, "ThreadPoolTaskExecutor configuré avec : corePoolSize={}, maxPoolSize={}, queueCapacity={}",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());

        return executor;
    }
}
