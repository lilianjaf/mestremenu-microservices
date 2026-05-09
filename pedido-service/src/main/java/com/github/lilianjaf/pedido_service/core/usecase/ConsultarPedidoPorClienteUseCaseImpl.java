package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import com.github.lilianjaf.pedido_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.rules.ConsultarPedidoPorClienteContext;
import com.github.lilianjaf.pedido_service.core.rules.ConsultarPedidoPorClienteRule;

import java.util.List;

public class ConsultarPedidoPorClienteUseCaseImpl implements ConsultarPedidoPorClienteUseCase {

    private final PedidoRepository pedidoRepository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final List<ConsultarPedidoPorClienteRule> permissaoRules;
    private final List<ConsultarPedidoPorClienteRule> businessRules;

    public ConsultarPedidoPorClienteUseCaseImpl(PedidoRepository pedidoRepository,
                                                ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                List<ConsultarPedidoPorClienteRule> permissaoRules,
                                                List<ConsultarPedidoPorClienteRule> businessRules) {
        this.pedidoRepository = pedidoRepository;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.permissaoRules = permissaoRules;
        this.businessRules = businessRules;
    }

    @Override
    public List<Pedido> executar() {
        Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioNaoAutenticadoException("Usuário não autenticado."));

        ConsultarPedidoPorClienteContext context = new ConsultarPedidoPorClienteContext(usuarioLogado);

        permissaoRules.forEach(rule -> rule.validar(context));
        businessRules.forEach(rule -> rule.validar(context));

        return pedidoRepository.buscarPorClienteId(usuarioLogado.getId());
    }
}
