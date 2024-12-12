package com.medHead.poc.config;

import com.medHead.poc.controller.TokenController;
import jakarta.annotation.PreDestroy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * Configuration globale pour le RestTemplate utilisé dans l'application.
 * Cette classe :
 * - Configure un pool de connexions pour optimiser les appels HTTP.
 * - Configure les timeouts des requêtes HTTP.
 * - Désactive temporairement la validation SSL (uniquement en développement ou PoC).
 */
@Configuration
public class RestTemplateConfig {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);
    private static final Marker APP_MARKER = MarkerFactory.getMarker("APP_FILE");

    @Value("${jwt.fixed-token}") // Injecte le token JWT depuis application.properties
    private String fixedToken;


    /**
     * Contrôleur utilisé pour ajouter le token CSRF aux en-têtes des requêtes.
     */
    @Autowired
    private TokenController tokenController; // Utilisation du TokenController pour le CSRF


    /**
     * Gestionnaire de pool de connexions pour optimiser les performances des appels HTTP.
     */
    private PoolingHttpClientConnectionManager connectionManager;

    /**
     * Factory HTTP personnalisée pour gérer les timeouts et la configuration des connexions.
     */
    private SimpleClientHttpRequestFactory requestFactory;

    /**
     * Crée et configure une instance de RestTemplate.
     *
     * @return Un RestTemplate configuré avec un pool de connexions et des timeouts adaptés.
     */
    @Bean
    public RestTemplate restTemplate() {
        try {
            // Désactivation de la validation SSL
            disableSslValidation();

            // Configuration du gestionnaire de pool de connexions
            configureConnectionManager();

            // Configuration de la factory HTTP avec gestion des timeouts
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
                @Override
                protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                    super.prepareConnection(connection, httpMethod);
                    connection.setRequestProperty("SO_REUSEADDR", "true");
                    connection.setConnectTimeout(600);                                              // Timeout de connexion
                    connection.setReadTimeout(400);                                                 // Timeout de lecture
                }
            };

            // Création du RestTemplate avec la factory configurée
            RestTemplate restTemplate = new RestTemplate(requestFactory);

            // Ajout d'un intercepteur pour injecter le JWT et le CSRF
            restTemplate.getInterceptors().add((request, body, execution) -> {
                // Ajouter l'en-tête Authorization avec un JWT fixe
                request.getHeaders().set("Authorization", "Bearer " + fixedToken);
                // Ajouter le token CSRF
                tokenController.addCsrfHeader(request.getHeaders());

                return execution.execute(request, body);
            });

            // Log de succès de la configuration
            logger.info(APP_MARKER, "RestTemplate configuré avec succès.");
            return restTemplate;
        } catch (Exception e) {
            // Log d'erreur lors de la configuration
            logger.error(APP_MARKER, "Erreur lors de la configuration du RestTemplate : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la configuration du RestTemplate", e);
        }
    }

    /**
     * Configure le gestionnaire de pool de connexions HTTP.
     * Ce gestionnaire optimise les performances en réutilisant les connexions existantes.
     */
    private void configureConnectionManager() {
        connectionManager = new PoolingHttpClientConnectionManager(10, TimeUnit.SECONDS);
        connectionManager.setMaxTotal(100);                                                         // Nombre total maximum de connexions
        connectionManager.setDefaultMaxPerRoute(50);                                                // Nombre maximum de connexions par route
        logger.info(APP_MARKER, "PoolingHttpClientConnectionManager configuré avec maxTotal={} et maxPerRoute={}",
                connectionManager.getMaxTotal(), connectionManager.getDefaultMaxPerRoute());
    }

    /**
     * Désactive la validation SSL pour accepter tous les certificats.
     */
    private void disableSslValidation() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        logger.info(APP_MARKER, "Validation SSL désactivée.");
    }

    /**
     * Libère les ressources du gestionnaire de pool de connexions lors de la destruction du bean.
     * Cette méthode est appelée automatiquement par le conteneur Spring à la fin de l'application.
     */
    @PreDestroy
    public void cleanup() {
        if (connectionManager != null) {
            logger.info(APP_MARKER, "Nettoyage du PoolingHttpClientConnectionManager.");
            connectionManager.close();
        }
    }
}
