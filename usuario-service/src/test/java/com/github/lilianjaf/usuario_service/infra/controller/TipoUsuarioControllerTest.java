package com.github.lilianjaf.usuario_service.infra.controller;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.usecase.AtualizarTipoUsuarioUsecase;
import com.github.lilianjaf.usuario_service.core.usecase.CriarTipoUsuarioUsecase;
import com.github.lilianjaf.usuario_service.core.usecase.DeletarTipoUsuarioUsecase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioControllerTest {

    @Mock
    private CriarTipoUsuarioUsecase criarTipoUsuarioUsecase;

    @Mock
    private AtualizarTipoUsuarioUsecase atualizarTipoUsuarioUsecase;

    @Mock
    private DeletarTipoUsuarioUsecase deletarTipoUsuarioUsecase;

    @InjectMocks
    private TipoUsuarioController tipoUsuarioController;

    @Test
    @DisplayName("Deve criar um tipo de usuario com sucesso")
    void deveCriarTipoUsuarioComSucesso() {
        CriarTipoUsuarioJson json = new CriarTipoUsuarioJson("Administrador", TipoNativo.DONO);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("admin");
        
        TipoUsuario tipoCriado = new TipoUsuario("Administrador", TipoNativo.DONO);
        when(criarTipoUsuarioUsecase.criar("admin", "Administrador", TipoNativo.DONO)).thenReturn(tipoCriado);

        ResponseEntity<Map<String, UUID>> response = tipoUsuarioController.criar(json, userDetails);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tipoCriado.getId(), response.getBody().get("id"));
        verify(criarTipoUsuarioUsecase).criar("admin", "Administrador", TipoNativo.DONO);
    }

    @Test
    @DisplayName("Deve atualizar um tipo de usuario com sucesso")
    void deveAtualizarTipoUsuarioComSucesso() {
        UUID id = UUID.randomUUID();
        AtualizarTipoUsuarioJson json = new AtualizarTipoUsuarioJson("Novo Nome");

        ResponseEntity<Void> response = tipoUsuarioController.atualizar(id, json);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(atualizarTipoUsuarioUsecase).atualizar(id, "Novo Nome");
    }

    @Test
    @DisplayName("Deve deletar um tipo de usuario com sucesso")
    void deveDeletarTipoUsuarioComSucesso() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response = tipoUsuarioController.deletar(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deletarTipoUsuarioUsecase).deletar(id);
    }
}
