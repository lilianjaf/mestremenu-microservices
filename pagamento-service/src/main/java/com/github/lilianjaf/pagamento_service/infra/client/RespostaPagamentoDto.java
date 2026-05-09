package com.github.lilianjaf.pagamento_service.infra.client;

public record RespostaPagamentoDto(
        String status,
        String mensagem
) {}
