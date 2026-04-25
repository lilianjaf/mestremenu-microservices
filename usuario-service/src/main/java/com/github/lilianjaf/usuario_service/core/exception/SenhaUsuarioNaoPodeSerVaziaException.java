package com.github.lilianjaf.usuario_service.core.exception;

public class SenhaUsuarioNaoPodeSerVaziaException extends RegraDeNegocioException {
    public SenhaUsuarioNaoPodeSerVaziaException(String mensagem) {
        super(mensagem);
    }
}
