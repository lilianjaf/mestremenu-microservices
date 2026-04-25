package com.github.lilianjaf.usuario_service.infra.controller;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.dto.UsuarioOutput;
import com.github.lilianjaf.usuario_service.core.usecase.AtualizarUsuarioUsecase;
import com.github.lilianjaf.usuario_service.core.usecase.BuscarUsuarioUsecase;
import com.github.lilianjaf.usuario_service.core.usecase.CriarUsuarioUsecase;
import com.github.lilianjaf.usuario_service.core.usecase.InativarUsuarioUsecase;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private CriarUsuarioUsecase criarUsuarioUsecase;

    @Mock
    private BuscarUsuarioUsecase buscarUsuarioUsecase;

    @Mock
    private AtualizarUsuarioUsecase atualizarUsuarioUsecase;

    @Mock
    private InativarUsuarioUsecase inativarUsuarioUsecase;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    @DisplayName("Deve criar um usuario com sucesso")
    void deveCriarUsuarioComSucesso() {
        EnderecoJson enderecoJson = new EnderecoJson("Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345-678", "UF");
        CriarUsuarioJson json = new CriarUsuarioJson("Nome", "email@teste.com", "login", "senha", "Cliente", TipoNativo.CLIENTE, enderecoJson);
        UUID idGerado = UUID.randomUUID();
        when(criarUsuarioUsecase.criar(
                "Nome", "email@teste.com", "login", "senha", "Cliente", TipoNativo.CLIENTE,
                "Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345-678", "UF"
        )).thenReturn(idGerado);

        ResponseEntity<Map<String, UUID>> response = usuarioController.criarUsuario(json);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(idGerado, response.getBody().get("id"));
    }

    @Test
    @DisplayName("Deve buscar um usuario com sucesso")
    void deveBuscarUsuarioComSucesso() {
        UUID id = UUID.randomUUID();
        UsuarioOutput output = new UsuarioOutput(id, "Nome", "email@teste.com", "login", "Cliente", "CLIENTE", true);
        when(buscarUsuarioUsecase.buscarPorId(id)).thenReturn(output);

        ResponseEntity<UsuarioResponseJson> response = usuarioController.buscarUsuario(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().id());
        assertEquals("Nome", response.getBody().nome());
    }

    @Test
    @DisplayName("Deve atualizar usuario com endereco")
    void deveAtualizarUsuarioComEndereco() {
        UUID id = UUID.randomUUID();
        EnderecoJson enderecoJson = new EnderecoJson("Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345-678", "UF");
        AtualizarUsuarioJson json = new AtualizarUsuarioJson("Novo Nome", "novo@email.com", enderecoJson);

        ResponseEntity<Void> response = usuarioController.atualizarUsuario(id, json);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(atualizarUsuarioUsecase).atualizarComEndereco(
                id, "Novo Nome", "novo@email.com", "Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345-678", "UF"
        );
    }

    @Test
    @DisplayName("Deve atualizar usuario sem endereco")
    void deveAtualizarUsuarioSemEndereco() {
        UUID id = UUID.randomUUID();
        AtualizarUsuarioJson json = new AtualizarUsuarioJson("Novo Nome", "novo@email.com", null);

        ResponseEntity<Void> response = usuarioController.atualizarUsuario(id, json);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(atualizarUsuarioUsecase).atualizarSemEndereco(id, "Novo Nome", "novo@email.com");
    }

    @Test
    @DisplayName("Deve inativar um usuario com sucesso")
    void deveInativarUsuarioComSucesso() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response = usuarioController.inativarUsuario(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(inativarUsuarioUsecase).inativar(id);
    }
}
