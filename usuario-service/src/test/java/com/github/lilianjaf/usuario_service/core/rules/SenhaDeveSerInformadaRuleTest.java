package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.exception.SenhaObrigatoriaNaoInformadaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SenhaDeveSerInformadaRuleTest {

    private final SenhaDeveSerInformadaRule rule = new SenhaDeveSerInformadaRule();

    private CriacaoUsuarioContext contexto(String senha) {
        return new CriacaoUsuarioContext("nome", "email", "login", senha, "tipo",
                TipoNativo.CLIENTE, () -> false, () -> false, null);
    }

    @Test
    @DisplayName("Deve passar quando senha é informada")
    void devePassarQuandoSenhaInformada() {
        assertDoesNotThrow(() -> rule.validar(contexto("senha123")));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é nula")
    void deveLancarExcecaoQuandoSenhaNula() {
        assertThrows(SenhaObrigatoriaNaoInformadaException.class, () -> rule.validar(contexto(null)));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é vazia")
    void deveLancarExcecaoQuandoSenhaVazia() {
        assertThrows(SenhaObrigatoriaNaoInformadaException.class, () -> rule.validar(contexto("   ")));
    }
}
