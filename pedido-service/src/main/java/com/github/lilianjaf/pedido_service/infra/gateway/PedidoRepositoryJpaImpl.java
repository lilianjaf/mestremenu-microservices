package com.github.lilianjaf.pedido_service.infra.gateway;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.infra.gateway.entity.PedidoEntity;
import com.github.lilianjaf.pedido_service.infra.gateway.mapper.PedidoEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class PedidoRepositoryJpaImpl implements PedidoRepository {

    private final SpringDataPedidoRepository springDataRepository;

    public PedidoRepositoryJpaImpl(SpringDataPedidoRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Pedido salvar(Pedido domain) {
        if (domain.getId() != null && springDataRepository.existsById(domain.getId())) {
            PedidoEntity entity = springDataRepository.findById(domain.getId())
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado para atualização"));
            entity.setStatus(domain.getStatus());
            PedidoEntity saved = springDataRepository.save(entity);
            return PedidoEntityMapper.toDomain(saved);
        }

        PedidoEntity entity = PedidoEntityMapper.toEntity(domain);
        springDataRepository.save(entity);
        return domain;
    }

    @Override
    public Optional<Pedido> buscarPorId(UUID id) {
        return springDataRepository.findById(id).map(PedidoEntityMapper::toDomain);
    }

    @Override
    public List<Pedido> buscarPorClienteId(UUID clienteId) {
        return springDataRepository.findByClienteId(clienteId).stream()
                .map(PedidoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
