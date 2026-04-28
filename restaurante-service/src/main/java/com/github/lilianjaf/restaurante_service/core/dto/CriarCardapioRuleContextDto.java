package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.TipoNativo;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

import com.github.lilianjaf.restaurante_service.core.rules.CardapioRuleContext;

import java.util.HashSet;
import java.util.Set;

public record CriarCardapioRuleContextDto(
        Usuario usuarioLogado,
        Restaurante restaurante,
        DadosCriacaoCardapio dados,
        boolean nomeUnico
) implements CardapioRuleContext, PermissionRuleContext {
    public boolean isUsuarioDonoDoRestaurante() {
        return usuarioLogado != null &&
               restaurante != null &&
               usuarioLogado.getTipoNativo() == TipoNativo.DONO &&
               restaurante.getIdDono().equals(usuarioLogado.getId());
    }

    public boolean hasNome() {
        return dados != null && dados.nome() != null && !dados.nome().isBlank();
    }

    public boolean isNomeUnico() {
        return nomeUnico;
    }

    public boolean hasPeloMenosUmItem() {
        return dados != null && dados.itens() != null && !dados.itens().isEmpty();
    }

    public boolean alterouItens() {
        return true;
    }

    public boolean hasItensDuplicados() {
        if (dados == null || dados.itens() == null) {
            return false;
        }
        Set<String> nomesItens = new HashSet<>();
        for (DadosCriacaoItemCardapio item : dados.itens()) {
            if (!nomesItens.add(item.nome())) {
                return true;
            }
        }
        return false;
    }
}
