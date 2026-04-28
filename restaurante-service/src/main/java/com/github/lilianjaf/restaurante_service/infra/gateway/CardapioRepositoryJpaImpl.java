package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.gateway.CardapioRepository;
import com.github.lilianjaf.restaurante_service.infra.gateway.entity.CardapioEntity;
import com.github.lilianjaf.restaurante_service.infra.gateway.mapper.CardapioEntityMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CardapioRepositoryJpaImpl implements CardapioRepository {

    private final SpringDataCardapioRepository springDataRepository;

    public CardapioRepositoryJpaImpl(SpringDataCardapioRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Transactional
    @Override
    public Cardapio salvar(Cardapio cardapioDomain) {
        CardapioEntity entity;

        if (cardapioDomain.getId() != null && springDataRepository.existsById(cardapioDomain.getId())) {
            entity = springDataRepository.findById(cardapioDomain.getId())
                    .orElseThrow(() -> new CardapioException("Cardápio não encontrado para atualização"));

            CardapioEntityMapper.atualizarEntity(cardapioDomain, entity);

            CardapioEntity entitySalva = springDataRepository.save(entity);
            return CardapioEntityMapper.toDomain(entitySalva);

        } else {
            entity = CardapioEntityMapper.toEntity(cardapioDomain);
            springDataRepository.save(entity);

            return cardapioDomain;
        }
    }

    @Override
    public Optional<Cardapio> findById(UUID id) {
        return springDataRepository.findById(id).map(CardapioEntityMapper::toDomain);
    }

    @Override
    public List<Cardapio> buscarPorIdRestaurante(UUID idRestaurante) {
        return springDataRepository.findAllByIdRestaurante(idRestaurante).stream()
                .map(CardapioEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeNomeParaRestaurante(String nome, UUID idRestaurante) {
        return springDataRepository.existsByNomeAndIdRestaurante(nome, idRestaurante);
    }

    @Override
    public void deletar(UUID id) {
        springDataRepository.deleteById(id);
    }
}
