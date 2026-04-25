package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.TipoUsuario;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.UsuarioGateway;
import com.github.lilianjaf.mestremenuclean.usuario.core.api.UsuarioIntegrationDto;
import com.github.lilianjaf.mestremenuclean.usuario.core.api.UsuarioModuleFacade;
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
    public Optional<Usuario> buscarUsuarioPorId(UUID usuarioId) {
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