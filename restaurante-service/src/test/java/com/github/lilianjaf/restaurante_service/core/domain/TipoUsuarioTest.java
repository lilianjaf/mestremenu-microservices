package com.github.lilianjaf.restaurante_service.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Teste da Entidade TipoUsuario")
class TipoUsuarioTest {

    @Test
    @DisplayName("Deve instanciar TipoUsuario corretamente")
    void deveInstanciarTipoUsuarioCorretamente() {
        String nome = "Administrador";
        String tipoString = "DONO";

        TipoUsuario tipoUsuario = new TipoUsuario(nome, tipoString);

        assertAll(
                () -> assertEquals(nome, tipoUsuario.getNome()),
                () -> assertEquals(TipoNativo.DONO, tipoUsuario.getTipoNativo())
        );
    }
}
