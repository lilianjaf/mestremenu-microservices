package com.github.lilianjaf.usuario_service.infra.security;

import com.github.lilianjaf.usuario_service.core.gateway.CodificadorDeSenha;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CodificadorDeSenhaBcryptImpl implements CodificadorDeSenha {
    private final PasswordEncoder encoder;

    public CodificadorDeSenhaBcryptImpl(PasswordEncoder encoder) {
        this.encoder = encoder;
    }
    @Override
    public String codificar(String senhaPura) {
        return encoder.encode(senhaPura);
    }
}