package com.github.lilianjaf.usuario_service.infra.controller;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.infra.controller.AutenticacaoController;
import com.github.lilianjaf.usuario_service.infra.security.LoginRequest;
import com.github.lilianjaf.usuario_service.infra.security.TokenResponse;
import com.github.lilianjaf.usuario_service.infra.security.TokenService;
import com.github.lilianjaf.usuario_service.infra.security.UsuarioDetailsAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticacaoControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AutenticacaoController autenticacaoController;

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar token")
    void deveRealizarLoginComSucesso() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("usuario");
        loginRequest.setSenha("senha");

        UUID userId = UUID.randomUUID();
        TipoUsuario tipoUsuario = new TipoUsuario("DONO", TipoNativo.DONO);
        UsuarioBase usuarioBase = mock(UsuarioBase.class);
        when(usuarioBase.getId()).thenReturn(userId);
        when(usuarioBase.getTipoCustomizado()).thenReturn(tipoUsuario);

        UsuarioDetailsAdapter principal = new UsuarioDetailsAdapter(usuarioBase);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("usuario");
        when(authentication.getPrincipal()).thenReturn(principal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.gerarToken("usuario", userId, "DONO")).thenReturn("token-gerado");

        TokenResponse response = autenticacaoController.login(loginRequest);

        assertNotNull(response);
        assertEquals("token-gerado", response.token());
    }
}
