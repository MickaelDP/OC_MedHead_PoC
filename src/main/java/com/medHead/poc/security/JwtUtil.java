package com.medHead.poc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Classe utilitaire pour la gestion des tokens JWT (JSON Web Tokens).
 * Cette classe permet de générer, signer et valider des tokens JWT
 * en utilisant une clé secrète définie dans les propriétés de l'application.
 */
@Component
public class JwtUtil implements JwTUtilInterface {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final Marker APP_MARKER = MarkerFactory.getMarker("APP_FILE"); // Marqueur pour les logs non HTTP

    /**
     * Clé secrète utilisée pour signer les tokens JWT.
     * La valeur est injectée depuis le fichier `application.properties`.
     * pour simplifier la logique dans la poc du fait de manque de contexte de loggin
     */
    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    /**
     * Méthode appelée après l'injection des propriétés pour initialiser la clé
     * HMAC (Hash-based Message Authentication Code).
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        logger.info(APP_MARKER, "Clé JWT initialisée avec succès.");
    }
    /**
     * Génère un token JWT pour un utilisateur donné.
     *
     * @param username le nom d'utilisateur ou identifiant unique.
     * @param role le rôle de l'utilisateur (ex. "ROLE_QUALIFIED_USER").
     * @return un token JWT signé contenant le sujet, les rôles, et une date d'expiration.
     */
    public String generateToken(String username, String role) {
        logger.debug(APP_MARKER, "Génération du token pour l'utilisateur: {}", username);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 jour
                .signWith(key)
                .compact();

        logger.debug(APP_MARKER, "Token JWT généré pour l'utilisateur: {}", username);
        return token;
    }

    /**
     * Valide un token JWT et retourne les claims contenus dans le token.
     *
     * @param token le token JWT à valider.
     * @return un objet Claims contenant les informations décryptées du token.
     * @throws io.jsonwebtoken.JwtException si le token est invalide ou expiré.
     */
    public Claims validateToken(String token) {
        logger.debug(APP_MARKER, "Validation du token JWT.");
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            logger.debug(APP_MARKER, "Token validé avec succès.");
            return claims;
        } catch (Exception e) {
            logger.error(APP_MARKER, "Échec de la validation du token JWT: {}", e.getMessage());
            throw e;  // Lancer l'exception pour la gestion dans le filtre
        }
    }
    /**
     * Méthode de test pour générer un token JWT en local.
     * Utile pour créer des tokens mockés dans le cadre de la PoC.
     * Décommenter pour utiliser au besoin
     * @param args les arguments de la ligne de commande (non utilisés).
     */

    /*
    public static void main(String[] args) {
        String secret = "kpPb9R2v7NcGxJ5mQuZnR7q6k7Z3NdMv8XkM4A7C6aPp9W2q3RyR7NzV9QqR3A8x";             //clé de application.properties jwt.secret pour genéré jwt.fixed-token
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()                                                                   // pour la poc aucune expiration
                .setSubject("test-user")
                .claim("role", "ROLE_QUALIFIED_USER")
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        System.out.println("Generated Token: " + token);
    }*/
}