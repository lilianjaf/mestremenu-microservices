package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.gateway.CardapioRepository;

public class NomeCardapioNaoPodeSerDuplicadoRule implements ValidadorCardapioRule<CriacaoCardapioContext> {

    private final CardapioRepository cardapioRepository;

    public NomeCardapioNaoPodeSerDuplicadoRule(CardapioRepository cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }

    @Override
    public void validar(CriacaoCardapioContext context) {
        if (cardapioRepository.existeNomeParaRestaurante(context.dados().nome(), context.dados().idRestaurante())) {
            throw new CardapioException("Já existe um cardápio com este nome para este restaurante.");
        }
    }

    public void validar(AtualizacaoCardapioContext context) {
        if (!context.cardapioExistente().getNome().equalsIgnoreCase(context.dados().nome()) &&
            cardapioRepository.existeNomeParaRestaurante(context.dados().nome(), context.cardapioExistente().getIdRestaurante())) {
            throw new CardapioException("Já existe um cardápio com este nome para este restaurante.");
        }
    }
}
