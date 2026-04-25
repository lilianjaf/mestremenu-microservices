package com.github.lilianjaf.restaurante_service.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste da Entidade Usuario (Domínio Restaurante)")
class UsuarioTest {

    @Test
    @DisplayName("Deve identificar corretamente quando o usuário é dono")
    void deveIdentificarUsuarioDono() {
        TipoUsuario tipoDono = new TipoUsuario("Dono", "DONO");
        Usuario usuario = new Usuario(UUID.randomUUID(), true, tipoDono);

        assertTrue(usuario.isDono());
    }

    @Test
    @DisplayName("Deve identificar corretamente quando o usuário não é dono")
    void deveIdentificarUsuarioNaoDono() {
        TipoUsuario tipoCliente = new TipoUsuario("Cliente", "CLIENTE");
        Usuario usuario = new Usuario(UUID.randomUUID(), true, tipoCliente);

        assertFalse(usuario.isDono());
    }

    @Test
    @DisplayName("Deve instanciar usuário com dados corretos")
    void deveInstanciarUsuarioCorretamente() {
        UUID id = UUID.randomUUID();
        TipoUsuario tipo = new TipoUsuario("Cliente", "CLIENTE");
        
        Usuario usuario = new Usuario(id, false, tipo);

        assertAll(
                () -> assertEquals(id, usuario.getId()),
                () -> assertFalse(usuario.isAtivo())
        );
    }
}
