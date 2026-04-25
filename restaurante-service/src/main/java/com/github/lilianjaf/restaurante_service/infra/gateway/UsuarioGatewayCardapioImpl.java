package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.TipoNativo;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.UsuarioGateway;
import com.github.lilianjaf.mestremenuclean.usuario.core.api.UsuarioIntegrationDto;
import com.github.lilianjaf.mestremenuclean.usuario.core.api.UsuarioModuleFacade;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioGatewayCardapioImpl implements UsuarioGateway {

    private final UsuarioModuleFacade usuarioFacade;

    public UsuarioGatewayCardapioImpl(UsuarioModuleFacade usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        UsuarioIntegrationDto dto = usuarioFacade.buscarUsuarioParaIntegracao(id);

        if (dto == null) {
            return Optional.empty();
        }

        TipoNativo tipo = null;
        if (dto.tipoNativo() != null) {
            try {
                tipo = TipoNativo.valueOf(dto.tipoNativo());
            } catch (IllegalArgumentException e) {
            }
        }

        return Optional.of(new Usuario(dto.id(), tipo));
    }

    @Override
    public Optional<Usuario> buscarPorUsuario(String username) {
        UsuarioIntegrationDto dto = usuarioFacade.buscarPorUsuario(username);

        if (dto == null) {
            return Optional.empty();
        }

        TipoNativo tipo = null;
        if (dto.tipoNativo() != null) {
            try {
                tipo = TipoNativo.valueOf(dto.tipoNativo());
            } catch (IllegalArgumentException e) {
            }
        }

        return Optional.of(new Usuario(dto.id(), tipo));
    }
}
