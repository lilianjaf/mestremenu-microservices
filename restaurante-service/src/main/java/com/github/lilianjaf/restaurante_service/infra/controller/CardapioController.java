package com.github.lilianjaf.restaurante_service.infra.controller;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Cardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosAtualizacaoCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosCriacaoCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosCriacaoItemCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.usecase.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cardapios")
public class CardapioController {

    private final CriarCardapioUseCase criarUseCase;
    private final AlterarCardapioUseCase editarUseCase;
    private final DeletarCardapioUseCase deletarUseCase;
    private final BuscarCardapioPorRestauranteUseCase buscarPorRestauranteUseCase;

    public CardapioController(CriarCardapioUseCase criarUseCase,
                              AlterarCardapioUseCase editarUseCase,
                              DeletarCardapioUseCase deletarUseCase,
                              BuscarCardapioPorRestauranteUseCase buscarPorRestauranteUseCase) {
        this.criarUseCase = criarUseCase;
        this.editarUseCase = editarUseCase;
        this.deletarUseCase = deletarUseCase;
        this.buscarPorRestauranteUseCase = buscarPorRestauranteUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> criar(@RequestBody CriarCardapioJson json) {
        List<DadosCriacaoItemCardapio> itens = json.itens().stream()
                .map(item -> new DadosCriacaoItemCardapio(
                        item.nome(),
                        item.descricao(),
                        item.preco(),
                        item.disponibilidadeRestaurante(),
                        item.caminhoFoto(),
                        null
                )).collect(Collectors.toList());

        DadosCriacaoCardapio dados = new DadosCriacaoCardapio(
                json.nome(),
                json.idRestaurante(),
                itens
        );

        Cardapio cardapio = criarUseCase.executar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", cardapio.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardapioResponseJson> editar(@PathVariable UUID id, @RequestBody AtualizarCardapioJson json) {
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(id, json.nome(), null);
        Cardapio cardapio = editarUseCase.executar(dados);
        return ResponseEntity.ok(toResponseJson(cardapio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        deletarUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/restaurante/{idRestaurante}")
    public ResponseEntity<List<CardapioResponseJson>> buscarPorRestaurante(@PathVariable UUID idRestaurante) {
        List<Cardapio> cardapios = buscarPorRestauranteUseCase.executar(idRestaurante);
        List<CardapioResponseJson> response = cardapios.stream()
                .map(this::toResponseJson)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private CardapioResponseJson toResponseJson(Cardapio cardapio) {
        return new CardapioResponseJson(
                cardapio.getId(),
                cardapio.getNome(),
                cardapio.getIdRestaurante()
        );
    }
}
