package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Cliente;
import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.Restaurante;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioPossuiRestauranteAtivoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioSemRestauranteVinculadoRuleTest {

    @Mock
    private Cliente cliente;

    @Mock
    private Dono dono;

    private final UsuarioSemRestauranteVinculadoRule rule = new UsuarioSemRestauranteVinculadoRule();

    @Test
    @DisplayName("Deve permitir quando usuario alvo eh cliente")
    void devePermitirQuandoUsuarioAlvoEhCliente() {
        InativacaoUsuarioContext context = new InativacaoUsuarioContext(null, cliente);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve permitir quando usuario alvo eh dono mas nao tem restaurantes")
    void devePermitirQuandoDonoNaoTemRestaurantes() {
        when(dono.isDono()).thenReturn(true);
        when(dono.getRestaurantes()).thenReturn(Collections.emptyList());
        InativacaoUsuarioContext context = new InativacaoUsuarioContext(null, dono);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario alvo eh dono e tem restaurantes")
    void deveLancarExcecaoQuandoDonoTemRestaurantes() {
        when(dono.isDono()).thenReturn(true);
        when(dono.getRestaurantes()).thenReturn(List.of(mock(Restaurante.class)));
        InativacaoUsuarioContext context = new InativacaoUsuarioContext(null, dono);
        assertThrows(UsuarioPossuiRestauranteAtivoException.class, () -> rule.validar(context));
    }
}
