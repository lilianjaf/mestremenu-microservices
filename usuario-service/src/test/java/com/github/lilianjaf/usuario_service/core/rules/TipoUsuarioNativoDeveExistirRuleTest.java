package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.exception.TipoUsuarioInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TipoUsuarioNativoDeveExistirRuleTest {

    private final TipoUsuarioNativoDeveExistirRule rule = new TipoUsuarioNativoDeveExistirRule();

    @Test
    @DisplayName("Deve permitir quando tipo nativo existe")
    void devePermitirQuandoTipoNativoExiste() {
        CriacaoUsuarioContext context = criarContexto(TipoNativo.CLIENTE);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando tipo nativo eh nulo")
    void deveLancarExcecaoQuandoTipoNativoEhNulo() {
        CriacaoUsuarioContext context = criarContexto(null);
        assertThrows(TipoUsuarioInvalidoException.class, () -> rule.validar(context));
    }

    private CriacaoUsuarioContext criarContexto(TipoNativo tipoNativo) {
        return new CriacaoUsuarioContext(
                "Nome", "email@teste.com", "login", "senha", "Cliente",
                tipoNativo, () -> false, () -> false, null
        );
    }
}
