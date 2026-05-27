package com.github.lilianjaf.usuario_service.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Dono extends UsuarioBase {

    public Dono(String nome, String email, String login, String senha, TipoUsuario tipoCustomizado, Endereco endereco) {
        super(nome, email, login, senha, tipoCustomizado, endereco);
    }

    public Dono(UUID id, String nome, String email, String login, String senha, TipoUsuario tipoCustomizado, Endereco endereco, LocalDateTime dataUltimaAlteracao, Boolean ativo) {
        super(id, nome, email, login, senha, tipoCustomizado, endereco, dataUltimaAlteracao, ativo);
    }

}