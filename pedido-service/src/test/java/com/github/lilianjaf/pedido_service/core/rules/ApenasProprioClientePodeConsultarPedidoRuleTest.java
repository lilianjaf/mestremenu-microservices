package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.AcessoNegadoAoPedidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApenasProprioClientePodeConsultarPedidoRuleTest {

    @InjectMocks
    private ApenasProprioClientePodeConsultarPedidoRule rule;

    @Mock
    private ConsultarPedidoContext context;

    @Test
    @DisplayName("Deve validar com sucesso quando pedido pertence ao cliente")
    void deveValidarQuandoPedidoDoCliente() {
        when(context.isPedidoDoCliente()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não pertence ao cliente")
    void deveLancarExcecaoQuandoPedidoNaoDoCliente() {
        when(context.isPedidoDoCliente()).thenReturn(false);
        assertThrows(AcessoNegadoAoPedidoException.class, () -> rule.validar(context));
    }
}
