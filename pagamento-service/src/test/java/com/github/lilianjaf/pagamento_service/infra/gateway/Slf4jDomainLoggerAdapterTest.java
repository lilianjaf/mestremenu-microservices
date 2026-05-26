package com.github.lilianjaf.pagamento_service.infra.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class Slf4jDomainLoggerAdapterTest {

    private final Slf4jDomainLoggerAdapter logger =
            new Slf4jDomainLoggerAdapter(Slf4jDomainLoggerAdapterTest.class);

    @Test
    @DisplayName("info deve logar sem lançar exceção")
    void infoNaoDeveLancarExcecao() {
        assertDoesNotThrow(() -> logger.info("Mensagem info: {}", "valor"));
    }

    @Test
    @DisplayName("warn deve logar sem lançar exceção")
    void warnNaoDeveLancarExcecao() {
        assertDoesNotThrow(() -> logger.warn("Mensagem warn: {}", "valor"));
    }

    @Test
    @DisplayName("error com params deve logar sem lançar exceção")
    void errorComParamsNaoDeveLancarExcecao() {
        assertDoesNotThrow(() -> logger.error("Mensagem error: {}", "valor"));
    }

    @Test
    @DisplayName("error com causa deve logar sem lançar exceção")
    void errorComCausaNaoDeveLancarExcecao() {
        assertDoesNotThrow(() -> logger.error("Mensagem error com causa", new RuntimeException("causa")));
    }
}
