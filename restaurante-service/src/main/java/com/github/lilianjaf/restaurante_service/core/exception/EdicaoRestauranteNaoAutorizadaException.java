package com.github.lilianjaf.restaurante_service.core.exception;

public class EdicaoRestauranteNaoAutorizadaException extends DomainException {
    public EdicaoRestauranteNaoAutorizadaException(String mensagem) {
        super(mensagem);
    }
}
