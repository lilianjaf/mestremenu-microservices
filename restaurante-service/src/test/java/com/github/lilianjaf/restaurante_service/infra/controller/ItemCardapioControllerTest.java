package com.github.lilianjaf.restaurante_service.infra.controller;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoItemCardapio;
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
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ItemCardapioController")
class ItemCardapioControllerTest {

    @Mock
    private CriarItemCardapioUseCase criarUseCase;
    @Mock
    private AlterarItemCardapioUseCase editarUseCase;
    @Mock
    private DeletarItemCardapioUseCase deletarUseCase;
    @Mock
    private BuscarItemCardapioPorIdUseCase buscarUseCase;

    @InjectMocks
    private ItemCardapioController controller;

    @Test
    @DisplayName("Deve criar item e retornar 201 com ID")
    void deveCriarItemERetornar201() {
        UUID idCardapio = UUID.randomUUID();
        CriarItemCardapioJson json = new CriarItemCardapioJson("Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", idCardapio);

        ItemCardapio item = new ItemCardapio("Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", idCardapio);
        when(criarUseCase.executar(any(DadosCriacaoItemCardapio.class))).thenReturn(item);

        ResponseEntity<Map<String, UUID>> response = controller.criar(json);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(item.getId(), response.getBody().get("id"));
        verify(criarUseCase).executar(any(DadosCriacaoItemCardapio.class));
    }

    @Test
    @DisplayName("Deve editar item e retornar 200")
    void deveEditarItemERetornar200() {
        UUID id = UUID.randomUUID();
        UUID idCardapio = UUID.randomUUID();
        AtualizarItemCardapioJson json = new AtualizarItemCardapioJson("Novo Burger", "Nova desc", BigDecimal.valueOf(30), true, "novo.jpg", null);

        ItemCardapio item = new ItemCardapio(id, "Novo Burger", "Nova desc", BigDecimal.valueOf(30), true, "novo.jpg", idCardapio);
        when(editarUseCase.executar(any(DadosAtualizacaoItemCardapio.class))).thenReturn(item);

        ResponseEntity<ItemCardapioResponseJson> response = controller.editar(id, json);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Novo Burger", response.getBody().nome());
        assertEquals(id, response.getBody().id());
        verify(editarUseCase).executar(any(DadosAtualizacaoItemCardapio.class));
    }

    @Test
    @DisplayName("Deve deletar item e retornar 204")
    void deveDeletarItemERetornar204() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response = controller.deletar(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deletarUseCase).executar(id);
    }

    @Test
    @DisplayName("Deve buscar item por ID e retornar 200")
    void deveBuscarItemPorIdERetornar200() {
        UUID id = UUID.randomUUID();
        UUID idCardapio = UUID.randomUUID();
        ItemCardapio item = new ItemCardapio(id, "Burger", "Desc", BigDecimal.valueOf(25), true, "foto.jpg", idCardapio);
        when(buscarUseCase.executar(id)).thenReturn(item);

        ResponseEntity<ItemCardapioResponseJson> response = controller.buscar(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Burger", response.getBody().nome());
        assertEquals(id, response.getBody().id());
        verify(buscarUseCase).executar(id);
    }
}
