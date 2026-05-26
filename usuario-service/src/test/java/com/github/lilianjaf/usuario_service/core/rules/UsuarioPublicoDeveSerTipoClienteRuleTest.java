package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.TipoUsuarioInvalidoParaCadastroPublicoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioPublicoDeveSerTipoClienteRuleTest {

    private final UsuarioPublicoDeveSerTipoClienteRule rule = new UsuarioPublicoDeveSerTipoClienteRule();

    private CriacaoUsuarioPublicoContext contexto(String nomeTipo) {
        return new CriacaoUsuarioPublicoContext("nome", "e@mail.com", "login", "senha", nomeTipo,
                "Rua A", "1", "", "Bairro", "Cidade", "SP", () -> false, () -> false);
    }

    @Test
    @DisplayName("Deve passar quando tipo é 'cliente'")
    void devePassarQuandoTipoEhCliente() {
        assertDoesNotThrow(() -> rule.validar(contexto("cliente")));
    }

    @Test
    @DisplayName("Deve passar quando tipo é 'CLIENTE' (case insensitive)")
    void devePassarQuandoTipoEhClienteMaiusculo() {
        assertDoesNotThrow(() -> rule.validar(contexto("CLIENTE")));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo não é cliente")
    void deveLancarExcecaoQuandoTipoNaoEhCliente() {
        assertThrows(TipoUsuarioInvalidoParaCadastroPublicoException.class, () -> rule.validar(contexto("DONO")));
    }
}
