package com.github.lilianjaf.gateway_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;
    private static final String SECRET = "test-secret-key";

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", SECRET);
    }

    private String buildToken(String subject, String userId) {
        var builder = JWT.create()
                .withIssuer("mestre-menu-api")
                .withSubject(subject)
                .withExpiresAt(Date.from(Instant.now().plusSeconds(3600)));
        if (userId != null) {
            builder = builder.withClaim("userId", userId);
        }
        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    @Test
    @DisplayName("getSubject deve retornar o subject do token válido")
    void getSubjectDeveRetornarSubject() {
        String token = buildToken("user@email.com", null);
        assertEquals("user@email.com", tokenService.getSubject(token));
    }

    @Test
    @DisplayName("getSubject deve retornar null para token inválido")
    void getSubjectDeveRetornarNullParaTokenInvalido() {
        assertNull(tokenService.getSubject("invalid.jwt.token"));
    }

    @Test
    @DisplayName("getUserId deve retornar userId da claim")
    void getUserIdDeveRetornarUserId() {
        String userId = UUID.randomUUID().toString();
        String token = buildToken("user@email.com", userId);
        assertEquals(userId, tokenService.getUserId(token));
    }

    @Test
    @DisplayName("getUserId deve retornar null para token inválido")
    void getUserIdDeveRetornarNullParaTokenInvalido() {
        assertNull(tokenService.getUserId("invalid.jwt.token"));
    }

    @Test
    @DisplayName("getUserId deve retornar null quando claim userId não existe")
    void getUserIdDeveRetornarNullSemClaim() {
        String token = buildToken("user@email.com", null);
        assertNull(tokenService.getUserId(token));
    }
}
