package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioDeveEstarAutenticadoParaListarPedidosRuleTest {

    @InjectMocks
    private UsuarioDeveEstarAutenticadoParaListarPedidosRule rule;

    @Mock
    private ConsultarPedidoPorClienteContext context;

    @Test
    @DisplayName("Deve validar com sucesso quando usuário autenticado")
    void deveValidarQuandoAutenticado() {
        when(context.isUsuarioAutenticado()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não autenticado")
    void deveLancarExcecaoQuandoNaoAutenticado() {
        when(context.isUsuarioAutenticado()).thenReturn(false);
        assertThrows(UsuarioNaoAutenticadoException.class, () -> rule.validar(context));
    }
}
