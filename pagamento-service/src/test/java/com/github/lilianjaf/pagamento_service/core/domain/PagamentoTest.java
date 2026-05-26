package com.github.lilianjaf.pagamento_service.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoTest {

    private Pagamento novoPagamento() {
        return new Pagamento(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("Novo pagamento deve iniciar em AGUARDANDO com zero tentativas")
    void novoPagamentoDeveIniciarEmAguardando() {
        Pagamento pagamento = novoPagamento();

        assertEquals(StatusPagamento.AGUARDANDO, pagamento.getStatus());
        assertEquals(0, pagamento.getTentativas());
        assertNotNull(pagamento.getId());
        assertNull(pagamento.getProcessadoEm());
    }

    @Test
    @DisplayName("aprovar() deve mudar status para APROVADO e registrar data de processamento")
    void aprovarDeveMudarStatusParaAprovado() {
        Pagamento pagamento = novoPagamento();
        pagamento.aprovar();

        assertEquals(StatusPagamento.APROVADO, pagamento.getStatus());
        assertNotNull(pagamento.getProcessadoEm());
    }

    @Test
    @DisplayName("marcarPendente() deve mudar status para PENDENTE")
    void marcarPendenteDeveMudarStatus() {
        Pagamento pagamento = novoPagamento();
        pagamento.marcarPendente();

        assertEquals(StatusPagamento.PENDENTE, pagamento.getStatus());
    }

    @Test
    @DisplayName("incrementarTentativa() deve incrementar o contador a cada chamada")
    void incrementarTentativaDeveIncrementarContador() {
        Pagamento pagamento = novoPagamento();

        pagamento.incrementarTentativa();
        assertEquals(1, pagamento.getTentativas());

        pagamento.incrementarTentativa();
        assertEquals(2, pagamento.getTentativas());
    }

    @Test
    @DisplayName("falhar() deve mudar status para FALHOU e registrar data de processamento")
    void falharDeveMudarStatusParaFalhou() {
        Pagamento pagamento = novoPagamento();
        pagamento.falhar();

        assertEquals(StatusPagamento.FALHOU, pagamento.getStatus());
        assertNotNull(pagamento.getProcessadoEm());
    }
}
