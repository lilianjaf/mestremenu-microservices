package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.domain.Usuario;

public record ConsultarPedidoPorClienteContext(
        Usuario usuarioLogado
) {
    public boolean isUsuarioAutenticado() {
        return usuarioLogado != null && usuarioLogado.getId() != null;
    }
}
