package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.InativacaoUsuarioNaoAutorizadaException;
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
class ApenasDonoOuProprioUsuarioPodeInativarRuleTest {

    @Mock
    private UsuarioBase usuarioLogado;

    @Mock
    private UsuarioBase usuarioAlvo;

    private final ApenasDonoOuProprioUsuarioPodeInativarRule rule = new ApenasDonoOuProprioUsuarioPodeInativarRule();

    @Test
    @DisplayName("Deve permitir inativacao quando usuario logado eh dono")
    void devePermitirInativacaoQuandoUsuarioLogadoEhDono() {
        Dono donoLogado = mock(Dono.class);
        InativacaoUsuarioContext context = new InativacaoUsuarioContext(donoLogado, usuarioAlvo);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve permitir inativacao quando usuario logado eh o proprio alvo")
    void devePermitirInativacaoQuandoEhOMesmoUsuario() {
        UUID id = UUID.randomUUID();
        when(usuarioLogado.getId()).thenReturn(id);
        when(usuarioAlvo.getId()).thenReturn(id);
        InativacaoUsuarioContext context = new InativacaoUsuarioContext(usuarioLogado, usuarioAlvo);

        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando nao tem permissao para inativar")
    void deveLancarExcecaoQuandoNaoTemPermissao() {
        when(usuarioLogado.getId()).thenReturn(UUID.randomUUID());
        when(usuarioAlvo.getId()).thenReturn(UUID.randomUUID());
        InativacaoUsuarioContext context = new InativacaoUsuarioContext(usuarioLogado, usuarioAlvo);

        assertThrows(InativacaoUsuarioNaoAutorizadaException.class, () -> rule.validar(context));
    }
}
