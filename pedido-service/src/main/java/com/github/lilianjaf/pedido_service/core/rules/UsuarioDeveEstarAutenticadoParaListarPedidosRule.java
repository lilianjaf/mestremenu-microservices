package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;

public class UsuarioDeveEstarAutenticadoParaListarPedidosRule implements ConsultarPedidoPorClienteRule {

    @Override
    public void validar(ConsultarPedidoPorClienteContext context) {
        if (!context.isUsuarioAutenticado()) {
            throw new UsuarioNaoAutenticadoException("É necessário estar autenticado para listar pedidos.");
        }
    }
}
