package com.github.lilianjaf.usuario_service.infra.security;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock private TokenService tokenService;
    @Mock private AutenticacaoService autenticacaoService;
    @Mock private FilterChain filterChain;
    @InjectMocks private SecurityFilter securityFilter;

    @Test
    @DisplayName("Sem header Authorization não deve setar autenticação")
    void semHeaderNaoDeveSetarAutenticacao() throws ServletException, IOException {
        SecurityContextHolder.clearContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        securityFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("Header inválido (não Bearer) não deve setar autenticação")
    void headerInvalidoNaoDeveSetarAutenticacao() throws ServletException, IOException {
        SecurityContextHolder.clearContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");
        MockHttpServletResponse response = new MockHttpServletResponse();

        securityFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("Token válido deve setar autenticação no contexto")
    void tokenValidoDeveSetarAutenticacao() throws ServletException, IOException {
        SecurityContextHolder.clearContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        TipoUsuario tipo = new TipoUsuario("DONO", TipoNativo.DONO);
        UsuarioBase usuario = mock(UsuarioBase.class);
        when(usuario.getTipoCustomizado()).thenReturn(tipo);
        UsuarioDetailsAdapter details = new UsuarioDetailsAdapter(usuario);

        when(tokenService.getSubject("valid-token")).thenReturn("user@email.com");
        when(autenticacaoService.loadUserByUsername("user@email.com")).thenReturn(details);

        securityFilter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Token inválido (subject nulo) não deve setar autenticação")
    void tokenInvalidoNaoDeveSetarAutenticacao() throws ServletException, IOException {
        SecurityContextHolder.clearContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer bad-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(tokenService.getSubject("bad-token")).thenReturn(null);

        securityFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(autenticacaoService);
    }
}
