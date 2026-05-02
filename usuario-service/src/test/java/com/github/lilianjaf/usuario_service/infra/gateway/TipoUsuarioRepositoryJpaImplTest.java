package com.github.lilianjaf.usuario_service.infra.gateway;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.infra.gateway.SpringDataTipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.infra.gateway.TipoUsuarioRepositoryJpaImpl;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.TipoUsuarioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioRepositoryJpaImplTest {

    @Mock
    private SpringDataTipoUsuarioRepository jpaRepository;

    @InjectMocks
    private TipoUsuarioRepositoryJpaImpl repository;

    @Test
    @DisplayName("Deve buscar tipo de usuário por nome")
    void deveBuscarPorNome() {
        
        String nome = "DONO";
        TipoUsuarioEntity entity = new TipoUsuarioEntity(UUID.randomUUID(), nome, TipoNativo.DONO);
        when(jpaRepository.findByNome(nome)).thenReturn(Optional.of(entity));

        
        Optional<TipoUsuario> resultado = repository.findByNome(nome);

        
        assertTrue(resultado.isPresent());
        assertEquals(nome, resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve salvar tipo de usuário")
    void deveSalvarTipoUsuario() {
        
        TipoUsuario domain = new TipoUsuario(UUID.randomUUID(), "CLIENTE", TipoNativo.CLIENTE);
        TipoUsuarioEntity entity = new TipoUsuarioEntity(domain.getId(), domain.getNome(), domain.getTipoNativo());
        when(jpaRepository.save(any(TipoUsuarioEntity.class))).thenReturn(entity);

        
        TipoUsuario resultado = repository.salvar(domain);

        
        assertNotNull(resultado);
        assertEquals(domain.getNome(), resultado.getNome());
        verify(jpaRepository).save(any(TipoUsuarioEntity.class));
    }

    @Test
    @DisplayName("Deve buscar tipo de usuário por ID")
    void deveBuscarPorId() {
        
        UUID id = UUID.randomUUID();
        TipoUsuarioEntity entity = new TipoUsuarioEntity(id, "CLIENTE", TipoNativo.CLIENTE);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        
        Optional<TipoUsuario> resultado = repository.findById(id);

        
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
    }

    @Test
    @DisplayName("Deve deletar tipo de usuário")
    void deveDeletarTipoUsuario() {
        
        TipoUsuario domain = new TipoUsuario(UUID.randomUUID(), "EXTRA", TipoNativo.CLIENTE);

        
        repository.deletar(domain);

        
        verify(jpaRepository).delete(any(TipoUsuarioEntity.class));
    }
}
