package com.github.lilianjaf.pedido_service.core.domain;

import com.github.lilianjaf.pedido_service.core.exception.DomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Pedido {

    private UUID id;
    private UUID clienteId;
    private UUID restauranteId;
    private List<ItemPedido> itens;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private LocalDateTime dataCriacao;

    public Pedido(UUID clienteId, UUID restauranteId, List<ItemPedido> itens) {
        this.id = UUID.randomUUID();
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.itens = itens;
        this.valorTotal = calcularTotal(itens);
        this.status = StatusPedido.RASCUNHO;
        this.dataCriacao = LocalDateTime.now();
    }

    public Pedido(UUID id, UUID clienteId, UUID restauranteId, List<ItemPedido> itens,
                  BigDecimal valorTotal, StatusPedido status, LocalDateTime dataCriacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.itens = itens;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    public void confirmar() {
        if (this.status != StatusPedido.RASCUNHO) {
            throw new DomainException("Apenas pedidos em rascunho podem ser confirmados.");
        }
        this.status = StatusPedido.CRIADO;
    }

    public void atualizarStatus(StatusPedido novoStatus) {
        boolean transicaoValida = switch (novoStatus) {
            case PAGO -> this.status == StatusPedido.CRIADO || this.status == StatusPedido.PENDENTE_PAGAMENTO;
            case PENDENTE_PAGAMENTO -> this.status == StatusPedido.CRIADO;
            case CANCELADO -> this.status == StatusPedido.CRIADO || this.status == StatusPedido.PENDENTE_PAGAMENTO;
            default -> false;
        };
        if (!transicaoValida) {
            throw new DomainException(
                "Transição de status inválida: " + this.status + " → " + novoStatus);
        }
        this.status = novoStatus;
    }

    private static BigDecimal calcularTotal(List<ItemPedido> itens) {
        return itens.stream()
                .map(ItemPedido::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() { return id; }
    public UUID getClienteId() { return clienteId; }
    public UUID getRestauranteId() { return restauranteId; }
    public List<ItemPedido> getItens() { return itens; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public StatusPedido getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
}
