package com.github.lilianjaf.gateway_service.filter;

import com.github.lilianjaf.gateway_service.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock private TokenService tokenService;
    @Mock private GatewayFilterChain chain;
    @InjectMocks private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        lenient().when(chain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("Rota /api/v1/login deve passar sem validar token")
    void loginPathDevePassar() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/login").build());

        filter.filter(exchange, chain).block();

        verify(chain).filter(any());
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("Rota /api/v1/publico/* deve passar sem validar token")
    void publicoPathDevePassar() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/publico/usuarios").build());

        filter.filter(exchange, chain).block();

        verify(chain).filter(any());
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("PUT /api/v1/usuarios/{id}/senha deve passar sem validar token")
    void putSenhaPathDevePassar() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.put("/api/v1/usuarios/abc-123/senha").build());

        filter.filter(exchange, chain).block();

        verify(chain).filter(any());
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("Rota /actuator/* deve passar sem validar token")
    void actuatorPathDevePassar() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/actuator/health").build());

        filter.filter(exchange, chain).block();

        verify(chain).filter(any());
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("Requisição protegida sem Authorization deve retornar 401")
    void semAuthorizationDeveRetornar401() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/pedidos").build());

        filter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verify(chain, never()).filter(any());
    }

    @Test
    @DisplayName("Token inválido deve retornar 401")
    void tokenInvalidoDeveRetornar401() {
        when(tokenService.getSubject("bad-token")).thenReturn(null);

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/pedidos")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer bad-token")
                        .build());

        filter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verify(chain, never()).filter(any());
    }

    @Test
    @DisplayName("Token válido deve propagar X-User-Login e X-User-Id e chamar chain")
    void tokenValidoDeveMutarHeadersEChamarChain() {
        when(tokenService.getSubject("valid-token")).thenReturn("user@email.com");
        when(tokenService.getUserId("valid-token")).thenReturn("uuid-456");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/pedidos")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer valid-token")
                        .build());

        ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);
        filter.filter(exchange, chain).block();

        verify(chain).filter(captor.capture());
        ServerWebExchange mutated = captor.getValue();
        assertEquals("user@email.com", mutated.getRequest().getHeaders().getFirst("X-User-Login"));
        assertEquals("uuid-456", mutated.getRequest().getHeaders().getFirst("X-User-Id"));
    }

    @Test
    @DisplayName("getOrder deve retornar -1")
    void getOrderDeveRetornarMenosUm() {
        assertEquals(-1, filter.getOrder());
    }
}
