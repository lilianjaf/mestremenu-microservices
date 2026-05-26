package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.domain.*;
import com.github.lilianjaf.restaurante_service.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoItemCardapio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AlterarCardapioRuleContextDtoTest {

    private static final UUID CARDAPIO_ID = UUID.randomUUID();
    private static final UUID RESTAURANTE_ID = UUID.randomUUID();

    private final Restaurante restaurante = new Restaurante(
            "Restaurante", new Endereco("R", "1", null, "B", "C", "12345678", "SP"), "Cat", "08-22", UUID.randomUUID());
    private final Cardapio cardapio = new Cardapio(CARDAPIO_ID, "Menu", RESTAURANTE_ID, Collections.emptyList());

    private AlterarCardapioRuleContextDto buildContext(Usuario usuario, DadosAtualizacaoCardapio dados) {
        return new AlterarCardapioRuleContextDto(usuario, restaurante, cardapio, dados, true, true);
    }

    @Test
    @DisplayName("isUsuarioTipoDono retorna true quando usuário é DONO")
    void isUsuarioTipoDono_trueParaDono() {
        Usuario dono = new Usuario(UUID.randomUUID(), TipoNativo.DONO);
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(CARDAPIO_ID, "Novo", null);

        assertTrue(buildContext(dono, dados).isUsuarioTipoDono());
    }

    @Test
    @DisplayName("isUsuarioTipoDono retorna false quando usuário é CLIENTE")
    void isUsuarioTipoDono_falseParaCliente() {
        Usuario cliente = new Usuario(UUID.randomUUID(), TipoNativo.CLIENTE);
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(CARDAPIO_ID, "Novo", null);

        assertFalse(buildContext(cliente, dados).isUsuarioTipoDono());
    }

    @Test
    @DisplayName("alterouItens retorna false quando itens é null")
    void alterouItens_falseQuandoNull() {
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(CARDAPIO_ID, "Novo", null);

        assertFalse(buildContext(null, dados).alterouItens());
    }

    @Test
    @DisplayName("alterouItens retorna true quando itens não é null")
    void alterouItens_trueQuandoNaoNull() {
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(CARDAPIO_ID, "Novo", Collections.emptyList());

        assertTrue(buildContext(null, dados).alterouItens());
    }

    @Test
    @DisplayName("hasPeloMenosUmItem retorna false quando lista de itens está vazia")
    void hasPeloMenosUmItem_falseQuandoVazio() {
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(CARDAPIO_ID, "Novo", Collections.emptyList());

        assertFalse(buildContext(null, dados).hasPeloMenosUmItem());
    }

    @Test
    @DisplayName("hasPeloMenosUmItem retorna true quando lista contém itens")
    void hasPeloMenosUmItem_trueQuandoTemItens() {
        List<DadosCriacaoItemCardapio> itens = List.of(
                new DadosCriacaoItemCardapio("Item", "Desc", BigDecimal.TEN, true, "foto.jpg", null));
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(CARDAPIO_ID, "Novo", itens);

        assertTrue(buildContext(null, dados).hasPeloMenosUmItem());
    }

    @Test
    @DisplayName("hasItensDuplicados retorna false quando não há nomes repetidos")
    void hasItensDuplicados_falseQuandoSemDuplicados() {
        List<DadosCriacaoItemCardapio> itens = List.of(
                new DadosCriacaoItemCardapio("Pizza", "Desc", BigDecimal.TEN, true, "foto.jpg", null),
                new DadosCriacaoItemCardapio("Burger", "Desc", BigDecimal.TEN, true, "foto2.jpg", null));
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(CARDAPIO_ID, "Novo", itens);

        assertFalse(buildContext(null, dados).hasItensDuplicados());
    }

    @Test
    @DisplayName("hasItensDuplicados retorna true quando há nomes repetidos")
    void hasItensDuplicados_trueQuandoTemDuplicados() {
        List<DadosCriacaoItemCardapio> itens = List.of(
                new DadosCriacaoItemCardapio("Pizza", "Desc 1", BigDecimal.TEN, true, "foto1.jpg", null),
                new DadosCriacaoItemCardapio("Pizza", "Desc 2", BigDecimal.valueOf(20), true, "foto2.jpg", null));
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(CARDAPIO_ID, "Novo", itens);

        assertTrue(buildContext(null, dados).hasItensDuplicados());
    }
}
