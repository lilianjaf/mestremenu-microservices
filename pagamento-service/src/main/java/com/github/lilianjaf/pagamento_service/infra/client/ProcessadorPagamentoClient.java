package com.github.lilianjaf.pagamento_service.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "procpag", url = "${pagamento.externo.url}")
public interface ProcessadorPagamentoClient {

    @PostMapping("/requisicao")
    RespostaPagamentoDto processar(@RequestBody SolicitacaoPagamentoDto solicitacao);
}
