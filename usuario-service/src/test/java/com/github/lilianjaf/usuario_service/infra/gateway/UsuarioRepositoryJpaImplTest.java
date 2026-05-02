package com.github.lilianjaf.usuario_service.infra.gateway;

import com.github.lilianjaf.usuario_service.core.domain.*;
import com.github.lilianjaf.usuario_service.infra.gateway.SpringDataTipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.infra.gateway.SpringDataUsuarioRepository;
import com.github.lilianjaf.usuario_service.infra.gateway.UsuarioRepositoryJpaImpl;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.EnderecoEmbeddable;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.TipoUsuarioEntity;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.UsuarioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryJpaImplTest {

    @Mock
    private SpringDataUsuarioRepository jpaRepository;

    @Mock
    private SpringDataTipoUsuarioRepository jpaTipoUsuarioRepository;

    @InjectMocks
    private UsuarioRepositoryJpaImpl repository;

    @Test
    @DisplayName("Deve salvar novo usuário corretamente")
    void deveSalvarNovoUsuarioCorretamente() {
        
        UUID tipoId = UUID.randomUUID();
        TipoUsuario tipo = new TipoUsuario(tipoId, "CLIENTE", TipoNativo.CLIENTE);
        Endereco endereco = new Endereco("Rua A", "123", null, "Bairro X", "Cidade Y", "12345678", "UF");
        UsuarioBase domain = new Cliente(UUID.randomUUID(), "Nome", "email@email.com", "login", "senha", tipo, endereco, LocalDateTime.now(), true);

        TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity(tipoId, "CLIENTE", TipoNativo.CLIENTE);
        when(jpaTipoUsuarioRepository.getReferenceById(tipoId)).thenReturn(tipoEntity);
        when(jpaRepository.existsById(any())).thenReturn(false);

        
        UsuarioBase resultado = repository.salvar(domain);

        
        assertNotNull(resultado);
        verify(jpaRepository).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("Deve atualizar usuário existente corretamente")
    void deveAtualizarUsuarioExistenteCorretamente() {
        
        UUID id = UUID.randomUUID();
        UUID tipoId = UUID.randomUUID();
        TipoUsuario tipo = new TipoUsuario(tipoId, "CLIENTE", TipoNativo.CLIENTE);
        Endereco endereco = new Endereco("Rua A", "123", null, "Bairro X", "Cidade Y", "12345678", "UF");
        UsuarioBase domain = new Cliente(id, "Nome Novo", "email@email.com", "login", "senha", tipo, endereco, LocalDateTime.now(), true);

        TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity(tipoId, "CLIENTE", TipoNativo.CLIENTE);
        UsuarioEntity entityExistente = new UsuarioEntity(id, "Nome Antigo", "email@email.com", "login", "senha", tipoEntity, 
                new EnderecoEmbeddable("Rua A", "123", null, "Bairro X", "Cidade Y", "12345678", "UF"), LocalDateTime.now(), true);

        when(jpaRepository.existsById(id)).thenReturn(true);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entityExistente));
        when(jpaTipoUsuarioRepository.getReferenceById(tipoId)).thenReturn(tipoEntity);
        when(jpaRepository.save(any(UsuarioEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        
        UsuarioBase resultado = repository.salvar(domain);

        
        assertNotNull(resultado);
        assertEquals("Nome Novo", resultado.getNome());
        verify(jpaRepository).save(entityExistente);
    }

    @Test
    @DisplayName("Deve buscar por login corretamente")
    void deveBuscarPorLoginCorretamente() {
        
        String login = "user123";
        UsuarioEntity entity = criarUsuarioEntity(TipoNativo.CLIENTE);
        when(jpaRepository.findByLogin(login)).thenReturn(Optional.of(entity));

        
        Optional<UsuarioBase> resultado = repository.findByLogin(login);

        
        assertTrue(resultado.isPresent());
        assertEquals(entity.getLogin(), resultado.get().getLogin());
    }

    @Test
    @DisplayName("Deve buscar por id corretamente")
    void deveBuscarPorIdCorretamente() {
        
        UUID id = UUID.randomUUID();
        UsuarioEntity entity = criarUsuarioEntity(TipoNativo.CLIENTE);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        
        Optional<UsuarioBase> resultado = repository.findById(id);

        
        assertTrue(resultado.isPresent());
        assertEquals(entity.getId(), resultado.get().getId());
    }

    @Test
    @DisplayName("Deve verificar se existe usuário com tipo")
    void deveVerificarSeExisteUsuarioComTipo() {
        
        UUID tipoId = UUID.randomUUID();
        when(jpaRepository.existsByTipoCustomizadoId(tipoId)).thenReturn(true);

        
        boolean existe = repository.existeUsuarioComTipo(tipoId);

        
        assertTrue(existe);
        verify(jpaRepository).existsByTipoCustomizadoId(tipoId);
    }

    private UsuarioEntity criarUsuarioEntity(TipoNativo tipoNativo) {
        EnderecoEmbeddable endereco = new EnderecoEmbeddable("Rua A", "123", null, "Bairro X", "Cidade Y", "12345678", "UF");
        TipoUsuarioEntity tipo = new TipoUsuarioEntity(UUID.randomUUID(), tipoNativo.name(), tipoNativo);
        return new UsuarioEntity(UUID.randomUUID(), "Teste", "teste@email.com", "teste", "senha", tipo, endereco, LocalDateTime.now(), true);
    }
}
