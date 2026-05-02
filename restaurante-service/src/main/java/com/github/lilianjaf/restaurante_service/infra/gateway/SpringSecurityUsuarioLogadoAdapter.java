package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.UsuarioGateway;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("restauranteSpringSecurityAdapter")
public class SpringSecurityUsuarioLogadoAdapter implements ObterUsuarioLogadoGateway {

    private final UsuarioGateway usuarioGateway;

    public SpringSecurityUsuarioLogadoAdapter(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public Optional<Usuario> obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        String username = authentication.getName();

        return usuarioGateway.buscarPorUsuario(username);
    }
}
