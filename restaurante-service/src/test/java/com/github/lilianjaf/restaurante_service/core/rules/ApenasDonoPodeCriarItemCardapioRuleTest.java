package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.TipoNativo;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ApenasDonoPodeCriarItemCardapioRuleTest {

    @InjectMocks
    private ApenasDonoPodeCriarItemCardapioRule rule;

    @Mock
    private Restaurante restaurante;

    @Test
    @DisplayName("Deve validar quando o usuário é do tipo DONO")
    void deveValidarQuandoUsuarioEDono() {
        Usuario dono = new Usuario(UUID.randomUUID(), TipoNativo.DONO);
        DadosCriacaoItemCardapio dados = new DadosCriacaoItemCardapio("Burger", "Desc", BigDecimal.TEN, true, "foto.jpg", UUID.randomUUID());
        CriacaoItemCardapioContext context = new CriacaoItemCardapioContext(dono, restaurante, dados);

        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário é do tipo CLIENTE")
    void deveLancarExcecaoQuandoUsuarioECliente() {
        Usuario cliente = new Usuario(UUID.randomUUID(), TipoNativo.CLIENTE);
        DadosCriacaoItemCardapio dados = new DadosCriacaoItemCardapio("Burger", "Desc", BigDecimal.TEN, true, "foto.jpg", UUID.randomUUID());
        CriacaoItemCardapioContext context = new CriacaoItemCardapioContext(cliente, restaurante, dados);

        assertThrows(CardapioException.class, () -> rule.validar(context));
    }
}
