package com.github.lilianjaf.pagamento_service.infra.gateway;

import com.github.lilianjaf.pagamento_service.core.gateway.ProcessadorPagamentoGateway;
import com.github.lilianjaf.pagamento_service.infra.client.ProcessadorPagamentoClient;
import com.github.lilianjaf.pagamento_service.infra.client.SolicitacaoPagamentoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
public class ProcessadorPagamentoGatewayImpl implements ProcessadorPagamentoGateway {

    private static final Logger log = LoggerFactory.getLogger(ProcessadorPagamentoGatewayImpl.class);

    private final ProcessadorPagamentoClient client;

    public ProcessadorPagamentoGatewayImpl(ProcessadorPagamentoClient client) {
        this.client = client;
    }

    @Override
    @CircuitBreaker(name = "procpag", fallbackMethod = "fallback")
    @Retry(name = "procpag")
    public boolean processar(UUID pedidoId, UUID clienteId, BigDecimal valorTotal) {
        long valorInt = valorTotal.setScale(0, RoundingMode.HALF_UP).longValue();
        client.processar(new SolicitacaoPagamentoDto(pedidoId.toString(), clienteId.toString(), valorInt));
        return true;
    }

    public boolean fallback(UUID pedidoId, UUID clienteId, BigDecimal valorTotal, Throwable t) {
        log.error("Sistema de pagamento offline ou indisponível para o pedido {} — tente novamente. Causa: {}", pedidoId, t.getMessage());
        return false;
    }
}
