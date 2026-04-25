package com.github.lilianjaf.usuario_service.core.domain;

import com.github.lilianjaf.usuario_service.core.exception.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TipoUsuarioTest {

    @Test
    @DisplayName("Deve criar um tipo de usuário válido")
    void deveCriarTipoUsuarioValido() {
        String nome = "Premium";
        TipoNativo tipoNativo = TipoNativo.CLIENTE;

        TipoUsuario tipoUsuario = assertDoesNotThrow(() -> new TipoUsuario(nome, tipoNativo));

        assertNotNull(tipoUsuario.getId());
        assertEquals(nome, tipoUsuario.getNome());
        assertEquals(tipoNativo, tipoUsuario.getTipoNativo());
    }

    @Test
    @DisplayName("Deve criar um tipo de usuário com ID específico")
    void deveCriarTipoUsuarioComId() {
        UUID id = UUID.randomUUID();
        String nome = "Admin";
        TipoNativo tipoNativo = TipoNativo.DONO;

        TipoUsuario tipoUsuario = new TipoUsuario(id, nome, tipoNativo);

        assertEquals(id, tipoUsuario.getId());
        assertEquals(nome, tipoUsuario.getNome());
        assertEquals(tipoNativo, tipoUsuario.getTipoNativo());
    }

    @Test
    @DisplayName("Deve atualizar o nome do tipo de usuário")
    void deveAtualizarNome() {
        TipoUsuario tipoUsuario = new TipoUsuario("Basico", TipoNativo.CLIENTE);
        String novoNome = "Standard";

        tipoUsuario.atualizarNome(novoNome);

        assertEquals(novoNome, tipoUsuario.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com nome nulo")
    void deveLancarExcecaoNomeNulo() {
        assertThrows(RegraDeNegocioException.class, () -> new TipoUsuario(null, TipoNativo.CLIENTE));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com nome vazio")
    void deveLancarExcecaoNomeVazio() {
        assertThrows(RegraDeNegocioException.class, () -> new TipoUsuario("   ", TipoNativo.CLIENTE));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com tipo nativo nulo")
    void deveLancarExcecaoTipoNativoNulo() {
        assertThrows(RegraDeNegocioException.class, () -> new TipoUsuario("Teste", null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar para nome inválido")
    void deveLancarExcecaoAoAtualizarNomeInvalido() {
        TipoUsuario tipoUsuario = new TipoUsuario("Teste", TipoNativo.CLIENTE);
        assertThrows(RegraDeNegocioException.class, () -> tipoUsuario.atualizarNome(""));
    }
}
