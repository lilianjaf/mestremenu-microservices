package com.github.lilianjaf.usuario_service.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonoTest {

    @Mock
    private Restaurante restaurante;

    private Endereco criarEnderecoValido() {
        return new Endereco("Rua", "123", null, "Bairro", "Cidade", "00000-000", "UF");
    }

    private TipoUsuario criarTipoUsuarioValido() {
        return new TipoUsuario("Proprietario", TipoNativo.DONO);
    }

    @Test
    @DisplayName("Deve criar um dono com lista de restaurantes")
    void deveCriarDonoComRestaurantes() {
        List<Restaurante> restaurantes = List.of(restaurante);
        
        Dono dono = new Dono("Dono", "dono@test.com", "dono.login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido(), restaurantes);

        assertEquals(restaurantes, dono.getRestaurantes());
    }

    @Test
    @DisplayName("Deve criar um dono com lista de restaurantes nula, resultando em lista vazia")
    void deveCriarDonoComRestaurantesNulo() {
        Dono dono = new Dono("Dono", "dono@test.com", "dono.login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido(), null);

        assertNotNull(dono.getRestaurantes());
        assertTrue(dono.getRestaurantes().isEmpty());
    }

    @Test
    @DisplayName("Deve verificar se o dono é proprietário de um restaurante")
    void deveVerificarSeEhProprietario() {
        Long idRestaurante = 1L;
        when(restaurante.getId()).thenReturn(idRestaurante);
        List<Restaurante> restaurantes = List.of(restaurante);
        Dono dono = new Dono("Dono", "dono@test.com", "dono.login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido(), restaurantes);

        boolean ehProprietario = dono.isProprietario(idRestaurante);

        assertTrue(ehProprietario);
    }

    @Test
    @DisplayName("Deve retornar falso ao verificar proprietário com lista vazia")
    void deveRetornarFalsoSeNaoTiverRestaurantes() {
        Dono dono = new Dono("Dono", "dono@test.com", "dono.login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido(), Collections.emptyList());

        boolean ehProprietario = dono.isProprietario(1L);

        assertFalse(ehProprietario);
    }

    @Test
    @DisplayName("Deve retornar falso se o id do restaurante não estiver na lista")
    void deveRetornarFalsoSeIdNaoEstiverNaLista() {
        when(restaurante.getId()).thenReturn(1L);
        Dono dono = new Dono("Dono", "dono@test.com", "dono.login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido(), List.of(restaurante));

        boolean ehProprietario = dono.isProprietario(2L);

        assertFalse(ehProprietario);
    }
}
