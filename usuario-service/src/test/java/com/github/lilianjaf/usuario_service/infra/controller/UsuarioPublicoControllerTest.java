package com.github.lilianjaf.usuario_service.infra.controller;

import com.github.lilianjaf.usuario_service.core.usecase.CriarUsuarioPublicoUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioPublicoControllerTest {

    @Mock
    private CriarUsuarioPublicoUseCase criarUsuarioPublicoUseCase;

    @InjectMocks
    private UsuarioPublicoController usuarioPublicoController;

    @Test
    @DisplayName("Deve criar um usuario publico com sucesso")
    void deveCriarUsuarioPublicoComSucesso() {
        EnderecoJson enderecoJson = new EnderecoJson("Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345-678", "UF");
        CriarUsuarioPublicoJson json = new CriarUsuarioPublicoJson("Nome", "email@teste.com", "login", "senha", enderecoJson);
        UUID idGerado = UUID.randomUUID();
        when(criarUsuarioPublicoUseCase.criar(
                "Nome", "email@teste.com", "login", "senha",
                "Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345-678", "UF"
        )).thenReturn(idGerado);

        ResponseEntity<Map<String, UUID>> response = usuarioPublicoController.criarUsuario(json);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(idGerado, response.getBody().get("id"));
    }

    @Test
    @DisplayName("Deve tentar criar um usuario publico sem endereco")
    void deveCriarUsuarioPublicoSemEndereco() {
        CriarUsuarioPublicoJson json = new CriarUsuarioPublicoJson("Nome", "email@teste.com", "login", "senha", null);
        UUID idGerado = UUID.randomUUID();
        when(criarUsuarioPublicoUseCase.criar(
                "Nome", "email@teste.com", "login", "senha",
                null, null, null, null, null, null, null
        )).thenReturn(idGerado);

        ResponseEntity<Map<String, UUID>> response = usuarioPublicoController.criarUsuario(json);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(idGerado, response.getBody().get("id"));
    }
}
