package com.github.lilianjaf.usuario_service.core.rules;

import java.util.function.BooleanSupplier;

public record CriacaoUsuarioPublicoContext(
        String nome,
        String email,
        String login,
        String senha,
        String nomeTipo,
        String logradouro,
        String numero,
        String bairro,
        String cidade,
        String cep,
        String uf,
        BooleanSupplier emailJaExiste,
        BooleanSupplier loginJaExiste
) {
    public boolean isEmailJaCadastrado() {
        return emailJaExiste.getAsBoolean();
    }

    public boolean isLoginJaCadastrado() {
        return loginJaExiste.getAsBoolean();
    }

    public boolean isSenhaInformada() {
        return senha != null && !senha.isBlank();
    }

    public boolean isTipoCliente() {
        return "cliente".equalsIgnoreCase(nomeTipo);
    }
}
