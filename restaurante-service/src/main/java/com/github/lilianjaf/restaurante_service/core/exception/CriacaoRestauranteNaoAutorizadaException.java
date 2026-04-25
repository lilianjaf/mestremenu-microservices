package com.github.lilianjaf.restaurante_service.core.exception;

public class CriacaoRestauranteNaoAutorizadaException extends DomainException {
    public CriacaoRestauranteNaoAutorizadaException(String mensagem) {
        super(mensagem);
    }
}
