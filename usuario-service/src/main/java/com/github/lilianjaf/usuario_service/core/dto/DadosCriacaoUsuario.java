package com.github.lilianjaf.usuario_service.core.dto;

import com.github.lilianjaf.mestremenuclean.usuario.core.domain.Endereco;
import com.github.lilianjaf.mestremenuclean.usuario.core.domain.TipoUsuario;
import com.github.lilianjaf.mestremenuclean.usuario.core.exception.DomainException;

public record DadosCriacaoUsuario(
        String nome,
        String email,
        String login,
        String senha,
        TipoUsuario tipoCustomizado,
        Endereco endereco
) {
    public DadosCriacaoUsuario {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DomainException("Nome não pode ser nulo ou vazio.");
        }
        if (email == null || !email.contains("@")) {
            throw new DomainException("Formato de email inválido.");
        }
        if (login == null || login.trim().isEmpty()) {
            throw new DomainException("Login não pode ser nulo ou vazio.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new DomainException("Senha não pode ser nula ou vazia.");
        }
        if (tipoCustomizado == null) {
            throw new DomainException("O tipo customizado é obrigatório para criar um usuário.");
        }
        if  (endereco == null) {
            throw new DomainException("O endereço é obrigatório para criar um usuário.");
        }
    }
}