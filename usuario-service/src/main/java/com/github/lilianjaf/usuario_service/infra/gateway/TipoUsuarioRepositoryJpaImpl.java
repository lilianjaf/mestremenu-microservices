package com.github.lilianjaf.usuario_service.infra.gateway;

import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.TipoUsuarioEntity;
import com.github.lilianjaf.usuario_service.infra.gateway.mapper.TipoUsuarioEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TipoUsuarioRepositoryJpaImpl implements TipoUsuarioRepository {

    private final SpringDataTipoUsuarioRepository jpaRepository;

    public TipoUsuarioRepositoryJpaImpl(SpringDataTipoUsuarioRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<TipoUsuario> findByNome(String nome) {
        return jpaRepository.findByNome(nome)
                .map(TipoUsuarioEntityMapper::toDomain);
    }

    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
        TipoUsuarioEntity entity = TipoUsuarioEntityMapper.toEntity(tipoUsuario);

        TipoUsuarioEntity salva = jpaRepository.save(entity);

        return TipoUsuarioEntityMapper.toDomain(salva);
    }

    @Override
    public Optional<TipoUsuario> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(TipoUsuarioEntityMapper::toDomain);
    }

    @Override
    public void deletar(TipoUsuario tipo) {
        TipoUsuarioEntity entity = TipoUsuarioEntityMapper.toEntity(tipo);
        jpaRepository.delete(entity);
    }
}