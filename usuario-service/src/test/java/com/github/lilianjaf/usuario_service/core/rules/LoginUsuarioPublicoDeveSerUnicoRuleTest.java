package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.LoginUsuarioJaEmUsoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginUsuarioPublicoDeveSerUnicoRuleTest {

    private final LoginUsuarioPublicoDeveSerUnicoRule rule = new LoginUsuarioPublicoDeveSerUnicoRule();

    @Test
    @DisplayName("Deve permitir quando login nao existe")
    void devePermitirQuandoLoginNaoExiste() {
        CriacaoUsuarioPublicoContext context = criarContexto(() -> false);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando login ja existe")
    void deveLancarExcecaoQuandoLoginJaExiste() {
        CriacaoUsuarioPublicoContext context = criarContexto(() -> true);
        assertThrows(LoginUsuarioJaEmUsoException.class, () -> rule.validar(context));
    }

    private CriacaoUsuarioPublicoContext criarContexto(BooleanSupplier loginJaExiste) {
        return new CriacaoUsuarioPublicoContext(
                "Nome", "email@teste.com", "login", "senha", "Cliente",
                "Rua", "123", "Bairro", "Cidade", "00000-000", "UF",
                () -> false, loginJaExiste
        );
    }
}
