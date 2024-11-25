package com.medHead.poc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration pour le gestionnaire de threads (ThreadPoolTaskExecutor).
 */
public class TheadConfig {

    /**
     * Le gestionnaire de threads est utilisé pour exécuter des tâches asynchrones
     * ou parallèles dans l'application.
     * @return Une instance configurée de ThreadPoolTaskExecutor.
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
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
        return executor;
    }
}
