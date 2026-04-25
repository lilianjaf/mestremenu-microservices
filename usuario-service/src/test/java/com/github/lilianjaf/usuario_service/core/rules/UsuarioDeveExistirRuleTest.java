package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UsuarioDeveExistirRuleTest {

    @Mock
    private UsuarioBase usuarioSendoEditado;

    private final UsuarioDeveExistirRule rule = new UsuarioDeveExistirRule();

    @Test
    @DisplayName("Deve permitir quando usuario sendo editado existe")
    void devePermitirQuandoUsuarioExiste() {
        AtualizacaoUsuarioContext context = new AtualizacaoUsuarioContext(usuarioSendoEditado, null, null);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario sendo editado eh nulo")
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        AtualizacaoUsuarioContext context = new AtualizacaoUsuarioContext(null, null, null);
        assertThrows(UsuarioNaoEncontradoException.class, () -> rule.validar(context));
    }
}
