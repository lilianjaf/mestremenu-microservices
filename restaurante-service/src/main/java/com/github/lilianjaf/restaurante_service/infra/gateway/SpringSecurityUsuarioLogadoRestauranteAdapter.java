package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.ObterUsuarioLogadoRestauranteGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.UsuarioGateway;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityUsuarioLogadoRestauranteAdapter implements ObterUsuarioLogadoRestauranteGateway {

    private final UsuarioGateway usuario;

    public SpringSecurityUsuarioLogadoRestauranteAdapter(UsuarioGateway usuario) {
        this.usuario = usuario;
    }

    @Override
    public Optional<Usuario> obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        String username = authentication.getName();

        return usuario.buscarPorUsuario(username);
    }
}