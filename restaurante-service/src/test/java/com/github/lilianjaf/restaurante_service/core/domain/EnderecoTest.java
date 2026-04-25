package com.github.lilianjaf.restaurante_service.core.domain;

import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste da Entidade de Valor Endereço")
class EnderecoTest {

    @Test
    @DisplayName("Deve instanciar um endereço corretamente com todos os dados válidos")
    void deveInstanciarUmEnderecoCorretamente() {
        String logradouro = "Rua das Flores";
        String numero = "123";
        String complemento = "Apto 1";
        String bairro = "Centro";
        String cidade = "São Paulo";
        String cep = "01010-010";
        String uf = "SP";

        Endereco endereco = new Endereco(logradouro, numero, complemento, bairro, cidade, cep, uf);

        assertAll(
                () -> assertNotNull(endereco),
                () -> assertEquals(logradouro, endereco.logradouro()),
                () -> assertEquals(numero, endereco.numero()),
                () -> assertEquals(complemento, endereco.complemento()),
                () -> assertEquals(bairro, endereco.bairro()),
                () -> assertEquals(cidade, endereco.cidade()),
                () -> assertEquals(cep, endereco.cep()),
                () -> assertEquals(uf, endereco.uf())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Deve lançar exceção ao tentar criar endereço com logradouro inválido")
    void deveLancarExcecaoQuandoLogradouroInvalido(String logradouroInvalido) {
        assertThrows(DomainException.class, () -> 
                new Endereco(logradouroInvalido, "123", "casa", "Bairro", "Cidade", "12345678", "UF"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar endereço com logradouro nulo")
    void deveLancarExcecaoQuandoLogradouroNulo() {
        assertThrows(DomainException.class, () -> 
                new Endereco(null, "123", "casa", "Bairro", "Cidade", "12345678", "UF"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Deve lançar exceção ao tentar criar endereço com número inválido")
    void deveLancarExcecaoQuandoNumeroInvalido(String numeroInvalido) {
        assertThrows(DomainException.class, () -> 
                new Endereco("Rua", numeroInvalido, "casa", "Bairro", "Cidade", "12345678", "UF"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Deve lançar exceção ao tentar criar endereço com bairro inválido")
    void deveLancarExcecaoQuandoBairroInvalido(String bairroInvalido) {
        assertThrows(DomainException.class, () -> 
                new Endereco("Rua", "123", "casa", bairroInvalido, "Cidade", "12345678", "UF"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Deve lançar exceção ao tentar criar endereço com cidade inválida")
    void deveLancarExcecaoQuandoCidadeInvalida(String cidadeInvalida) {
        assertThrows(DomainException.class, () -> 
                new Endereco("Rua", "123", "casa", "Bairro", cidadeInvalida, "12345678", "UF"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Deve lançar exceção ao tentar criar endereço com CEP inválido")
    void deveLancarExcecaoQuandoCepInvalido(String cepInvalido) {
        assertThrows(DomainException.class, () -> 
                new Endereco("Rua", "123", "casa", "Bairro", "Cidade", cepInvalido, "UF"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Deve lançar exceção ao tentar criar endereço com UF inválida")
    void deveLancarExcecaoQuandoUfInvalida(String ufInvalida) {
        assertThrows(DomainException.class, () -> 
                new Endereco("Rua", "123", "casa", "Bairro", "Cidade", "12345678", ufInvalida));
    }
}
