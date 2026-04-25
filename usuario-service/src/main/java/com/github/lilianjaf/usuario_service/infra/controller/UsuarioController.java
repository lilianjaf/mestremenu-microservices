package com.github.lilianjaf.usuario_service.infra.controller;

import com.github.lilianjaf.mestremenuclean.usuario.core.dto.UsuarioOutput;
import com.github.lilianjaf.mestremenuclean.usuario.core.usecase.AtualizarUsuarioUsecase;
import com.github.lilianjaf.mestremenuclean.usuario.core.usecase.BuscarUsuarioUsecase;
import com.github.lilianjaf.mestremenuclean.usuario.core.usecase.CriarUsuarioUsecase;
import com.github.lilianjaf.mestremenuclean.usuario.core.usecase.InativarUsuarioUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final CriarUsuarioUsecase criarUsuarioUsecase;
    private final BuscarUsuarioUsecase buscarUsuarioUsecase;
    private final AtualizarUsuarioUsecase atualizarUsuarioUsecase;
    private final InativarUsuarioUsecase inativarUsuarioUsecase;

    public UsuarioController(
            CriarUsuarioUsecase criarUsuarioUsecase,
            BuscarUsuarioUsecase buscarUsuarioUsecase,
            AtualizarUsuarioUsecase atualizarUsuarioUsecase,
            InativarUsuarioUsecase inativarUsuarioUsecase) {
        this.criarUsuarioUsecase = criarUsuarioUsecase;
        this.buscarUsuarioUsecase = buscarUsuarioUsecase;
        this.atualizarUsuarioUsecase = atualizarUsuarioUsecase;
        this.inativarUsuarioUsecase = inativarUsuarioUsecase;
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> criarUsuario(
            @RequestBody CriarUsuarioJson json) {
        UUID idGerado = criarUsuarioUsecase.criar(
                json.nome(),
                json.email(),
                json.login(),
                json.senha(),
                json.nomeTipoDesejado(),
                json.tipoNativo(),
                json.endereco().logradouro(),
                json.endereco().numero(),
                json.endereco().complemento(),
                json.endereco().bairro(),
                json.endereco().cidade(),
                json.endereco().cep(),
                json.endereco().uf()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", idGerado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseJson> buscarUsuario(@PathVariable UUID id) {
        UsuarioOutput dadosUsuario = buscarUsuarioUsecase.buscarPorId(id);

        UsuarioResponseJson response = new UsuarioResponseJson(
                dadosUsuario.id(),
                dadosUsuario.nome(),
                dadosUsuario.email(),
                dadosUsuario.login(),
                dadosUsuario.tipoPerfil(),
                dadosUsuario.tipoNativo(),
                dadosUsuario.ativo()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarUsuario(@PathVariable UUID id, @RequestBody AtualizarUsuarioJson json) {
        if (json.endereco() != null) {
            atualizarUsuarioUsecase.atualizarComEndereco(
                    id,
                    json.nome(),
                    json.email(),
                    json.endereco().logradouro(),
                    json.endereco().numero(),
                    json.endereco().complemento(),
                    json.endereco().bairro(),
                    json.endereco().cidade(),
                    json.endereco().cep(),
                    json.endereco().uf()
            );
        } else {
            atualizarUsuarioUsecase.atualizarSemEndereco(id, json.nome(), json.email());
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarUsuario(@PathVariable UUID id) {
        inativarUsuarioUsecase.inativar(id);
        return ResponseEntity.noContent().build();
    }
}