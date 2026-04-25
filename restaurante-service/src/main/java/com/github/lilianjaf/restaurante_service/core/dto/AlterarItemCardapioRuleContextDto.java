package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.ItemCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;

import java.math.BigDecimal;

public record AlterarItemCardapioRuleContextDto(
    Usuario usuarioLogado,
    Restaurante restaurante,
    ItemCardapio item,
    DadosAtualizacaoItemCardapio dados,
    boolean isUsuarioDonoDoRestauranteDoItem,
    boolean hasRestauranteVinculado,
    boolean isNomeUnico
) implements ItemCardapioRuleContext {
    public boolean isUsuarioDonoDoRestauranteDoItem() {
        return isUsuarioDonoDoRestauranteDoItem;
    }

    public boolean hasRestauranteVinculado() {
        return hasRestauranteVinculado;
    }

    public boolean hasTodosCamposPreenchidos() {
        return dados.nome() != null && !dados.nome().isBlank() &&
               dados.descricao() != null && !dados.descricao().isBlank() &&
               dados.preco() != null;
    }

    public boolean isNomeUnico() {
        return isNomeUnico;
    }

    public boolean isPrecoValido() {
        return dados.preco() != null && dados.preco().compareTo(BigDecimal.ZERO) > 0;
    }
}
