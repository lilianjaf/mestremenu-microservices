package com.github.lilianjaf.restaurante_service.core.dto;

public interface ItemCardapioRuleContext {
    boolean isUsuarioDonoDoRestauranteDoItem();
    boolean hasRestauranteVinculado();
    boolean hasTodosCamposPreenchidos();
    boolean isNomeUnico();
    boolean isPrecoValido();
}
