package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ConsultaUsuarioDeveExistirRuleTest {

    private final ConsultaUsuarioDeveExistirRule rule = new ConsultaUsuarioDeveExistirRule();

    @Test
    @DisplayName("Deve passar quando usuário buscado existe")
    void devePassarQuandoUsuarioExiste() {
        ConsultaUsuarioContext context = new ConsultaUsuarioContext(mock(UsuarioBase.class), mock(UsuarioBase.class));
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário buscado é nulo")
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        ConsultaUsuarioContext context = new ConsultaUsuarioContext(mock(UsuarioBase.class), null);
        assertThrows(UsuarioNaoEncontradoException.class, () -> rule.validar(context));
    }
}
