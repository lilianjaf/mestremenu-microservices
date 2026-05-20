package com.github.lilianjaf.pedido_service.infra.controller;

import com.github.lilianjaf.pedido_service.core.exception.DomainException;
import com.github.lilianjaf.pedido_service.core.exception.PedidoNaoEncontradoException;
import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNaoEncontrado(PedidoNaoEncontradoException ex) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        detail.setTitle("Recurso não encontrado");
        detail.setType(URI.create("https://mestremenu.com.br/erros/recurso-nao-encontrado"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detail);
    }

    @ExceptionHandler(UsuarioNaoAutenticadoException.class)
    public ResponseEntity<ProblemDetail> handleNaoAutenticado(UsuarioNaoAutenticadoException ex) {
        log.warn("Usuário não autenticado: {}", ex.getMessage());
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        detail.setTitle("Não autenticado");
        detail.setType(URI.create("https://mestremenu.com.br/erros/nao-autenticado"));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(detail);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomainException(DomainException ex) {
        log.warn("Violação de regra de negócio: {}", ex.getMessage());
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        detail.setTitle("Regra de negócio violada");
        detail.setType(URI.create("https://mestremenu.com.br/erros/erro-de-negocio"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUncaught(Exception ex) {
        log.error("Erro interno inesperado: {}", ex.getMessage(), ex);
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno inesperado. Tente novamente mais tarde.");
        detail.setTitle("Erro de servidor");
        detail.setType(URI.create("https://mestremenu.com.br/erros/erro-de-servidor"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(detail);
    }
}
