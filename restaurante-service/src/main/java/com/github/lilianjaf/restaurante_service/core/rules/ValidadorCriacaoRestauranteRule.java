package com.github.lilianjaf.restaurante_service.core.rules;

public interface ValidadorCriacaoRestauranteRule {
    <T extends CriacaoRestauranteContext> void validar(T context);
}
