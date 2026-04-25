package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.RestauranteRepository;
import com.github.lilianjaf.mestremenuclean.restaurante.infra.gateway.entity.RestauranteEntity;
import com.github.lilianjaf.mestremenuclean.restaurante.infra.gateway.mapper.RestauranteEntityMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RestauranteRepositoryJpaImpl implements RestauranteRepository {

    private final SpringDataRestauranteRepository springDataRepository;

    public RestauranteRepositoryJpaImpl(SpringDataRestauranteRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Transactional
    @Override
    public Restaurante salvar(Restaurante restauranteDomain) {
        RestauranteEntity entity;

        if (restauranteDomain.getId() != null && springDataRepository.existsById(restauranteDomain.getId())) {
            entity = springDataRepository.findById(restauranteDomain.getId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado para atualização"));

            RestauranteEntityMapper.atualizarEntity(restauranteDomain, entity);

            RestauranteEntity entitySalva = springDataRepository.save(entity);
            return RestauranteEntityMapper.toDomain(entitySalva);

        } else {
            entity = RestauranteEntityMapper.toEntity(restauranteDomain);
            springDataRepository.save(entity);

            return restauranteDomain;
        }
    }

    @Override
    public Optional<Restaurante> findById(UUID id) {
        return springDataRepository.findById(id).map(RestauranteEntityMapper::toDomain);
    }

    @Override
    public List<Restaurante> buscarTodos() {
        return springDataRepository.findAll().stream()
                .map(RestauranteEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
