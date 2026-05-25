package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.gateway.CardapioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NomeCardapioNaoPodeSerDuplicadoRuleTest {

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private Restaurante restaurante;

    @Mock
    private Usuario usuario;

    private NomeCardapioNaoPodeSerDuplicadoRule rule;

    @BeforeEach
    void setUp() {
        rule = new NomeCardapioNaoPodeSerDuplicadoRule(cardapioRepository);
    }

    @Test
    @DisplayName("Deve validar criação quando nome é único no restaurante")
    void deveValidarCriacaoQuandoNomeUnico() {
        UUID idRestaurante = UUID.randomUUID();
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu Único", idRestaurante, Collections.emptyList());
        CriacaoCardapioContext context = new CriacaoCardapioContext(usuario, restaurante, dados);

        when(cardapioRepository.existeNomeParaRestaurante("Menu Único", idRestaurante)).thenReturn(false);

        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção na criação quando nome já existe no restaurante")
    void deveLancarExcecaoNaCriacaoQuandoNomeDuplicado() {
        UUID idRestaurante = UUID.randomUUID();
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu Existente", idRestaurante, Collections.emptyList());
        CriacaoCardapioContext context = new CriacaoCardapioContext(usuario, restaurante, dados);

        when(cardapioRepository.existeNomeParaRestaurante("Menu Existente", idRestaurante)).thenReturn(true);

        assertThrows(CardapioException.class, () -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve validar atualização quando o nome não mudou")
    void deveValidarAtualizacaoQuandoNomeNaoMudou() {
        UUID idRestaurante = UUID.randomUUID();
        Cardapio cardapioExistente = new Cardapio("Menu Atual", idRestaurante, Collections.emptyList());
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(cardapioExistente.getId(), "Menu Atual", null);
        AtualizacaoCardapioContext context = new AtualizacaoCardapioContext(usuario, restaurante, cardapioExistente, dados);

        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve validar atualização quando o nome mudou e é único")
    void deveValidarAtualizacaoQuandoNomeMudouEEUnico() {
        UUID idRestaurante = UUID.randomUUID();
        Cardapio cardapioExistente = new Cardapio("Menu Atual", idRestaurante, Collections.emptyList());
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(cardapioExistente.getId(), "Novo Nome", null);
        AtualizacaoCardapioContext context = new AtualizacaoCardapioContext(usuario, restaurante, cardapioExistente, dados);

        when(cardapioRepository.existeNomeParaRestaurante("Novo Nome", idRestaurante)).thenReturn(false);

        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção na atualização quando o nome mudou e já existe")
    void deveLancarExcecaoNaAtualizacaoQuandoNomeMudouEJaExiste() {
        UUID idRestaurante = UUID.randomUUID();
        Cardapio cardapioExistente = new Cardapio("Menu Atual", idRestaurante, Collections.emptyList());
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(cardapioExistente.getId(), "Menu Duplicado", null);
        AtualizacaoCardapioContext context = new AtualizacaoCardapioContext(usuario, restaurante, cardapioExistente, dados);

        when(cardapioRepository.existeNomeParaRestaurante("Menu Duplicado", idRestaurante)).thenReturn(true);

        assertThrows(CardapioException.class, () -> rule.validar(context));
    }
}
