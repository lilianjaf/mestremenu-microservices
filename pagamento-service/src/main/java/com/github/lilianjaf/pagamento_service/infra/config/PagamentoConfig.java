package com.github.lilianjaf.pagamento_service.infra.config;

import com.github.lilianjaf.pagamento_service.core.gateway.OutboxGateway;
import com.github.lilianjaf.pagamento_service.core.gateway.PagamentoRepository;
import com.github.lilianjaf.pagamento_service.core.gateway.ProcessadorPagamentoGateway;
import com.github.lilianjaf.pagamento_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.pagamento_service.core.usecase.ProcessarPagamentoUseCase;
import com.github.lilianjaf.pagamento_service.core.usecase.ProcessarPagamentoUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoConfig {

    @Bean
    public ProcessarPagamentoUseCase processarPagamentoUseCase(
            PagamentoRepository pagamentoRepository,
            ProcessadorPagamentoGateway processadorPagamentoGateway,
            OutboxGateway outboxGateway,
            TransactionGateway transactionGateway) {
        return new ProcessarPagamentoUseCaseImpl(
                pagamentoRepository, processadorPagamentoGateway, outboxGateway, transactionGateway);
    }
}
