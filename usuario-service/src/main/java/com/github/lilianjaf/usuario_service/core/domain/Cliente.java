package com.github.lilianjaf.usuario_service.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Cliente extends UsuarioBase {

    public Cliente(String nome, String email, String login, String senha, TipoUsuario tipoCustomizado, Endereco endereco) {
        super(nome, email, login, senha, tipoCustomizado, endereco);
    }

    public Cliente(UUID id, String nome, String email, String login, String senha, TipoUsuario tipoCustomizado, Endereco endereco, LocalDateTime dataUltimaAlteracao, Boolean ativo) {
        super(id, nome, email, login, senha, tipoCustomizado, endereco, dataUltimaAlteracao, ativo);
    }
}