package com.github.lilianjaf.pedido_service.infra.controller;

import com.github.lilianjaf.pedido_service.core.exception.AcessoNegadoAoPedidoException;
import com.github.lilianjaf.pedido_service.core.exception.PedidoNaoEncontradoException;
import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class ApiExceptionHandlerTest {

    @Autowired MockMvc mockMvc;

    @MockBean TokenService tokenService;
    @MockBean CriarPedidoUseCase criarPedidoUseCase;
    @MockBean ConfirmarPedidoUseCase confirmarPedidoUseCase;
    @MockBean ConsultarPedidoUseCase consultarPedidoUseCase;
    @MockBean ConsultarPedidoPorClienteUseCase consultarPedidoPorClienteUseCase;

    @Test
    @WithMockUser
    @DisplayName("PedidoNaoEncontradoException deve retornar 404")
    void pedidoNaoEncontrado_deveRetornar404() throws Exception {
        when(consultarPedidoUseCase.executar(any()))
                .thenThrow(new PedidoNaoEncontradoException("Não encontrado"));

        mockMvc.perform(get("/api/v1/pedidos/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("UsuarioNaoAutenticadoException deve retornar 401")
    void usuarioNaoAutenticado_deveRetornar401() throws Exception {
        when(consultarPedidoUseCase.executar(any()))
                .thenThrow(new UsuarioNaoAutenticadoException("Não autenticado"));

        mockMvc.perform(get("/api/v1/pedidos/{id}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("DomainException deve retornar 400")
    void domainException_deveRetornar400() throws Exception {
        when(consultarPedidoUseCase.executar(any()))
                .thenThrow(new AcessoNegadoAoPedidoException("Acesso negado"));

        mockMvc.perform(get("/api/v1/pedidos/{id}", UUID.randomUUID()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Exception não mapeada deve retornar 500")
    void exceptionNaoMapeada_deveRetornar500() throws Exception {
        when(consultarPedidoUseCase.executar(any()))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/api/v1/pedidos/{id}", UUID.randomUUID()))
                .andExpect(status().isInternalServerError());
    }
}
