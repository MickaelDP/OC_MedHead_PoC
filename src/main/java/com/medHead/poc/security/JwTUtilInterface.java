package com.medHead.poc.security;

import io.jsonwebtoken.Claims;

public interface JwTUtilInterface {
    String generateToken(String username, String role);
    Claims validateToken(String token);
}
