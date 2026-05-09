package com.github.lilianjaf.pagamento_service.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {

    @Value("${api.security.token.secret:my-secret-key}")
    private String secret;

    public String getSubject(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("mestre-menu-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public UUID getUserId(String token) {
        try {
            String userId = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("mestre-menu-api")
                    .build()
                    .verify(token)
                    .getClaim("userId")
                    .asString();
            return userId != null ? UUID.fromString(userId) : null;
        } catch (JWTVerificationException | IllegalArgumentException e) {
            return null;
        }
    }
}
