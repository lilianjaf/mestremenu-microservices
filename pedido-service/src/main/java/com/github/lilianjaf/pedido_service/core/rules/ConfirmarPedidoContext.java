package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;

public record ConfirmarPedidoContext(
        Usuario usuarioLogado,
        Pedido pedido
) {
    public boolean isUsuarioAutenticado() {
        return usuarioLogado != null && usuarioLogado.getId() != null;
    }

    public boolean isPedidoDoCliente() {
        return usuarioLogado != null && pedido != null
                && pedido.getClienteId().equals(usuarioLogado.getId());
    }

    public boolean isPedidoEmRascunho() {
        return pedido != null && pedido.getStatus() == StatusPedido.RASCUNHO;
    }
}
