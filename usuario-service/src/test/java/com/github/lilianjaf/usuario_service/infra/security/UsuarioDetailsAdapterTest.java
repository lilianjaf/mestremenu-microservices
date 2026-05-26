package com.github.lilianjaf.usuario_service.infra.security;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioDetailsAdapterTest {

    private UsuarioDetailsAdapter adapter;
    private UsuarioBase usuario;

    @BeforeEach
    void setUp() {
        TipoUsuario tipo = new TipoUsuario("DONO", TipoNativo.DONO);
        usuario = mock(UsuarioBase.class);
        when(usuario.getTipoCustomizado()).thenReturn(tipo);
        when(usuario.getSenha()).thenReturn("senha-criptografada");
        when(usuario.getLogin()).thenReturn("user@email.com");
        when(usuario.getAtivo()).thenReturn(true);
        adapter = new UsuarioDetailsAdapter(usuario);
    }

    @Test
    @DisplayName("getAuthorities deve retornar ROLE_ prefixado com tipoNativo")
    void getAuthoritiesDeveRetornarRoleCorreta() {
        Collection<? extends GrantedAuthority> authorities = adapter.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_DONO", authorities.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("getPassword deve retornar senha do usuário")
    void getPasswordDeveRetornarSenha() {
        assertEquals("senha-criptografada", adapter.getPassword());
    }

    @Test
    @DisplayName("getUsername deve retornar login do usuário")
    void getUsernameDeveRetornarLogin() {
        assertEquals("user@email.com", adapter.getUsername());
    }

    @Test
    @DisplayName("isAccountNonExpired deve retornar true")
    void isAccountNonExpiredDeveRetornarTrue() {
        assertTrue(adapter.isAccountNonExpired());
    }

    @Test
    @DisplayName("isAccountNonLocked deve retornar true")
    void isAccountNonLockedDeveRetornarTrue() {
        assertTrue(adapter.isAccountNonLocked());
    }

    @Test
    @DisplayName("isCredentialsNonExpired deve retornar true")
    void isCredentialsNonExpiredDeveRetornarTrue() {
        assertTrue(adapter.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("isEnabled deve retornar ativo do usuário")
    void isEnabledDeveRetornarAtivo() {
        assertTrue(adapter.isEnabled());
    }

    @Test
    @DisplayName("getUsuario deve retornar a instância original")
    void getUsuarioDeveRetornarInstanciaOriginal() {
        assertSame(usuario, adapter.getUsuario());
    }
}
