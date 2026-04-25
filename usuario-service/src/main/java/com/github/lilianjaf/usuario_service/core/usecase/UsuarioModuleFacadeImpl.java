package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.usuario.core.api.UsuarioIntegrationDto;
import com.github.lilianjaf.mestremenuclean.usuario.core.api.UsuarioModuleFacade;
import com.github.lilianjaf.mestremenuclean.usuario.core.dto.UsuarioOutput;

import java.util.UUID;

public class UsuarioModuleFacadeImpl implements UsuarioModuleFacade {

    private final BuscarUsuarioUsecase buscarUsuarioUsecase;

    public UsuarioModuleFacadeImpl(BuscarUsuarioUsecase buscarUsuarioUsecase) {
        this.buscarUsuarioUsecase = buscarUsuarioUsecase;
    }

    @Override
    public UsuarioIntegrationDto buscarUsuarioParaIntegracao(UUID id) {
        UsuarioOutput output = buscarUsuarioUsecase.buscarPorId(id);

        return new UsuarioIntegrationDto(
                output.id(),
                output.tipoPerfil(),
                output.tipoNativo(),
                output.ativo()
        );
    }

    @Override
    public UsuarioIntegrationDto buscarPorUsuario(String username) {
        UsuarioOutput output = buscarUsuarioUsecase.buscarPorUsername(username);

        return new UsuarioIntegrationDto(
                output.id(),
                output.tipoPerfil(),
                output.tipoNativo(),
                output.ativo()
        );
    }
}