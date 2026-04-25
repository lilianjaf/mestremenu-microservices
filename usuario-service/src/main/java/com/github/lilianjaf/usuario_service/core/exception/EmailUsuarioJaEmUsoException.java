package com.github.lilianjaf.usuario_service.core.exception;

public class EmailUsuarioJaEmUsoException extends RegraDeNegocioException {
    public EmailUsuarioJaEmUsoException(String mensagem) {
        super(mensagem);
    }
}
