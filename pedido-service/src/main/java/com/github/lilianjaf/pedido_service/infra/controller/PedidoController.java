package com.github.lilianjaf.pedido_service.infra.controller;

import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoItemPedido;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoPedido;
import com.github.lilianjaf.pedido_service.core.usecase.ConfirmarPedidoUseCase;
import com.github.lilianjaf.pedido_service.core.usecase.ConsultarPedidoPorClienteUseCase;
import com.github.lilianjaf.pedido_service.core.usecase.ConsultarPedidoUseCase;
import com.github.lilianjaf.pedido_service.core.usecase.CriarPedidoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final CriarPedidoUseCase criarPedidoUseCase;
    private final ConfirmarPedidoUseCase confirmarPedidoUseCase;
    private final ConsultarPedidoUseCase consultarPedidoUseCase;
    private final ConsultarPedidoPorClienteUseCase consultarPedidoPorClienteUseCase;

    public PedidoController(CriarPedidoUseCase criarPedidoUseCase,
                            ConfirmarPedidoUseCase confirmarPedidoUseCase,
                            ConsultarPedidoUseCase consultarPedidoUseCase,
                            ConsultarPedidoPorClienteUseCase consultarPedidoPorClienteUseCase) {
        this.criarPedidoUseCase = criarPedidoUseCase;
        this.confirmarPedidoUseCase = confirmarPedidoUseCase;
        this.consultarPedidoUseCase = consultarPedidoUseCase;
        this.consultarPedidoPorClienteUseCase = consultarPedidoPorClienteUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> criarPedido(@RequestBody CriarPedidoJson json) {
        List<DadosCriacaoItemPedido> itens = json.itens().stream()
                .map(i -> new DadosCriacaoItemPedido(i.descricao(), i.quantidade(), i.preco()))
                .collect(Collectors.toList());

        DadosCriacaoPedido dados = new DadosCriacaoPedido(json.restauranteId(), itens);
        Pedido pedido = criarPedidoUseCase.executar(dados);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", pedido.getId(), "valorTotal", pedido.getValorTotal()));
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<PedidoResponseJson> confirmarPedido(@PathVariable UUID id) {
        Pedido pedido = confirmarPedidoUseCase.executar(id);
        return ResponseEntity.ok(toResponseJson(pedido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseJson> consultarPedido(@PathVariable UUID id) {
        Pedido pedido = consultarPedidoUseCase.executar(id);
        return ResponseEntity.ok(toResponseJson(pedido));
    }

    @GetMapping("/meus-pedidos")
    public ResponseEntity<List<PedidoResponseJson>> listarMeusPedidos() {
        List<Pedido> pedidos = consultarPedidoPorClienteUseCase.executar();
        List<PedidoResponseJson> response = pedidos.stream()
                .map(this::toResponseJson)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private PedidoResponseJson toResponseJson(Pedido pedido) {
        List<ItemPedidoResponseJson> itensResponse = pedido.getItens().stream()
                .map(i -> new ItemPedidoResponseJson(
                        i.getId(), i.getDescricao(), i.getQuantidade(), i.getPreco(), i.subtotal()))
                .collect(Collectors.toList());

        return new PedidoResponseJson(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getRestauranteId(),
                itensResponse,
                pedido.getValorTotal(),
                pedido.getStatus().name(),
                pedido.getDataCriacao()
        );
    }
}
