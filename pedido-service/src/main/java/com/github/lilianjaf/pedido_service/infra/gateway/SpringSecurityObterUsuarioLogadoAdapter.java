package com.github.lilianjaf.pedido_service.infra.gateway;

import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import com.github.lilianjaf.pedido_service.core.gateway.ObterUsuarioLogadoGateway;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SpringSecurityObterUsuarioLogadoAdapter implements ObterUsuarioLogadoGateway {

    @Override
    public Optional<Usuario> obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UUID userId)) {
            return Optional.empty();
        }
        return Optional.of(new Usuario(userId));
    }
}
