package com.github.lilianjaf.usuario_service.core.exception;

public class NomeTipoUsuarioJaEmUsoException extends RegraDeNegocioException {
    public NomeTipoUsuarioJaEmUsoException(String mensagem) {
        super(mensagem);
    }
}
