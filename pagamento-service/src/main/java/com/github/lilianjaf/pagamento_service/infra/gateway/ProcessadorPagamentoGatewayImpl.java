package com.github.lilianjaf.pagamento_service.infra.gateway;

import com.github.lilianjaf.pagamento_service.core.gateway.ProcessadorPagamentoGateway;
import com.github.lilianjaf.pagamento_service.infra.client.ProcessadorPagamentoClient;
import com.github.lilianjaf.pagamento_service.infra.client.SolicitacaoPagamentoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class ProcessadorPagamentoGatewayImpl implements ProcessadorPagamentoGateway {

    private static final Logger log = LoggerFactory.getLogger(ProcessadorPagamentoGatewayImpl.class);

    private final ProcessadorPagamentoClient client;

    public ProcessadorPagamentoGatewayImpl(ProcessadorPagamentoClient client) {
        this.client = client;
    }

    @CircuitBreaker(name = "procpag", fallbackMethod = "fallbackAsync")
    @Retry(name = "procpag")
    @TimeLimiter(name = "procpag")
    public CompletableFuture<Boolean> processarAsync(UUID pedidoId, BigDecimal valorTotal) {
        return CompletableFuture.supplyAsync(() -> {
            client.processar(new SolicitacaoPagamentoDto(pedidoId.toString(), valorTotal));
            return true;
        });
    }

    public CompletableFuture<Boolean> fallbackAsync(UUID pedidoId, BigDecimal valorTotal, Throwable t) {
        log.warn("Fallback ativado para pedido {} — causa: {}", pedidoId, t.getMessage());
        return CompletableFuture.completedFuture(false);
    }

    @Override
    public boolean processar(UUID pedidoId, BigDecimal valorTotal) {
        try {
            return processarAsync(pedidoId, valorTotal).get();
        } catch (Exception e) {
            log.error("Erro irrecuperável ao processar pagamento do pedido {}", pedidoId, e);
            return false;
        }
    }
}
