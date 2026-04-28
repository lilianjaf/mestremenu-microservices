package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.restaurante_service.core.domain.TipoUsuario;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.gateway.UsuarioGateway;
import com.github.lilianjaf.usuario_service.core.api.UsuarioIntegrationDto;
import com.github.lilianjaf.usuario_service.core.api.UsuarioModuleFacade;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioModuleFacade usuarioFacade;

    public UsuarioGatewayImpl(UsuarioModuleFacade usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID usuarioId) {
        UsuarioIntegrationDto dto = usuarioFacade.buscarUsuarioParaIntegracao(usuarioId);

        if (dto == null) {
            return Optional.empty();
        }

        return Optional.of (new Usuario(
                dto.id(),
                dto.ativo(),
                new TipoUsuario(dto.nomeDoTipo(), dto.tipoNativo())
        ));
    }

    @Override
    public Optional<Usuario> buscarPorUsuario(String username) {
        UsuarioIntegrationDto dto = usuarioFacade.buscarPorUsuario(username);

        if (dto == null) {
            return Optional.empty();
        }

        return Optional.of (new Usuario(
                dto.id(),
                dto.ativo(),
                new TipoUsuario(dto.nomeDoTipo(), dto.tipoNativo())
        ));
    }
}