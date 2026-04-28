package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.gateway.ItemCardapioRepository;
import com.github.lilianjaf.restaurante_service.infra.gateway.entity.CardapioEntity;
import com.github.lilianjaf.restaurante_service.infra.gateway.entity.ItemCardapioEntity;
import com.github.lilianjaf.restaurante_service.infra.gateway.mapper.ItemCardapioEntityMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ItemCardapioRepositoryJpaImpl implements ItemCardapioRepository {

    private final SpringDataItemCardapioRepository springDataRepository;
    private final SpringDataCardapioRepository springDataCardapioRepository;

    public ItemCardapioRepositoryJpaImpl(SpringDataItemCardapioRepository springDataRepository,
                                         SpringDataCardapioRepository springDataCardapioRepository) {
        this.springDataRepository = springDataRepository;
        this.springDataCardapioRepository = springDataCardapioRepository;
    }

    @Transactional
    @Override
    public ItemCardapio salvar(ItemCardapio item) {
        ItemCardapioEntity entity;

        if (item.getId() != null && springDataRepository.existsById(item.getId())) {
            entity = springDataRepository.findById(item.getId())
                    .orElseThrow(() -> new CardapioException("Item do cardápio não encontrado para atualização."));

            ItemCardapioEntityMapper.atualizarEntity(item, entity);

            ItemCardapioEntity savedEntity = springDataRepository.save(entity);
            return ItemCardapioEntityMapper.toDomain(savedEntity);

        } else {
            CardapioEntity cardapioEntity = springDataCardapioRepository.findById(item.getIdCardapio())
                    .orElseThrow(() -> new CardapioException("Cardápio não encontrado para persistência do item."));

            entity = ItemCardapioEntityMapper.toEntity(item, cardapioEntity);
            springDataRepository.save(entity);

            return item;
        }
    }

    @Override
    public Optional<ItemCardapio> findById(UUID id) {
        return springDataRepository.findById(id).map(ItemCardapioEntityMapper::toDomain);
    }

    @Override
    public List<ItemCardapio> buscarPorIdCardapio(UUID idCardapio) {
        return springDataRepository.findAllByCardapioId(idCardapio).stream()
                .map(ItemCardapioEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeNomeNoCardapio(String nome, UUID idCardapio) {
        return springDataRepository.existsByNomeAndCardapioId(nome, idCardapio);
    }

    @Override
    public boolean existeNomeNoCardapioExcetoId(String nome, UUID idCardapio, UUID idItem) {
        return springDataRepository.existsByNomeAndCardapioIdAndIdNot(nome, idCardapio, idItem);
    }

    @Override
    public void deletar(UUID id) {
        springDataRepository.deleteById(id);
    }
}
