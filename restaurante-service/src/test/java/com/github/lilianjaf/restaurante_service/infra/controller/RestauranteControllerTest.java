package com.github.lilianjaf.restaurante_service.infra.controller;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Endereco;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.dto.DadosAtualizacaoRestaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.dto.DadosCriacaoRestaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.usecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
@DisplayName("Teste do Controlador de Restaurante")
class RestauranteControllerTest {

    @Mock
    private CriarRestauranteUseCase criarRestauranteUseCase;

    @Mock
    private BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase;

    @Mock
    private ListarRestaurantesUseCase listarRestaurantesUseCase;

    @Mock
    private AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    @Mock
    private InativarRestauranteUseCase inativarRestauranteUseCase;

    @InjectMocks
    private RestauranteController restauranteController;

    @Test
    @DisplayName("Deve criar um restaurante com sucesso")
    void deveCriarRestauranteComSucesso() {
        UUID idDono = UUID.randomUUID();
        EnderecoJson enderecoJson = new EnderecoJson("Rua A", "123", "Apto 1", "Bairro X", "Cidade Y", "12345678", "UF");
        CriarRestauranteJson json = new CriarRestauranteJson("Restaurante Teste", enderecoJson, "Italiana", "08:00 - 22:00", idDono);
        
        Endereco endereco = new Endereco("Rua A", "123", "Apto 1", "Bairro X", "Cidade Y", "12345678", "UF");
        Restaurante restaurante = new Restaurante("Restaurante Teste", endereco, "Italiana", "08:00 - 22:00", idDono);
        
        when(criarRestauranteUseCase.executar(any(DadosCriacaoRestaurante.class))).thenReturn(restaurante);

        ResponseEntity<Map<String, UUID>> response = restauranteController.criarRestaurante(json);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(restaurante.getId(), response.getBody().get("id"));
        verify(criarRestauranteUseCase).executar(any(DadosCriacaoRestaurante.class));
    }

    @Test
    @DisplayName("Deve listar todos os restaurantes")
    void deveListarRestaurantes() {
        Endereco endereco = new Endereco("Rua A", "123", "Apto 1", "Bairro X", "Cidade Y", "12345678", "UF");
        Restaurante restaurante = new Restaurante("Restaurante 1", endereco, "Italiana", "10-22", UUID.randomUUID());
        when(listarRestaurantesUseCase.executar()).thenReturn(List.of(restaurante));

        ResponseEntity<List<RestauranteResponseJson>> response = restauranteController.listarRestaurantes();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(restaurante.getId(), response.getBody().get(0).id());
        verify(listarRestaurantesUseCase).executar();
    }

    @Test
    @DisplayName("Deve buscar restaurante por ID")
    void deveBuscarRestaurantePorId() {
        UUID id = UUID.randomUUID();
        Endereco endereco = new Endereco("Rua A", "123", "Apto 1", "Bairro X", "Cidade Y", "12345678", "UF");
        Restaurante restaurante = new Restaurante(id, "Restaurante 1", endereco, "Italiana", "10-22", UUID.randomUUID(), true);
        when(buscarRestaurantePorIdUseCase.executar(id)).thenReturn(restaurante);

        ResponseEntity<RestauranteResponseJson> response = restauranteController.buscarRestaurante(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().id());
        verify(buscarRestaurantePorIdUseCase).executar(id);
    }

    @Test
    @DisplayName("Deve atualizar um restaurante")
    void deveAtualizarRestaurante() {
        UUID id = UUID.randomUUID();
        AtualizarRestauranteJson.EnderecoJson enderecoJson = new AtualizarRestauranteJson.EnderecoJson("Nova Rua", "456", null, "Novo Bairro", "Nova Cidade", "87654321", "XX");
        AtualizarRestauranteJson json = new AtualizarRestauranteJson("Novo Nome", enderecoJson, "Francesa", "11:00 - 23:00");
        
        Endereco enderecoNovo = new Endereco("Nova Rua", "456", null, "Novo Bairro", "Nova Cidade", "87654321", "XX");
        Restaurante restauranteAtualizado = new Restaurante(id, "Novo Nome", enderecoNovo, "Francesa", "11:00 - 23:00", UUID.randomUUID(), true);
        
        when(atualizarRestauranteUseCase.executar(eq(id), any(DadosAtualizacaoRestaurante.class))).thenReturn(restauranteAtualizado);

        ResponseEntity<RestauranteResponseJson> response = restauranteController.atualizarRestaurante(id, json);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Novo Nome", response.getBody().nome());
        verify(atualizarRestauranteUseCase).executar(eq(id), any(DadosAtualizacaoRestaurante.class));
    }

    @Test
    @DisplayName("Deve inativar um restaurante")
    void deveInativarRestaurante() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response = restauranteController.inativarRestaurante(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(inativarRestauranteUseCase).executar(id);
    }
}
