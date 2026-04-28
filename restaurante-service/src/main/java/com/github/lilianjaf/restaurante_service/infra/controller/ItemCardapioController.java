package com.github.lilianjaf.restaurante_service.infra.controller;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.usecase.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/itens-cardapio")
public class ItemCardapioController {

    private final CriarItemCardapioUseCase criarUseCase;
    private final AlterarItemCardapioUseCase editarUseCase;
    private final DeletarItemCardapioUseCase deletarUseCase;
    private final BuscarItemCardapioPorIdUseCase buscarUseCase;

    public ItemCardapioController(CriarItemCardapioUseCase criarUseCase,
                                 AlterarItemCardapioUseCase editarUseCase,
                                 DeletarItemCardapioUseCase deletarUseCase,
                                 BuscarItemCardapioPorIdUseCase buscarUseCase) {
        this.criarUseCase = criarUseCase;
        this.editarUseCase = editarUseCase;
        this.deletarUseCase = deletarUseCase;
        this.buscarUseCase = buscarUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> criar(@RequestBody CriarItemCardapioJson json) {
        DadosCriacaoItemCardapio dados = new DadosCriacaoItemCardapio(
                json.nome(),
                json.descricao(),
                json.preco(),
                json.disponibilidadeRestaurante(),
                json.caminhoFoto(),
                json.idCardapio()
        );
        ItemCardapio item = criarUseCase.executar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", item.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemCardapioResponseJson> editar(@PathVariable UUID id, @RequestBody AtualizarItemCardapioJson json) {
        DadosAtualizacaoItemCardapio dados = new DadosAtualizacaoItemCardapio(
                id,
                json.nome(),
                json.descricao(),
                json.preco(),
                json.disponibilidadeRestaurante(),
                json.caminhoFoto()
        );
        ItemCardapio item = editarUseCase.executar(dados);
        return ResponseEntity.ok(toResponseJson(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        deletarUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemCardapioResponseJson> buscar(@PathVariable UUID id) {
        ItemCardapio item = buscarUseCase.executar(id);
        return ResponseEntity.ok(toResponseJson(item));
    }

    private ItemCardapioResponseJson toResponseJson(ItemCardapio item) {
        return new ItemCardapioResponseJson(
                item.getId(),
                item.getNome(),
                item.getDescricao(),
                item.getPreco(),
                item.isDisponibilidadeRestaurante(),
                item.getCaminhoFoto(),
                item.getIdCardapio()
        );
    }
}
