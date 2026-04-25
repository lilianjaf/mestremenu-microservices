package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.EdicaoUsuarioNaoAutorizadaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApenasDonoOuProprioUsuarioPodeEditarRuleTest {

    @Mock
    private UsuarioBase usuarioLogado;

    @Mock
    private UsuarioBase usuarioSendoEditado;

    private final ApenasDonoOuProprioUsuarioPodeEditarRule rule = new ApenasDonoOuProprioUsuarioPodeEditarRule();

    @Test
    @DisplayName("Deve permitir edicao quando o usuario logado eh dono")
    void devePermitirEdicaoQuandoUsuarioLogadoEhDono() {
        Dono donoLogado = mock(Dono.class);
        AtualizacaoUsuarioContext context = new AtualizacaoUsuarioContext(usuarioSendoEditado, null, donoLogado);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve permitir edicao quando o usuario logado eh o proprio usuario sendo editado")
    void devePermitirEdicaoQuandoEhOProprioUsuario() {
        UUID idComum = UUID.randomUUID();
        when(usuarioLogado.getId()).thenReturn(idComum);
        when(usuarioSendoEditado.getId()).thenReturn(idComum);

        AtualizacaoUsuarioContext context = new AtualizacaoUsuarioContext(usuarioSendoEditado, null, usuarioLogado);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao eh dono nem o proprio editado")
    void deveLancarExcecaoQuandoNaoTemPermissao() {
        when(usuarioLogado.getId()).thenReturn(UUID.randomUUID());
        when(usuarioSendoEditado.getId()).thenReturn(UUID.randomUUID());

        AtualizacaoUsuarioContext context = new AtualizacaoUsuarioContext(usuarioSendoEditado, null, usuarioLogado);
        assertThrows(EdicaoUsuarioNaoAutorizadaException.class, () -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve permitir se o usuario sendo editado for nulo")
    void devePermitirSeUsuarioSendoEditadoForNulo() {
        AtualizacaoUsuarioContext context = new AtualizacaoUsuarioContext(null, null, usuarioLogado);
        assertDoesNotThrow(() -> rule.validar(context));
    }
}
