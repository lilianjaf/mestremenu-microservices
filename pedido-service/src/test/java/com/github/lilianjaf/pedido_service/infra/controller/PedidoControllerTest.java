package com.github.lilianjaf.pedido_service.infra.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.usecase.ConfirmarPedidoUseCase;
import com.github.lilianjaf.pedido_service.core.usecase.ConsultarPedidoPorClienteUseCase;
import com.github.lilianjaf.pedido_service.core.usecase.ConsultarPedidoUseCase;
import com.github.lilianjaf.pedido_service.core.usecase.CriarPedidoUseCase;
import com.github.lilianjaf.pedido_service.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean TokenService tokenService;
    @MockBean CriarPedidoUseCase criarPedidoUseCase;
    @MockBean ConfirmarPedidoUseCase confirmarPedidoUseCase;
    @MockBean ConsultarPedidoUseCase consultarPedidoUseCase;
    @MockBean ConsultarPedidoPorClienteUseCase consultarPedidoPorClienteUseCase;

    private Pedido buildPedido() {
        UUID pedidoId = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        UUID restauranteId = UUID.randomUUID();
        ItemPedido item = new ItemPedido("Pizza", 2, BigDecimal.valueOf(25));
        return new Pedido(pedidoId, clienteId, restauranteId, List.of(item),
                BigDecimal.valueOf(50), StatusPedido.RASCUNHO, LocalDateTime.now());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /pedidos deve retornar 201")
    void criarPedido_deveRetornar201() throws Exception {
        Pedido pedido = buildPedido();
        when(criarPedidoUseCase.executar(any())).thenReturn(pedido);

        CriarPedidoJson body = new CriarPedidoJson(
                pedido.getRestauranteId(),
                List.of(new ItemPedidoJson("Pizza", 2, BigDecimal.valueOf(25))));

        mockMvc.perform(post("/api/v1/pedidos").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(pedido.getId().toString()));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /{id}/confirmar deve retornar 200")
    void confirmarPedido_deveRetornar200() throws Exception {
        Pedido pedido = buildPedido();
        when(confirmarPedidoUseCase.executar(any())).thenReturn(pedido);

        mockMvc.perform(post("/api/v1/pedidos/{id}/confirmar", pedido.getId()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedido.getId().toString()));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /{id} deve retornar 200")
    void consultarPedido_deveRetornar200() throws Exception {
        Pedido pedido = buildPedido();
        when(consultarPedidoUseCase.executar(any())).thenReturn(pedido);

        mockMvc.perform(get("/api/v1/pedidos/{id}", pedido.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedido.getId().toString()));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /meus-pedidos deve retornar 200 com lista")
    void listarMeusPedidos_deveRetornar200() throws Exception {
        Pedido pedido = buildPedido();
        when(consultarPedidoPorClienteUseCase.executar()).thenReturn(List.of(pedido));

        mockMvc.perform(get("/api/v1/pedidos/meus-pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pedido.getId().toString()));
    }
}
