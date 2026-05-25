package com.github.lilianjaf.restaurante_service.infra.controller;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.usecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CardapioController")
class CardapioControllerTest {

    @Mock
    private CriarCardapioUseCase criarUseCase;
    @Mock
    private AlterarCardapioUseCase editarUseCase;
    @Mock
    private DeletarCardapioUseCase deletarUseCase;
    @Mock
    private BuscarCardapioPorRestauranteUseCase buscarPorRestauranteUseCase;

    @InjectMocks
    private CardapioController controller;

    @Test
    @DisplayName("Deve criar cardápio e retornar 201 com ID")
    void deveCriarCardapioERetornar201() {
        UUID idRestaurante = UUID.randomUUID();
        CriarItemCardapioJson itemJson = new CriarItemCardapioJson("Burger", "Desc", BigDecimal.TEN, true, "foto.jpg", null);
        CriarCardapioJson json = new CriarCardapioJson("Menu Principal", idRestaurante, List.of(itemJson), null);

        Cardapio cardapio = new Cardapio("Menu Principal", idRestaurante, Collections.emptyList());
        when(criarUseCase.executar(any(DadosCriacaoCardapio.class))).thenReturn(cardapio);

        ResponseEntity<Map<String, UUID>> response = controller.criar(json);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cardapio.getId(), response.getBody().get("id"));
        verify(criarUseCase).executar(any(DadosCriacaoCardapio.class));
    }

    @Test
    @DisplayName("Deve editar cardápio e retornar 200 com o cardápio atualizado")
    void deveEditarCardapioERetornar200() {
        UUID id = UUID.randomUUID();
        UUID idRestaurante = UUID.randomUUID();
        AtualizarCardapioJson json = new AtualizarCardapioJson("Novo Nome", null);

        Cardapio cardapio = new Cardapio(id, "Novo Nome", idRestaurante, Collections.emptyList());
        when(editarUseCase.executar(any(DadosAtualizacaoCardapio.class))).thenReturn(cardapio);

        ResponseEntity<CardapioResponseJson> response = controller.editar(id, json);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Novo Nome", response.getBody().nome());
        assertEquals(id, response.getBody().id());
        verify(editarUseCase).executar(any(DadosAtualizacaoCardapio.class));
    }

    @Test
    @DisplayName("Deve deletar cardápio e retornar 204")
    void deveDeletarCardapioERetornar204() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response = controller.deletar(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deletarUseCase).executar(id);
    }

    @Test
    @DisplayName("Deve buscar cardápios por restaurante e retornar 200")
    void deveBuscarCardapiosPorRestauranteERetornar200() {
        UUID idRestaurante = UUID.randomUUID();
        Cardapio cardapio = new Cardapio("Menu", idRestaurante, Collections.emptyList());
        when(buscarPorRestauranteUseCase.executar(idRestaurante)).thenReturn(List.of(cardapio));

        ResponseEntity<List<CardapioResponseJson>> response = controller.buscarPorRestaurante(idRestaurante);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(idRestaurante, response.getBody().get(0).idRestaurante());
        verify(buscarPorRestauranteUseCase).executar(idRestaurante);
    }
}
