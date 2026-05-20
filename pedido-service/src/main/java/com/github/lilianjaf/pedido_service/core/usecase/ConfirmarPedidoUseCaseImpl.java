package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import com.github.lilianjaf.pedido_service.core.exception.PedidoNaoEncontradoException;
import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import com.github.lilianjaf.pedido_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.pedido_service.core.gateway.OutboxGateway;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.pedido_service.core.rules.ConfirmarPedidoContext;
import com.github.lilianjaf.pedido_service.core.rules.ConfirmarPedidoRule;

import java.util.List;
import java.util.UUID;

public class ConfirmarPedidoUseCaseImpl implements ConfirmarPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final OutboxGateway outboxGateway;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<ConfirmarPedidoRule> permissaoRules;
    private final List<ConfirmarPedidoRule> businessRules;

    public ConfirmarPedidoUseCaseImpl(PedidoRepository pedidoRepository,
                                      OutboxGateway outboxGateway,
                                      ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                      TransactionGateway transactionGateway,
                                      List<ConfirmarPedidoRule> permissaoRules,
                                      List<ConfirmarPedidoRule> businessRules) {
        this.pedidoRepository = pedidoRepository;
        this.outboxGateway = outboxGateway;
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

            ConfirmarPedidoContext context = new ConfirmarPedidoContext(usuarioLogado, pedido);
            permissaoRules.forEach(rule -> rule.validar(context));
            businessRules.forEach(rule -> rule.validar(context));

            pedido.confirmar();
            Pedido pedidoSalvo = pedidoRepository.salvar(pedido);
            outboxGateway.salvarEventoPedidoCriado(pedidoSalvo);
            return pedidoSalvo;
        });
    }
}
