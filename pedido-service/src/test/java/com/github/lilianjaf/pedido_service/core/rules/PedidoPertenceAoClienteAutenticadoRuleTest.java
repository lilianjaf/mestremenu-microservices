package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.AcessoNegadoAoPedidoException;
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
class PedidoPertenceAoClienteAutenticadoRuleTest {

    @InjectMocks
    private PedidoPertenceAoClienteAutenticadoRule rule;

    @Mock
    private ConfirmarPedidoContext context;

    @Test
    @DisplayName("Deve validar com sucesso quando o pedido pertence ao cliente autenticado")
    void deveValidarComSucessoQuandoPedidoDoCliente() {
        when(context.isPedidoDoCliente()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pedido não pertence ao cliente autenticado")
    void deveLancarExcecaoQuandoPedidoNaoPertenceAoCliente() {
        when(context.isPedidoDoCliente()).thenReturn(false);
        assertThrows(AcessoNegadoAoPedidoException.class, () -> rule.validar(context));
    }
}
