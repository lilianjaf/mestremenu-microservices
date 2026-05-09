package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.exception.PedidoNaoEncontradoException;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.pedido_service.core.rules.AtualizarStatusPedidoContext;
import com.github.lilianjaf.pedido_service.core.rules.AtualizarStatusPedidoRule;

import java.util.List;
import java.util.UUID;

public class AtualizarStatusPedidoUseCaseImpl implements AtualizarStatusPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final TransactionGateway transactionGateway;
    private final List<AtualizarStatusPedidoRule> businessRules;

    public AtualizarStatusPedidoUseCaseImpl(PedidoRepository pedidoRepository,
                                            TransactionGateway transactionGateway,
                                            List<AtualizarStatusPedidoRule> businessRules) {
        this.pedidoRepository = pedidoRepository;
        this.transactionGateway = transactionGateway;
        this.businessRules = businessRules;
    }

    @Override
    public Pedido executar(UUID pedidoId, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado: " + pedidoId));

        AtualizarStatusPedidoContext context = new AtualizarStatusPedidoContext(pedido, novoStatus);
        businessRules.forEach(rule -> rule.validar(context));

        return transactionGateway.execute(() -> {
            pedido.atualizarStatus(novoStatus);
            return pedidoRepository.salvar(pedido);
        });
    }
}
