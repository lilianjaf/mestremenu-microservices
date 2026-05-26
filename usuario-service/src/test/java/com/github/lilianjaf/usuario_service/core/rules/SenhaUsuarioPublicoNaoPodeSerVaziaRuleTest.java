package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.SenhaUsuarioNaoPodeSerVaziaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SenhaUsuarioPublicoNaoPodeSerVaziaRuleTest {

    private final SenhaUsuarioPublicoNaoPodeSerVaziaRule rule = new SenhaUsuarioPublicoNaoPodeSerVaziaRule();

    private CriacaoUsuarioPublicoContext contexto(String senha) {
        return new CriacaoUsuarioPublicoContext("nome", "e@mail.com", "login", senha, "cliente",
                "Rua A", "1", "", "Bairro", "Cidade", "SP", () -> false, () -> false);
    }

    @Test
    @DisplayName("Deve passar quando senha é informada")
    void devePassarQuandoSenhaInformada() {
        assertDoesNotThrow(() -> rule.validar(contexto("senha123")));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é nula")
    void deveLancarExcecaoQuandoSenhaNula() {
        assertThrows(SenhaUsuarioNaoPodeSerVaziaException.class, () -> rule.validar(contexto(null)));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha é vazia")
    void deveLancarExcecaoQuandoSenhaVazia() {
        assertThrows(SenhaUsuarioNaoPodeSerVaziaException.class, () -> rule.validar(contexto("")));
    }
}
