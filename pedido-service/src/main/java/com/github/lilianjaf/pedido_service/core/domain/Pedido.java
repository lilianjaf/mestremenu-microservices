package com.github.lilianjaf.pedido_service.core.domain;

import java.util.List;
import java.util.UUID;

public class Pedido {

    private UUID id;
    private Usuario cliente;
    private Restaurante restaurante;
    private List<ItemPedido> itens;

}
