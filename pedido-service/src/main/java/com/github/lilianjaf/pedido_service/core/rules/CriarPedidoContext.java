package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;

import java.util.List;
import java.util.UUID;

public record CriarPedidoContext(
        Usuario usuarioLogado,
        UUID restauranteId,
        List<ItemPedido> itens
) {
    public boolean isUsuarioAutenticado() {
        return usuarioLogado != null && usuarioLogado.getId() != null;
    }

    public boolean isItensPedidoVazio() {
        return itens == null || itens.isEmpty();
    }
}
