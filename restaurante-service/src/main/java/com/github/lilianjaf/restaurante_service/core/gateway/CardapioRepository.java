package com.github.lilianjaf.restaurante_service.core.gateway;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardapioRepository {
    Cardapio salvar(Cardapio cardapio);
    Optional<Cardapio> findById(UUID id);
    List<Cardapio> buscarPorIdRestaurante(UUID idRestaurante);
    boolean existeNomeParaRestaurante(String nome, UUID idRestaurante);
    void deletar(UUID id);
}
