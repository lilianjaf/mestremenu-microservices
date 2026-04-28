package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.TipoNativo;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

import java.math.BigDecimal;

public record CriarItemCardapioRuleContextDto(
    Usuario usuarioLogado,
    Restaurante restaurante,
    DadosCriacaoItemCardapio dados,
    boolean nomeUnico
) implements ItemCardapioRuleContext {
    public boolean isUsuarioDonoDoRestauranteDoItem() {
        return isUsuarioDonoDoRestaurante();
    }
    public boolean isUsuarioDonoDoRestaurante() {
        return usuarioLogado != null && 
               usuarioLogado.getTipoNativo() == TipoNativo.DONO && 
               restaurante != null && 
               restaurante.getIdDono().equals(usuarioLogado.getId());
    }

    public boolean hasRestauranteVinculado() {
        return restaurante != null;
    }

    public boolean hasTodosCamposPreenchidos() {
        return dados != null &&
               dados.nome() != null && !dados.nome().isBlank() &&
               dados.descricao() != null && !dados.descricao().isBlank() &&
               dados.preco() != null &&
               dados.idCardapio() != null;
    }

    public boolean isNomeUnico() {
        return nomeUnico;
    }

    public boolean isPrecoValido() {
        return dados != null && 
               dados.preco() != null && 
               dados.preco().compareTo(BigDecimal.ZERO) > 0;
    }
}
