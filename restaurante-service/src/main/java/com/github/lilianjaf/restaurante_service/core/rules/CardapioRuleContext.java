package com.github.lilianjaf.restaurante_service.core.rules;

public interface CardapioRuleContext {
    boolean hasPeloMenosUmItem();
    boolean isNomeUnico();
    boolean hasNome();
    boolean hasItensDuplicados();
    boolean alterouItens();
}
