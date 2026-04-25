package com.github.lilianjaf.restaurante_service.core.domain;

import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste da Entidade Restaurante")
class RestauranteTest {

    private Endereco criarEnderecoValido() {
        return new Endereco("Rua das Flores", "123", "Apto 1", "Centro", "São Paulo", "01010-010", "SP");
    }

    @Test
    @DisplayName("Deve criar um restaurante com dados válidos (construtor novo)")
    void deveCriarRestauranteComDadosValidos() {
        String nome = "Restaurante do João";
        Endereco endereco = criarEnderecoValido();
        String tipoCozinha = "Italiana";
        String horarioFuncionamento = "08:00 - 22:00";
        UUID idDono = UUID.randomUUID();

        Restaurante restaurante = new Restaurante(nome, endereco, tipoCozinha, horarioFuncionamento, idDono);

        assertAll(
                () -> assertNotNull(restaurante.getId()),
                () -> assertEquals(nome, restaurante.getNome()),
                () -> assertEquals(endereco, restaurante.getEndereco()),
                () -> assertEquals(tipoCozinha, restaurante.getTipoCozinha()),
                () -> assertEquals(horarioFuncionamento, restaurante.getHorarioFuncionamento()),
                () -> assertEquals(idDono, restaurante.getIdDono()),
                () -> assertTrue(restaurante.isAtivo())
        );
    }

    @Test
    @DisplayName("Deve carregar um restaurante existente com dados válidos (construtor de persistência)")
    void deveCarregarRestauranteExistente() {
        UUID id = UUID.randomUUID();
        String nome = "Restaurante do João";
        Endereco endereco = criarEnderecoValido();
        String tipoCozinha = "Italiana";
        String horarioFuncionamento = "08:00 - 22:00";
        UUID idDono = UUID.randomUUID();
        boolean ativo = false;

        Restaurante restaurante = new Restaurante(id, nome, endereco, tipoCozinha, horarioFuncionamento, idDono, ativo);

        assertAll(
                () -> assertEquals(id, restaurante.getId()),
                () -> assertEquals(nome, restaurante.getNome()),
                () -> assertEquals(endereco, restaurante.getEndereco()),
                () -> assertEquals(tipoCozinha, restaurante.getTipoCozinha()),
                () -> assertEquals(horarioFuncionamento, restaurante.getHorarioFuncionamento()),
                () -> assertEquals(idDono, restaurante.getIdDono()),
                () -> assertEquals(ativo, restaurante.isAtivo())
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar restaurante com nome nulo ou em branco")
    void deveLancarExcecaoQuandoNomeInvalido() {
        Endereco endereco = criarEnderecoValido();
        UUID idDono = UUID.randomUUID();

        assertThrows(DomainException.class, () -> 
                new Restaurante(null, endereco, "Italiana", "08:00 - 22:00", idDono));
        assertThrows(DomainException.class, () -> 
                new Restaurante("", endereco, "Italiana", "08:00 - 22:00", idDono));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar restaurante sem endereço")
    void deveLancarExcecaoQuandoEnderecoNulo() {
        UUID idDono = UUID.randomUUID();
        assertThrows(DomainException.class, () -> 
                new Restaurante("Nome", null, "Italiana", "08:00 - 22:00", idDono));
    }

    @Test
    @DisplayName("Deve atualizar dados do restaurante corretamente")
    void deveAtualizarRestaurante() {
        Restaurante restaurante = new Restaurante("Nome Antigo", criarEnderecoValido(), "Antiga", "08-18", UUID.randomUUID());
        String novoNome = "Novo Nome";
        Endereco novoEndereco = new Endereco("Nova Rua", "456", null, "Novo Bairro", "Nova Cidade", "99999-999", "RJ");
        String novaCozinha = "Francesa";
        String novoHorario = "10:00 - 23:00";

        restaurante.atualizar(novoNome, novoEndereco, novaCozinha, novoHorario);

        assertAll(
                () -> assertEquals(novoNome, restaurante.getNome()),
                () -> assertEquals(novoEndereco, restaurante.getEndereco()),
                () -> assertEquals(novaCozinha, restaurante.getTipoCozinha()),
                () -> assertEquals(novoHorario, restaurante.getHorarioFuncionamento())
        );
    }

    @Test
    @DisplayName("Deve manter dados antigos se atualizar for chamado com valores nulos")
    void deveManterDadosSeAtualizarComNulo() {
        String nomeOriginal = "Nome Original";
        Endereco enderecoOriginal = criarEnderecoValido();
        String cozinhaOriginal = "Italiana";
        String horarioOriginal = "08:00 - 22:00";
        Restaurante restaurante = new Restaurante(nomeOriginal, enderecoOriginal, cozinhaOriginal, horarioOriginal, UUID.randomUUID());

        restaurante.atualizar(null, null, null, null);

        assertAll(
                () -> assertEquals(nomeOriginal, restaurante.getNome()),
                () -> assertEquals(enderecoOriginal, restaurante.getEndereco()),
                () -> assertEquals(cozinhaOriginal, restaurante.getTipoCozinha()),
                () -> assertEquals(horarioOriginal, restaurante.getHorarioFuncionamento())
        );
    }

    @Test
    @DisplayName("Deve inativar o restaurante corretamente")
    void deveInativarRestaurante() {
        Restaurante restaurante = new Restaurante("Nome", criarEnderecoValido(), "Italiana", "08:00", UUID.randomUUID());
        assertTrue(restaurante.isAtivo());

        restaurante.inativar();

        assertFalse(restaurante.isAtivo());
    }
}
