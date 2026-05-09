package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;

import java.util.List;

public interface ConsultarPedidoPorClienteUseCase {
    List<Pedido> executar();
}
