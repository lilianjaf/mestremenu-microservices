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
class PedidoDeveEstarEmRascunhoRuleTest {

    @InjectMocks
    private PedidoDeveEstarEmRascunhoRule rule;

    @Mock
    private ConfirmarPedidoContext context;

    @Test
    @DisplayName("Deve validar com sucesso quando o pedido está em rascunho")
    void deveValidarComSucessoQuandoPedidoEmRascunho() {
        when(context.isPedidoEmRascunho()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pedido não está em rascunho")
    void deveLancarExcecaoQuandoPedidoNaoEstaEmRascunho() {
        when(context.isPedidoEmRascunho()).thenReturn(false);
        assertThrows(DomainException.class, () -> rule.validar(context));
    }
}
