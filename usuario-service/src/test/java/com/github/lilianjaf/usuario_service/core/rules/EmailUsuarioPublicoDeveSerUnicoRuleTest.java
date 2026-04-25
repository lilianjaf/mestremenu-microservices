package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.EmailUsuarioJaEmUsoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailUsuarioPublicoDeveSerUnicoRuleTest {

    private final EmailUsuarioPublicoDeveSerUnicoRule rule = new EmailUsuarioPublicoDeveSerUnicoRule();

    @Test
    @DisplayName("Deve permitir quando email nao existe")
    void devePermitirQuandoEmailNaoExiste() {
        CriacaoUsuarioPublicoContext context = criarContexto(() -> false);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando email ja existe")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        CriacaoUsuarioPublicoContext context = criarContexto(() -> true);
        assertThrows(EmailUsuarioJaEmUsoException.class, () -> rule.validar(context));
    }

    private CriacaoUsuarioPublicoContext criarContexto(BooleanSupplier emailJaExiste) {
        return new CriacaoUsuarioPublicoContext(
                "Nome", "email@teste.com", "login", "senha", "Cliente",
                "Rua", "123", "Bairro", "Cidade", "00000-000", "UF",
                emailJaExiste, () -> false
        );
    }
}
