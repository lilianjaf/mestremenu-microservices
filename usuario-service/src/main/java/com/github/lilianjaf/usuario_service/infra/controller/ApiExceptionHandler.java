package com.github.lilianjaf.usuario_service.infra.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.github.lilianjaf.usuario_service.core.exception.DomainException;
import com.github.lilianjaf.usuario_service.core.exception.RegistroNaoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Component("usuarioApiExceptionHandler")
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private record Field(String name, String userMessage) {}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        log.error("Ocorreu um erro interno inesperado: {}", ex.getMessage(), ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status,
                "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o problema persistir, entre em contato com o administrador.");
        problemDetail.setTitle("Erro de servidor");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/erro-de-servidor"));

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleCredenciaisInvalidas(Exception ex) {
        log.warn("Tentativa de login com credenciais inválidas: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Login ou senha inválidos");
        problemDetail.setTitle("Credenciais inválidas");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/credenciais-invalidas"));
        return problemDetail;
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleRegraDeNegocio(RuntimeException ex) {
        log.warn("Violação de regra de negócio/domínio: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Regra de negócio violada");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/erro-de-negocio"));
        return problemDetail;
    }

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ProblemDetail handleRegistroNaoEncontrado(RegistroNaoEncontradoException ex) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Recurso não encontrado");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/recurso-nao-encontrado"));
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ex.getRootCause();

        if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        } else if (ex.getCause() instanceof InvalidFormatException invalidExp && invalidExp.getTargetType().isEnum()) {
            String enumValues = Arrays.toString(invalidExp.getTargetType().getEnumConstants());
            String message = String.format("Valor '%s' não suportado. Os valores aceitos são: %s",
                    invalidExp.getValue(), enumValues);

            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    "The request body contains invalid data."
            );
            problemDetail.setDetail(message);
            problemDetail.setProperty("invalidValue", invalidExp.getValue());
            problemDetail.setProperty("acceptedValues", invalidExp.getTargetType().getEnumConstants());
            return handleExceptionInternal(ex, problemDetail, headers, status, request) ;
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Corpo da requisição inválido. Verifique a sintaxe do JSON.");
        problemDetail.setTitle("Corpo da requisição inválido");

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String path = joinPath(ex.getPath());

        String detail = String.format("O corpo da requisição contém uma propriedade desconhecida: '%s'. " +
                "Certifique-se de que os campos enviados estão de acordo com a documentação.", path);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle("Corpo da requisição inválido");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/corpo-da-requisicao-invalido"));

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<Field> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new Field(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.");
        problemDetail.setTitle("Dados inválidos");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/dados-invalidos"));
        problemDetail.setProperty("fields", fields);

        log.warn("Dados inválidos na requisição: {}", fields);

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }
}