package com.github.lilianjaf.usuario_service.core.domain;

import com.github.lilianjaf.usuario_service.core.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    @Test
    @DisplayName("Deve criar um endereço válido com todos os campos obrigatórios preenchidos")
    void deveCriarEnderecoValido() {
        String logradouro = "Rua das Flores";
        String numero = "123";
        String complemento = "Apto 101";
        String bairro = "Centro";
        String cidade = "São Paulo";
        String cep = "01010-010";
        String uf = "SP";

        Endereco endereco = assertDoesNotThrow(() -> 
            new Endereco(logradouro, numero, complemento, bairro, cidade, cep, uf)
        );

        assertEquals(logradouro, endereco.logradouro());
        assertEquals(numero, endereco.numero());
        assertEquals(complemento, endereco.complemento());
        assertEquals(bairro, endereco.bairro());
        assertEquals(cidade, endereco.cidade());
        assertEquals(cep, endereco.cep());
        assertEquals(uf, endereco.uf());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar endereço com logradouro vazio")
    void deveLancarExcecaoLogradouroVazio() {
        assertThrows(DomainException.class, () -> 
            new Endereco("", "123", null, "Centro", "SP", "01010-010", "SP")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar endereço com número nulo")
    void deveLancarExcecaoNumeroNulo() {
        assertThrows(DomainException.class, () -> 
            new Endereco("Rua", null, null, "Centro", "SP", "01010-010", "SP")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar endereço com bairro em branco")
    void deveLancarExcecaoBairroEmBranco() {
        assertThrows(DomainException.class, () -> 
            new Endereco("Rua", "123", null, "   ", "SP", "01010-010", "SP")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar endereço com cidade nula")
    void deveLancarExcecaoCidadeNula() {
        assertThrows(DomainException.class, () -> 
            new Endereco("Rua", "123", null, "Centro", null, "01010-010", "SP")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar endereço com CEP vazio")
    void deveLancarExcecaoCepVazio() {
        assertThrows(DomainException.class, () -> 
            new Endereco("Rua", "123", null, "Centro", "SP", "", "SP")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar endereço com UF nula")
    void deveLancarExcecaoUfNula() {
        assertThrows(DomainException.class, () -> 
            new Endereco("Rua", "123", null, "Centro", "SP", "01010-010", null)
        );
    }
}
