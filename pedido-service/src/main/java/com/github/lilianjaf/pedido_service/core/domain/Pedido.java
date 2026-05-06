package com.github.lilianjaf.pedido_service.core.domain;

import java.util.List;
import java.util.UUID;

public class Pedido {

    /*
    • Dados do cliente (ID extraído do token JWT).
• Dados do restaurante.
• Lista de itens, contendo:
‣ id do produto
‣ nome
‣ quantidade
‣ preço

     */
    private UUID id;
    private Usuario cliente;
    private Restaurante restaurante;
    private List<ItemPedido> itens;

}
