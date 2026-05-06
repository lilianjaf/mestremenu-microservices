package com.github.lilianjaf.pedido_service.core.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemPedido {
    private UUID id;
    private String descricao;
    private int quantidade;
    private BigDecimal preco;
}
