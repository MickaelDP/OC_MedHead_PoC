package com.medHead.poc.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtre d'authentification basé sur JWT (JSON Web Token).
 * Ce filtre est exécuté pour chaque requête HTTP afin de valider le token JWT
 * présent dans l'en-tête d'autorisation et d'authentifier l'utilisateur.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final Marker HTTP_MARKER = MarkerFactory.getMarker("HTTP_FILE");

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Méthode principale du filtre pour traiter la requête et valider le token JWT.
     *
     * @param request  la requête HTTP.
     * @param response la réponse HTTP.
     * @param chain    la chaîne de filtres permettant de passer à la requête suivante.
     * @throws IOException      en cas de problème d'entrée/sortie.
     * @throws ServletException en cas d'erreur au niveau du traitement de la requête.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Log de la requête entrante
        logger.debug(HTTP_MARKER, "Request received: {} {}", request.getMethod(), request.getRequestURI());

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            logger.warn(HTTP_MARKER, "No valid Authorization header found for request: {}", request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            Claims claims = jwtUtil.validateToken(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            // Log du résultat de la validation du token
            logger.debug(HTTP_MARKER, "Token validated for user: {} with role: {}", username, role);

            if (username != null && role != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Log des erreurs de validation du token
            logger.error(HTTP_MARKER, "Token validation failed for request: {}. Error: {}", request.getRequestURI(), e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}
