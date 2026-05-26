package com.github.lilianjaf.usuario_service.infra.controller;

import com.github.lilianjaf.usuario_service.core.exception.DomainException;
import com.github.lilianjaf.usuario_service.core.exception.RegistroNaoEncontradoException;
import com.github.lilianjaf.usuario_service.core.usecase.CriarUsuarioPublicoUseCase;
import com.github.lilianjaf.usuario_service.infra.security.AutenticacaoService;
import com.github.lilianjaf.usuario_service.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioPublicoController.class)
class ApiExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private CriarUsuarioPublicoUseCase criarUseCase;
    @MockBean private TokenService tokenService;
    @MockBean private AutenticacaoService autenticacaoService;

    private static final String VALID_JSON = """
            {
              "nome": "João",
              "email": "joao@email.com",
              "login": "joao",
              "senha": "senha123",
              "endereco": {
                "logradouro": "Rua A",
                "numero": "10",
                "complemento": "",
                "bairro": "Centro",
                "cidade": "SP",
                "cep": "01310-000",
                "uf": "SP"
              }
            }
            """;

    @Test
    @WithMockUser
    @DisplayName("DomainException deve retornar 400")
    void domainException_returns400() throws Exception {
        when(criarUseCase.criar(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new DomainException("regra violada") {});

        mockMvc.perform(post("/api/v1/publico/usuarios").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("RegistroNaoEncontradoException deve retornar 404")
    void registroNaoEncontrado_returns404() throws Exception {
        when(criarUseCase.criar(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new RegistroNaoEncontradoException("não encontrado"));

        mockMvc.perform(post("/api/v1/publico/usuarios").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("BadCredentialsException deve retornar 401")
    void badCredentials_returns401() throws Exception {
        when(criarUseCase.criar(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new BadCredentialsException("credenciais inválidas"));

        mockMvc.perform(post("/api/v1/publico/usuarios").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("RuntimeException deve retornar 500")
    void runtimeException_returns500() throws Exception {
        when(criarUseCase.criar(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("erro inesperado"));

        mockMvc.perform(post("/api/v1/publico/usuarios").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    @DisplayName("JSON malformado deve retornar 400")
    void malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/publico/usuarios").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Campos obrigatórios ausentes devem retornar 400 (MethodArgumentNotValid)")
    void missingRequiredFields_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/publico/usuarios").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

}
