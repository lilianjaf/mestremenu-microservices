package com.github.lilianjaf.pedido_service.infra.config;

import com.github.lilianjaf.pedido_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.pedido_service.core.gateway.OutboxGateway;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.pedido_service.core.rules.*;
import com.github.lilianjaf.pedido_service.core.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PedidoConfig {

    @Bean
    public CriarPedidoUseCase criarPedidoUseCase(PedidoRepository pedidoRepository,
                                                  ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                  TransactionGateway transactionGateway) {
        List<CriarPedidoRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoParaCriarPedidoRule()
        );
        List<CriarPedidoRule> businessRules = List.of(
                new PedidoDeveConterItensRule()
        );
        return new CriarPedidoUseCaseImpl(
                pedidoRepository, obterUsuarioLogadoGateway, transactionGateway, permissaoRules, businessRules);
    }

    @Bean
    public ConfirmarPedidoUseCase confirmarPedidoUseCase(PedidoRepository pedidoRepository,
                                                         OutboxGateway outboxGateway,
                                                         ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                         TransactionGateway transactionGateway) {
        List<ConfirmarPedidoRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoParaConfirmarPedidoRule(),
                new PedidoPertenceAoClienteAutenticadoRule()
        );
        List<ConfirmarPedidoRule> businessRules = List.of(
                new PedidoDeveEstarEmRascunhoRule()
        );
        return new ConfirmarPedidoUseCaseImpl(
                pedidoRepository, outboxGateway, obterUsuarioLogadoGateway, transactionGateway,
                permissaoRules, businessRules);
    }

    @Bean
    public ConsultarPedidoUseCase consultarPedidoUseCase(PedidoRepository pedidoRepository,
                                                         ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                         TransactionGateway transactionGateway) {
        List<ConsultarPedidoRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoParaConsultarPedidoRule(),
                new ApenasProprioClientePodeConsultarPedidoRule()
        );
        List<ConsultarPedidoRule> businessRules = List.of();
        return new ConsultarPedidoUseCaseImpl(
                pedidoRepository, obterUsuarioLogadoGateway, transactionGateway, permissaoRules, businessRules);
    }

    @Bean
    public ConsultarPedidoPorClienteUseCase consultarPedidoPorClienteUseCase(
            PedidoRepository pedidoRepository,
            ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
            TransactionGateway transactionGateway) {
        List<ConsultarPedidoPorClienteRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoParaListarPedidosRule()
        );
        List<ConsultarPedidoPorClienteRule> businessRules = List.of();
        return new ConsultarPedidoPorClienteUseCaseImpl(
                pedidoRepository, obterUsuarioLogadoGateway, transactionGateway, permissaoRules, businessRules);
    }

    @Bean
    public AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase(
            PedidoRepository pedidoRepository,
            TransactionGateway transactionGateway) {
        return new AtualizarStatusPedidoUseCaseImpl(pedidoRepository, transactionGateway, List.of());
    }
}
