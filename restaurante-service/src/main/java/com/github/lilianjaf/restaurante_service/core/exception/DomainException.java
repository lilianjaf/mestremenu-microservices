package com.github.lilianjaf.restaurante_service.core.exception;

public class DomainException extends RuntimeException {
    public DomainException(String mensagem) {
        super(mensagem);
    }
}