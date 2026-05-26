package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.TipoUsuarioNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class TipoUsuarioDeveExistirRuleTest {

    private final TipoUsuarioDeveExistirRule rule = new TipoUsuarioDeveExistirRule();

    @Test
    @DisplayName("Deve passar quando tipo de usuário existe")
    void devePassarQuandoTipoExiste() {
        TipoUsuario tipo = new TipoUsuario("GERENTE", TipoNativo.CLIENTE);
        ExclusaoTipoUsuarioContext context = new ExclusaoTipoUsuarioContext(
                Optional.of(tipo), () -> false, mock(UsuarioBase.class));
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não existe")
    void deveLancarExcecaoQuandoTipoNaoExiste() {
        ExclusaoTipoUsuarioContext context = new ExclusaoTipoUsuarioContext(
                Optional.empty(), () -> false, mock(UsuarioBase.class));
        assertThrows(TipoUsuarioNaoEncontradoException.class, () -> rule.validar(context));
    }
}
