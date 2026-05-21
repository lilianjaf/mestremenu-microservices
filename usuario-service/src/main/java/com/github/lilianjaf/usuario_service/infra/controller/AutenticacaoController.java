package com.github.lilianjaf.usuario_service.infra.controller;

import com.github.lilianjaf.usuario_service.infra.security.LoginRequest;
import com.github.lilianjaf.usuario_service.infra.security.TokenResponse;
import com.github.lilianjaf.usuario_service.infra.security.TokenService;
import com.github.lilianjaf.usuario_service.infra.security.UsuarioDetailsAdapter;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public TokenResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getSenha());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UsuarioDetailsAdapter principal = (UsuarioDetailsAdapter) authentication.getPrincipal();
        String tipoNativo = principal.getUsuario().getTipoCustomizado().getTipoNativo().name();
        String token = tokenService.gerarToken(authentication.getName(), principal.getUsuario().getId(), tipoNativo);

        return new TokenResponse(token);
    }
}
