package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.restaurante_service.infra.gateway.entity.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataRestauranteRepository extends JpaRepository<RestauranteEntity, UUID> {
}
