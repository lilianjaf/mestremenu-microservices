package com.github.lilianjaf.pagamento_service.infra.gateway;

import com.github.lilianjaf.pagamento_service.core.domain.Pagamento;
import com.github.lilianjaf.pagamento_service.core.domain.StatusPagamento;
import com.github.lilianjaf.pagamento_service.core.gateway.PagamentoRepository;
import com.github.lilianjaf.pagamento_service.infra.gateway.entity.PagamentoEntity;
import com.github.lilianjaf.pagamento_service.infra.gateway.mapper.PagamentoEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class PagamentoRepositoryJpaImpl implements PagamentoRepository {

    private final SpringDataPagamentoRepository springDataRepository;

    public PagamentoRepositoryJpaImpl(SpringDataPagamentoRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Pagamento salvar(Pagamento domain) {
        Optional<PagamentoEntity> existing = springDataRepository.findByPedidoId(domain.getPedidoId());
        if (existing.isPresent()) {
            PagamentoEntity entity = existing.get();
            entity.setStatus(domain.getStatus());
            entity.setTentativas(domain.getTentativas());
            entity.setProcessadoEm(domain.getProcessadoEm());
            return PagamentoEntityMapper.toDomain(springDataRepository.save(entity));
        }
        PagamentoEntity entity = PagamentoEntityMapper.toEntity(domain);
        return PagamentoEntityMapper.toDomain(springDataRepository.save(entity));
    }

    @Override
    public Optional<Pagamento> buscarPorPedidoId(UUID pedidoId) {
        return springDataRepository.findByPedidoId(pedidoId)
                .map(PagamentoEntityMapper::toDomain);
    }

    @Override
    public List<Pagamento> buscarPorStatus(StatusPagamento status) {
        return springDataRepository.findByStatus(status).stream()
                .map(PagamentoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
