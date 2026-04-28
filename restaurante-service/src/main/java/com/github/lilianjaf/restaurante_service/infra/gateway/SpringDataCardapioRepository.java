package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.restaurante_service.infra.gateway.entity.CardapioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataCardapioRepository extends JpaRepository<CardapioEntity, UUID> {
    List<CardapioEntity> findAllByIdRestaurante(UUID idRestaurante);
    boolean existsByNomeAndIdRestaurante(String nome, UUID idRestaurante);
}
