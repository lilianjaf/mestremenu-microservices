package com.github.lilianjaf.restaurante_service.infra.controller;

import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.exception.RegistroNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.usecase.*;
import com.github.lilianjaf.restaurante_service.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardapioController.class)
class ApiExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private CriarCardapioUseCase criarUseCase;
    @MockBean private AlterarCardapioUseCase editarUseCase;
    @MockBean private DeletarCardapioUseCase deletarUseCase;
    @MockBean private BuscarCardapioPorRestauranteUseCase buscarPorRestauranteUseCase;
    @MockBean private TokenService tokenService;

    @Test
    @WithMockUser
    @DisplayName("DomainException deve retornar 400")
    void domainException_returns400() throws Exception {
        doThrow(new CardapioException("erro de domínio")).when(deletarUseCase).executar(any());

        mockMvc.perform(delete("/api/v1/cardapios/{id}", UUID.randomUUID()).with(csrf()))
               .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("RegistroNaoEncontradoException deve retornar 404")
    void registroNaoEncontrado_returns404() throws Exception {
        when(buscarPorRestauranteUseCase.executar(any()))
                .thenThrow(new RegistroNaoEncontradoException("Recurso não encontrado"));

        mockMvc.perform(get("/api/v1/cardapios/restaurante/{id}", UUID.randomUUID()))
               .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("BadCredentialsException deve retornar 401")
    void badCredentials_returns401() throws Exception {
        when(buscarPorRestauranteUseCase.executar(any()))
                .thenThrow(new BadCredentialsException("credenciais inválidas"));

        mockMvc.perform(get("/api/v1/cardapios/restaurante/{id}", UUID.randomUUID()))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Exception genérica deve retornar 500")
    void genericException_returns500() throws Exception {
        when(buscarPorRestauranteUseCase.executar(any()))
                .thenThrow(new RuntimeException("erro inesperado"));

        mockMvc.perform(get("/api/v1/cardapios/restaurante/{id}", UUID.randomUUID()))
               .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    @DisplayName("JSON malformado deve retornar 400")
    void malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/cardapios").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
               .andExpect(status().isBadRequest());
    }
}
