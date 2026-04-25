package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.AcessoNegadoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidarPermissaoDonoRuleTest {

    @Mock
    private UsuarioBase usuarioLogado;

    private final ValidarPermissaoDonoRule rule = new ValidarPermissaoDonoRule();

    @Test
    @DisplayName("Deve validar com sucesso quando usuario logado eh dono")
    void deveValidarComSucessoQuandoEhDono() {
        Dono donoLogado = mock(Dono.class);
        when(donoLogado.isDono()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(donoLogado));
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario logado nao eh dono")
    void deveLancarExcecaoQuandoNaoEhDono() {
        assertThrows(AcessoNegadoException.class, () -> rule.validar(usuarioLogado));
    }
}
