package com.github.lilianjaf.restaurante_service.infra.gateway.mapper;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.infra.gateway.entity.CardapioEntity;
import com.github.lilianjaf.restaurante_service.infra.gateway.entity.ItemCardapioEntity;

public class ItemCardapioEntityMapper {

    public static ItemCardapio toDomain(ItemCardapioEntity entity) {
        if (entity == null) return null;
        return new ItemCardapio(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.isDisponibilidadeRestaurante(),
                entity.getCaminhoFoto(),
                entity.getCardapio().getId()
        );
    }

    public static ItemCardapioEntity toEntity(ItemCardapio domain, CardapioEntity cardapioEntity) {
        if (domain == null) return null;
        return new ItemCardapioEntity(
                domain.getId(),
                domain.getNome(),
                domain.getDescricao(),
                domain.getPreco(),
                domain.isDisponibilidadeRestaurante(),
                domain.getCaminhoFoto(),
                cardapioEntity
        );
    }

    public static void atualizarEntity(ItemCardapio domain, ItemCardapioEntity entity) {
        if (domain == null || entity == null) return;
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setPreco(domain.getPreco());
        entity.setDisponibilidadeRestaurante(domain.isDisponibilidadeRestaurante());
        entity.setCaminhoFoto(domain.getCaminhoFoto());
    }
}
