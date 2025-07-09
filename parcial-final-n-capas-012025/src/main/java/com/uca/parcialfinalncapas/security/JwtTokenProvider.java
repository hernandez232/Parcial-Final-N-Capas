package com.uca.parcialfinalncapas.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    // Obtener los valores necesarios desde el pom
    @Value("${app.jwt-secret}")
    private String secret;

    @Value("${app.jwt-expiration-time}")
    private String expirationTime;

    // Crear una función para generar el token con JWT
    public String generateToken(Authentication auth) {
        String username = auth.getName(); // Retrieve the username from the authentication object
        Date now = new Date(); // Current date and time
        Date expirationDate = new Date(now.getTime() + Long.parseLong(expirationTime)); // Calculate the expiration date
        // Build and sign the JWT token
        String token = Jwts.builder()
                .setSubject(username) // Set the username as the subject
                .setIssuedAt(now) // Set the issue date
                .setExpiration(expirationDate) // Set the expiration date
                .signWith(getKey()) // Sign the token with the secret key
                .compact();
        return token; // Return the generated token
    }

    // Función auxiliar para obtener la llave
    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // Crear una para obtener el username desde el token
    public String getUsernameFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Extract the username from the token

        return username; // Return the extracted username
    }

    // Crear una función para validar el token
    public boolean validateToken(String token) {
        Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parse(token);

        return true;
    }
}
