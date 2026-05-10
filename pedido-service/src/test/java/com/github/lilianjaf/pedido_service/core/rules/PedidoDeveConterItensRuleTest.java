package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.DomainException;
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
class PedidoDeveConterItensRuleTest {

    @InjectMocks
    private PedidoDeveConterItensRule rule;

    @Mock
    private CriarPedidoContext context;

    @Test
    @DisplayName("Deve validar com sucesso quando o pedido contém itens")
    void deveValidarComSucessoQuandoPedidoContemItens() {
        when(context.isItensPedidoVazio()).thenReturn(false);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pedido não contém itens")
    void deveLancarExcecaoQuandoPedidoSemItens() {
        when(context.isItensPedidoVazio()).thenReturn(true);
        assertThrows(DomainException.class, () -> rule.validar(context));
    }
}
