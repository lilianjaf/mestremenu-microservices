package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoPedido;
import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import com.github.lilianjaf.pedido_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.pedido_service.core.rules.CriarPedidoContext;
import com.github.lilianjaf.pedido_service.core.rules.CriarPedidoRule;

import java.util.List;
import java.util.stream.Collectors;

public class CriarPedidoUseCaseImpl implements CriarPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<CriarPedidoRule> permissaoRules;
    private final List<CriarPedidoRule> businessRules;

    public CriarPedidoUseCaseImpl(PedidoRepository pedidoRepository,
                                  ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                  TransactionGateway transactionGateway,
                                  List<CriarPedidoRule> permissaoRules,
                                  List<CriarPedidoRule> businessRules) {
        this.pedidoRepository = pedidoRepository;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
        this.businessRules = businessRules;
    }

    @Override
    public Pedido executar(DadosCriacaoPedido dados) {
        Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioNaoAutenticadoException("Usuário não autenticado."));

        List<ItemPedido> itens = dados.itens().stream()
                .map(i -> new ItemPedido(i.descricao(), i.quantidade(), i.preco()))
                .collect(Collectors.toList());

        CriarPedidoContext context = new CriarPedidoContext(usuarioLogado, dados.restauranteId(), itens);

        permissaoRules.forEach(rule -> rule.validar(context));
        businessRules.forEach(rule -> rule.validar(context));

        return transactionGateway.execute(() -> {
            Pedido pedido = new Pedido(usuarioLogado.getId(), dados.restauranteId(), itens);
            return pedidoRepository.salvar(pedido);
        });
    }
}
