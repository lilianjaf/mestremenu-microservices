package com.github.lilianjaf.pagamento_service.infra.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SolicitacaoPagamentoDto(
        @JsonProperty("pagamento_id") String pagamentoId,
        @JsonProperty("cliente_id")   String clienteId,
        @JsonProperty("valor")        long valor
) {}
