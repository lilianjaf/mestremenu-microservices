package com.github.lilianjaf.pedido_service.core.exception;

public class PedidoNaoEncontradoException extends DomainException {
    public PedidoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
