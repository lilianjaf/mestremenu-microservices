package com.github.lilianjaf.usuario_service.infra.controller;

import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.usecase.AtualizarTipoUsuarioUsecase;
import com.github.lilianjaf.usuario_service.core.usecase.CriarTipoUsuarioUsecase;
import com.github.lilianjaf.usuario_service.core.usecase.DeletarTipoUsuarioUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tipos-usuario")
public class TipoUsuarioController {

    private final CriarTipoUsuarioUsecase criarTipoUsuarioUsecase;
    private final AtualizarTipoUsuarioUsecase atualizarTipoUsuarioUsecase;
    private final DeletarTipoUsuarioUsecase deletarTipoUsuarioUsecase;

    public TipoUsuarioController(
            CriarTipoUsuarioUsecase criarTipoUsuarioUsecase,
            AtualizarTipoUsuarioUsecase atualizarTipoUsuarioUsecase,
            DeletarTipoUsuarioUsecase deletarTipoUsuarioUsecase) {
        this.criarTipoUsuarioUsecase = criarTipoUsuarioUsecase;
        this.atualizarTipoUsuarioUsecase = atualizarTipoUsuarioUsecase;
        this.deletarTipoUsuarioUsecase = deletarTipoUsuarioUsecase;
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> criar(
            @RequestBody CriarTipoUsuarioJson json,
            @AuthenticationPrincipal UserDetails userDetails) {
        String loginUsuarioLogado = userDetails.getUsername();
        TipoUsuario tipoCriado = criarTipoUsuarioUsecase.criar(loginUsuarioLogado, json.nome(), json.tipoNativo());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", tipoCriado.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable UUID id,
            @RequestBody AtualizarTipoUsuarioJson json) {
        atualizarTipoUsuarioUsecase.atualizar(id, json.nome());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        deletarTipoUsuarioUsecase.deletar(id);

        return ResponseEntity.noContent().build();
    }
}