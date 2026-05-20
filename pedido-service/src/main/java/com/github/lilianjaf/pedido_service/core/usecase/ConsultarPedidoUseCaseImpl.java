package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import com.github.lilianjaf.pedido_service.core.exception.PedidoNaoEncontradoException;
import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import com.github.lilianjaf.pedido_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.pedido_service.core.rules.ConsultarPedidoContext;
import com.github.lilianjaf.pedido_service.core.rules.ConsultarPedidoRule;

import java.util.List;
import java.util.UUID;

public class ConsultarPedidoUseCaseImpl implements ConsultarPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<ConsultarPedidoRule> permissaoRules;
    private final List<ConsultarPedidoRule> businessRules;

    public ConsultarPedidoUseCaseImpl(PedidoRepository pedidoRepository,
                                      ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                      TransactionGateway transactionGateway,
                                      List<ConsultarPedidoRule> permissaoRules,
                                      List<ConsultarPedidoRule> businessRules) {
        this.pedidoRepository = pedidoRepository;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
        this.businessRules = businessRules;
    }

    @Override
    public Pedido executar(UUID pedidoId) {
        Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioNaoAutenticadoException("Usuário não autenticado."));

        return transactionGateway.execute(() -> {
            Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                    .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado: " + pedidoId));
            ConsultarPedidoContext context = new ConsultarPedidoContext(usuarioLogado, pedido);
            permissaoRules.forEach(rule -> rule.validar(context));
            businessRules.forEach(rule -> rule.validar(context));
            return pedido;
        });
    }
}
