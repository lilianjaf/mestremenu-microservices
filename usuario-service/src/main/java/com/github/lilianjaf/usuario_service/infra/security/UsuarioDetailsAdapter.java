package com.github.lilianjaf.usuario_service.infra.security;

import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UsuarioDetailsAdapter implements UserDetails {

    private final UsuarioBase usuario;

    public UsuarioDetailsAdapter(UsuarioBase usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = usuario.getTipoCustomizado().getTipoNativo().name();
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.getAtivo();
    }

    public UsuarioBase getUsuario() {
        return usuario;
    }
}