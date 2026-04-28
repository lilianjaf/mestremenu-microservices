package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.TipoNativo;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

import com.github.lilianjaf.restaurante_service.core.rules.CardapioRuleContext;

import java.util.HashSet;
import java.util.Set;

public record AlterarCardapioRuleContextDto(
    Usuario usuarioLogado,
    Restaurante restaurante,
    Cardapio cardapio,
    DadosAtualizacaoCardapio dados,
    boolean isCardapioDoProprioRestaurante,
    boolean isNomeUnico
) implements CardapioRuleContext, PermissionRuleContext {
    
    @Override
    public boolean isUsuarioDonoDoRestaurante() {
        return isCardapioDoProprioRestaurante;
    }
    public boolean isUsuarioTipoDono() {
        return usuarioLogado != null && usuarioLogado.getTipoNativo() == TipoNativo.DONO;
    }

    public boolean isCardapioDoProprioRestaurante() {
        return isCardapioDoProprioRestaurante;
    }

    public boolean hasNome() {
        return dados.nome() != null && !dados.nome().isBlank();
    }

    public boolean isNomeUnico() {
        return isNomeUnico;
    }

    public boolean hasPeloMenosUmItem() {
        return dados.itens() != null && !dados.itens().isEmpty();
    }

    public boolean alterouItens() {
        return dados.itens() != null;
    }

    public boolean hasItensDuplicados() {
        if (dados.itens() == null) return false;
        Set<String> nomesItens = new HashSet<>();
        for (var item : dados.itens()) {
            if (!nomesItens.add(item.nome().toLowerCase().trim())) {
                return true;
            }
        }
        return false;
    }
}
