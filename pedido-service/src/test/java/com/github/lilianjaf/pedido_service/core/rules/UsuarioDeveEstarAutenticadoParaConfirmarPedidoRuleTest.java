package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioDeveEstarAutenticadoParaConfirmarPedidoRuleTest {

    @InjectMocks
    private UsuarioDeveEstarAutenticadoParaConfirmarPedidoRule rule;

    @Mock
    private ConfirmarPedidoContext context;

    @Test
    @DisplayName("Deve validar com sucesso quando o usuário está autenticado")
    void deveValidarComSucessoQuandoAutenticado() {
        when(context.isUsuarioAutenticado()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não está autenticado")
    void deveLancarExcecaoQuandoNaoAutenticado() {
        when(context.isUsuarioAutenticado()).thenReturn(false);
        assertThrows(UsuarioNaoAutenticadoException.class, () -> rule.validar(context));
    }
}
