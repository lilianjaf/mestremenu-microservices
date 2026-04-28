package com.github.lilianjaf.restaurante_service.infra.gateway.mapper;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.infra.gateway.entity.CardapioEntity;
import com.github.lilianjaf.restaurante_service.infra.gateway.entity.ItemCardapioEntity;

import java.util.List;
import java.util.stream.Collectors;

public class CardapioEntityMapper {

    public static Cardapio toDomain(CardapioEntity entity) {
        if (entity == null) return null;
        
        List<ItemCardapio> itens = entity.getItens().stream()
                .map(ItemCardapioEntityMapper::toDomain)
                .collect(Collectors.toList());

        return new Cardapio(
                entity.getId(),
                entity.getNome(),
                entity.getIdRestaurante(),
                itens
        );
    }

    public static CardapioEntity toEntity(Cardapio domain) {
        if (domain == null) return null;

        CardapioEntity entity = new CardapioEntity(
                domain.getId(),
                domain.getNome(),
                domain.getIdRestaurante()
        );

        if (domain.getItens() != null) {
            List<ItemCardapioEntity> itemEntities = domain.getItens().stream()
                    .map(item -> ItemCardapioEntityMapper.toEntity(item, entity))
                    .collect(Collectors.toList());
            entity.setItens(itemEntities);
        }

        return entity;
    }

    public static void atualizarEntity(Cardapio domain, CardapioEntity entity) {
        entity.setNome(domain.getNome());
        entity.setIdRestaurante(domain.getIdRestaurante());
    }
}
