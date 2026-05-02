package com.github.lilianjaf.usuario_service.infra.gateway.mapper;

import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.TipoUsuarioEntity;

public class TipoUsuarioEntityMapper {

    public static TipoUsuario toDomain(TipoUsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return new TipoUsuario(
                entity.getId(),
                entity.getNome(),
                entity.getTipoNativo()
        );
    }

    public static TipoUsuarioEntity toEntity(TipoUsuario domain) {
        if (domain == null) {
            return null;
        }

        return new TipoUsuarioEntity(
                domain.getId(),
                domain.getNome(),
                domain.getTipoNativo()
        );
    }
}