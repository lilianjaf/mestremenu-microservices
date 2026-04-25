package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.exception.CredenciaisJaEmUsoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailELoginDevemSerUnicosRuleTest {

    private final EmailELoginDevemSerUnicosRule rule = new EmailELoginDevemSerUnicosRule();

    @Test
    @DisplayName("Deve permitir quando email e login nao existem")
    void devePermitirQuandoEmailELoginNaoExistem() {
        CriacaoUsuarioContext context = criarContexto(() -> false, () -> false);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando email ja existe")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        CriacaoUsuarioContext context = criarContexto(() -> true, () -> false);
        assertThrows(CredenciaisJaEmUsoException.class, () -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando login ja existe")
    void deveLancarExcecaoQuandoLoginJaExiste() {
        CriacaoUsuarioContext context = criarContexto(() -> false, () -> true);
        assertThrows(CredenciaisJaEmUsoException.class, () -> rule.validar(context));
    }

    private CriacaoUsuarioContext criarContexto(BooleanSupplier emailJaExiste, BooleanSupplier loginJaExiste) {
        return new CriacaoUsuarioContext(
                "Nome", "email@teste.com", "login", "senha", "Cliente",
                TipoNativo.CLIENTE, emailJaExiste, loginJaExiste, null
        );
    }
}
