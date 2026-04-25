package com.github.lilianjaf.restaurante_service.infra.controller;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Endereco;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.dto.DadosAtualizacaoRestaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.dto.DadosCriacaoRestaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.usecase.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/restaurantes")
public class RestauranteController {

    private final CriarRestauranteUseCase criarRestauranteUseCase;
    private final BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase;
    private final ListarRestaurantesUseCase listarRestaurantesUseCase;
    private final AtualizarRestauranteUseCase atualizarRestauranteUseCase;
    private final InativarRestauranteUseCase inativarRestauranteUseCase;

    public RestauranteController(CriarRestauranteUseCase criarRestauranteUseCase,
                                 BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase,
                                 ListarRestaurantesUseCase listarRestaurantesUseCase,
                                 AtualizarRestauranteUseCase atualizarRestauranteUseCase,
                                 InativarRestauranteUseCase inativarRestauranteUseCase) {
        this.criarRestauranteUseCase = criarRestauranteUseCase;
        this.buscarRestaurantePorIdUseCase = buscarRestaurantePorIdUseCase;
        this.listarRestaurantesUseCase = listarRestaurantesUseCase;
        this.atualizarRestauranteUseCase = atualizarRestauranteUseCase;
        this.inativarRestauranteUseCase = inativarRestauranteUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> criarRestaurante(@RequestBody CriarRestauranteJson json) {
        Endereco enderecoDomain = null;
        if (json.endereco() != null) {
            enderecoDomain = new Endereco(
                    json.endereco().logradouro(),
                    json.endereco().numero(),
                    json.endereco().complemento(),
                    json.endereco().bairro(),
                    json.endereco().cidade(),
                    json.endereco().cep(),
                    json.endereco().uf()
            );
        }

        DadosCriacaoRestaurante dados = new DadosCriacaoRestaurante(
                json.nome(),
                enderecoDomain,
                json.tipoCozinha(),
                json.horarioFuncionamento(),
                json.idDono()
        );

        Restaurante restauranteCriado = criarRestauranteUseCase.executar(dados);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", restauranteCriado.getId()));
    }

    @GetMapping
    public ResponseEntity<List<RestauranteResponseJson>> listarRestaurantes() {
        List<Restaurante> restaurantes = listarRestaurantesUseCase.executar();
        List<RestauranteResponseJson> response = restaurantes.stream()
                .map(this::toResponseJson)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseJson> buscarRestaurante(@PathVariable UUID id) {
        Restaurante restaurante = buscarRestaurantePorIdUseCase.executar(id);
        return ResponseEntity.ok(toResponseJson(restaurante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponseJson> atualizarRestaurante(@PathVariable UUID id, @RequestBody AtualizarRestauranteJson json) {
        Endereco enderecoDomain = null;
        if (json.endereco() != null) {
            enderecoDomain = new Endereco(
                    json.endereco().logradouro(),
                    json.endereco().numero(),
                    json.endereco().complemento(),
                    json.endereco().bairro(),
                    json.endereco().cidade(),
                    json.endereco().cep(),
                    json.endereco().uf()
            );
        }

        DadosAtualizacaoRestaurante dados = new DadosAtualizacaoRestaurante(
                json.nome(),
                enderecoDomain,
                json.tipoCozinha(),
                json.horarioFuncionamento()
        );

        Restaurante restauranteAtualizado = atualizarRestauranteUseCase.executar(id, dados);
        return ResponseEntity.ok(toResponseJson(restauranteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarRestaurante(@PathVariable UUID id) {
        inativarRestauranteUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    private RestauranteResponseJson toResponseJson(Restaurante restaurante) {
        return new RestauranteResponseJson(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getTipoCozinha(),
                restaurante.getHorarioFuncionamento(),
                restaurante.getIdDono()
        );
    }
}
