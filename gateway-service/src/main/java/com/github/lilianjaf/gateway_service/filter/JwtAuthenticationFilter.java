package com.github.lilianjaf.gateway_service.filter;

import com.github.lilianjaf.gateway_service.security.TokenService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final TokenService tokenService;

    public JwtAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        if (isPublic(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        String subject = tokenService.getSubject(token);

        if (subject == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String userId = tokenService.getUserId(token);
        ServerWebExchange mutated = exchange.mutate()
                .request(r -> r
                        .header("X-User-Login", subject)
                        .header("X-User-Id", userId != null ? userId : ""))
                .build();

        return chain.filter(mutated);
    }

    private boolean isPublic(String path, String method) {
        if (path.startsWith("/api/v1/login") ||
            path.startsWith("/api/v1/publico") ||
            path.startsWith("/actuator")) {
            return true;
        }
        return "PUT".equalsIgnoreCase(method) && path.matches("/api/v1/usuarios/[^/]+/senha");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
