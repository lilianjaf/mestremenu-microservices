package com.github.lilianjaf.restaurante_service.core.gateway;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemCardapioRepository {
    ItemCardapio salvar(ItemCardapio item);
    Optional<ItemCardapio> findById(UUID id);
    List<ItemCardapio> buscarPorIdCardapio(UUID idCardapio);
    boolean existeNomeNoCardapio(String nome, UUID idCardapio);
    boolean existeNomeNoCardapioExcetoId(String nome, UUID idCardapio, UUID idItem);
    void deletar(UUID id);
}
