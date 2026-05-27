package com.github.lilianjaf.usuario_service.core.domain;

import com.github.lilianjaf.usuario_service.core.dto.DadosCriacaoUsuario;
import com.github.lilianjaf.usuario_service.core.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioFactoryTest {

    private Endereco criarEnderecoValido() {
        return new Endereco("Rua", "123", null, "Bairro", "Cidade", "00000-000", "UF");
    }

    @Test
    @DisplayName("Deve criar um Cliente através da factory")
    void deveCriarCliente() {
        TipoUsuario tipoCliente = new TipoUsuario("Standard", TipoNativo.CLIENTE);
        DadosCriacaoUsuario dados = new DadosCriacaoUsuario("João", "joao@test.com", "joao.login", "senha", tipoCliente, criarEnderecoValido());

        UsuarioBase usuario = UsuarioFactory.criar(dados);

        assertNotNull(usuario);
        assertInstanceOf(Cliente.class, usuario);
        assertEquals(dados.nome(), usuario.getNome());
    }

    @Test
    @DisplayName("Deve criar um Dono através da factory")
    void deveCriarDono() {
        TipoUsuario tipoDono = new TipoUsuario("Proprietario", TipoNativo.DONO);
        DadosCriacaoUsuario dados = new DadosCriacaoUsuario("Dono", "dono@test.com", "dono.login", "senha", tipoDono, criarEnderecoValido());

        UsuarioBase usuario = UsuarioFactory.criar(dados);

        assertNotNull(usuario);
        assertInstanceOf(Dono.class, usuario);
        assertEquals(dados.nome(), usuario.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao passar dados nulos")
    void deveLancarExcecaoDadosNulos() {
        assertThrows(DomainException.class, () -> UsuarioFactory.criar(null));
    }

    @Test
    @DisplayName("Deve lançar exceção se o tipo customizado for nulo")
    void deveLancarExcecaoTipoCustomizadoNulo() {
        assertThrows(DomainException.class, () -> new DadosCriacaoUsuario("Nome", "email@test.com", "login", "senha", null, criarEnderecoValido()));
    }
}
