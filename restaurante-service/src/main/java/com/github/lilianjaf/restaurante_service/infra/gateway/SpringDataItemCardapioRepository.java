package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.mestremenuclean.cardapio.infra.gateway.entity.ItemCardapioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataItemCardapioRepository extends JpaRepository<ItemCardapioEntity, UUID> {
    List<ItemCardapioEntity> findAllByCardapioId(UUID cardapioId);
    boolean existsByNomeAndCardapioId(String nome, UUID cardapioId);
    boolean existsByNomeAndCardapioIdAndIdNot(String nome, UUID cardapioId, UUID id);
}
