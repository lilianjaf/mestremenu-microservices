package com.github.lilianjaf.usuario_service.infra.security;

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

    private String buildToken(String login, UUID userId, String tipoNativo) {
        return JWT.create()
                .withIssuer("mestre-menu-api")
                .withSubject(login)
                .withClaim("userId", userId.toString())
                .withClaim("tipoNativo", tipoNativo)
                .withExpiresAt(Date.from(Instant.now().plusSeconds(3600)))
                .sign(Algorithm.HMAC256(SECRET));
    }

    @Test
    @DisplayName("gerarToken deve retornar token não nulo")
    void gerarTokenDeveRetornarTokenNaoNulo() {
        UUID userId = UUID.randomUUID();
        String token = tokenService.gerarToken("user@email.com", userId, "DONO");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("getSubject deve retornar o subject do token válido")
    void getSubjectDeveRetornarSubject() {
        UUID userId = UUID.randomUUID();
        String token = buildToken("user@email.com", userId, "DONO");
        assertEquals("user@email.com", tokenService.getSubject(token));
    }

    @Test
    @DisplayName("getSubject deve retornar null para token inválido")
    void getSubjectDeveRetornarNullParaTokenInvalido() {
        assertNull(tokenService.getSubject("token.invalido.aqui"));
    }

    @Test
    @DisplayName("getUserId deve retornar UUID do token válido")
    void getUserIdDeveRetornarUUID() {
        UUID userId = UUID.randomUUID();
        String token = buildToken("user@email.com", userId, "DONO");
        assertEquals(userId, tokenService.getUserId(token));
    }

    @Test
    @DisplayName("getUserId deve retornar null para token inválido")
    void getUserIdDeveRetornarNullParaTokenInvalido() {
        assertNull(tokenService.getUserId("token.invalido.aqui"));
    }

    @Test
    @DisplayName("gerarToken seguido de getSubject deve ser round-trip consistente")
    void gerarEGetSubjectDevemSerConsistentes() {
        UUID userId = UUID.randomUUID();
        String token = tokenService.gerarToken("roundtrip@email.com", userId, "CLIENTE");
        assertEquals("roundtrip@email.com", tokenService.getSubject(token));
        assertEquals(userId, tokenService.getUserId(token));
    }
}
