package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.restaurante_service.core.domain.TipoNativo;
import com.github.lilianjaf.restaurante_service.core.domain.TipoUsuario;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("restauranteSpringSecurityAdapter")
public class SpringSecurityUsuarioLogadoAdapter implements ObterUsuarioLogadoGateway {

    @Override
    public Optional<Usuario> obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return Optional.empty();
        }
        if (!(authentication.getPrincipal() instanceof UUID userId)) {
            return Optional.empty();
        }
        String tipoNativo = (String) authentication.getDetails();
        if (tipoNativo == null) {
            return Optional.empty();
        }
        return Optional.of(new Usuario(userId, true, new TipoUsuario(tipoNativo, tipoNativo)));
    }
}
