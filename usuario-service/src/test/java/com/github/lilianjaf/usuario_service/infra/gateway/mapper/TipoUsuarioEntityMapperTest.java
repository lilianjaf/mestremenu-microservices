package com.github.lilianjaf.usuario_service.infra.gateway.mapper;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.TipoUsuarioEntity;
import com.github.lilianjaf.usuario_service.infra.gateway.mapper.TipoUsuarioEntityMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TipoUsuarioEntityMapperTest {

    @Test
    @DisplayName("Deve converter domínio para entidade corretamente")
    void deveConverterDominioParaEntidadeCorretamente() {
        
        UUID id = UUID.randomUUID();
        TipoUsuario domain = new TipoUsuario(id, "DONO", TipoNativo.DONO);

        
        TipoUsuarioEntity entity = TipoUsuarioEntityMapper.toEntity(domain);

        
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("DONO", entity.getNome());
        assertEquals(TipoNativo.DONO, entity.getTipoNativo());
    }

    @Test
    @DisplayName("Deve retornar null ao converter domínio nulo para entidade")
    void deveRetornarNullAoConverterDominioNulo() {
        
        TipoUsuarioEntity entity = TipoUsuarioEntityMapper.toEntity(null);

        
        assertNull(entity);
    }

    @Test
    @DisplayName("Deve converter entidade para domínio corretamente")
    void deveConverterEntidadeParaDominioCorretamente() {
        
        UUID id = UUID.randomUUID();
        TipoUsuarioEntity entity = new TipoUsuarioEntity(id, "CLIENTE", TipoNativo.CLIENTE);

        
        TipoUsuario domain = TipoUsuarioEntityMapper.toDomain(entity);

        
        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("CLIENTE", domain.getNome());
        assertEquals(TipoNativo.CLIENTE, domain.getTipoNativo());
    }

    @Test
    @DisplayName("Deve retornar null ao converter entidade nula para domínio")
    void deveRetornarNullAoConverterEntidadeNula() {
        
        TipoUsuario domain = TipoUsuarioEntityMapper.toDomain(null);

        
        assertNull(domain);
    }
}
