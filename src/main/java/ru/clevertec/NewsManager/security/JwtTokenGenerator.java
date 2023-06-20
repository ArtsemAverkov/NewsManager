package ru.clevertec.NewsManager.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 The JwtTokenGenerator class is responsible for generating JWT (JSON Web Token) tokens for authentication.
 It uses a secret key and sets the expiration time and user role in the token.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private static String SECRET_KEY;// = "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";
    private static final long EXPIRATION_TIME = 86400000;
    private static final String role = "SUBSCRIBER";

    /**
     Generates a JWT token for the given username.
     @param username the username for which the token is generated
     @return the generated JWT token
     */

    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

        return builder.compact();
    }
}
