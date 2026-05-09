package com.github.lilianjaf.pedido_service.infra.gateway.mapper;

import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.infra.gateway.entity.ItemPedidoEntity;
import com.github.lilianjaf.pedido_service.infra.gateway.entity.PedidoEntity;

import java.util.List;
import java.util.stream.Collectors;

public class PedidoEntityMapper {

    public static Pedido toDomain(PedidoEntity entity) {
        List<ItemPedido> itens = entity.getItens().stream()
                .map(i -> new ItemPedido(i.getId(), i.getDescricao(), i.getQuantidade(), i.getPreco()))
                .collect(Collectors.toList());

        return new Pedido(
                entity.getId(),
                entity.getClienteId(),
                entity.getRestauranteId(),
                itens,
                entity.getValorTotal(),
                entity.getStatus(),
                entity.getDataCriacao()
        );
    }

    public static PedidoEntity toEntity(Pedido domain) {
        PedidoEntity entity = new PedidoEntity(
                domain.getId(),
                domain.getClienteId(),
                domain.getRestauranteId(),
                domain.getValorTotal(),
                domain.getStatus(),
                domain.getDataCriacao()
        );

        List<ItemPedidoEntity> itemEntities = domain.getItens().stream()
                .map(i -> new ItemPedidoEntity(i.getId(), entity, i.getDescricao(), i.getQuantidade(), i.getPreco()))
                .collect(Collectors.toList());

        entity.getItens().addAll(itemEntities);

        return entity;
    }
}
