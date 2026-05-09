package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoPedido;

public interface CriarPedidoUseCase {
    Pedido executar(DadosCriacaoPedido dados);
}
