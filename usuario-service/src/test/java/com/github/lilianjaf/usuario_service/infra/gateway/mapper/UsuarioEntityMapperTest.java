package com.github.lilianjaf.usuario_service.infra.gateway.mapper;

import com.github.lilianjaf.usuario_service.core.domain.*;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.EnderecoEmbeddable;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.TipoUsuarioEntity;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.UsuarioEntity;
import com.github.lilianjaf.usuario_service.infra.gateway.mapper.UsuarioEntityMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioEntityMapperTest {

    @Test
    @DisplayName("Deve converter domínio para entidade corretamente")
    void deveConverterDominioParaEntidadeCorretamente() {
        
        UUID id = UUID.randomUUID();
        String nome = "João Silva";
        String email = "joao@email.com";
        String login = "joaosilva";
        String senha = "senha123";
        LocalDateTime data = LocalDateTime.now();
        Endereco endereco = new Endereco("Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345-678", "UF");
        TipoUsuario tipo = new TipoUsuario(UUID.randomUUID(), "CLIENTE", TipoNativo.CLIENTE);
        Cliente cliente = new Cliente(id, nome, email, login, senha, tipo, endereco, data, true);

        
        UsuarioEntity entity = UsuarioEntityMapper.toEntity(cliente);

        
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(nome, entity.getNome());
        assertEquals(email, entity.getEmail());
        assertEquals(login, entity.getLogin());
        assertEquals(senha, entity.getSenha());
        assertEquals(data, entity.getDataUltimaAlteracao());
        assertTrue(entity.getAtivo());
        assertEquals(endereco.logradouro(), entity.getEndereco().getLogradouro());
        assertEquals(tipo.getId(), entity.getTipoCustomizado().getId());
    }

    @Test
    @DisplayName("Deve retornar null ao converter domínio nulo para entidade")
    void deveRetornarNullAoConverterDominioNulo() {
        
        UsuarioEntity entity = UsuarioEntityMapper.toEntity(null);

        
        assertNull(entity);
    }

    @Test
    @DisplayName("Deve converter entidade para domínio de Cliente corretamente")
    void deveConverterEntidadeParaClienteCorretamente() {
        
        UsuarioEntity entity = criarUsuarioEntity(TipoNativo.CLIENTE);

        
        UsuarioBase domain = UsuarioEntityMapper.toDomain(entity);

        
        assertNotNull(domain);
        assertTrue(domain instanceof Cliente);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getNome(), domain.getNome());
        assertEquals(TipoNativo.CLIENTE, domain.getTipoCustomizado().getTipoNativo());
    }

    @Test
    @DisplayName("Deve converter entidade para domínio de Dono corretamente")
    void deveConverterEntidadeParaDonoCorretamente() {
        
        UsuarioEntity entity = criarUsuarioEntity(TipoNativo.DONO);

        
        UsuarioBase domain = UsuarioEntityMapper.toDomain(entity);

        
        assertNotNull(domain);
        assertTrue(domain instanceof Dono);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(TipoNativo.DONO, domain.getTipoCustomizado().getTipoNativo());
    }

    @Test
    @DisplayName("Deve atualizar entidade a partir do domínio corretamente")
    void deveAtualizarEntidadeCorretamente() {
        
        UsuarioEntity entity = criarUsuarioEntity(TipoNativo.CLIENTE);
        String novoNome = "Nome Atualizado";
        Endereco novoEndereco = new Endereco("Nova Rua", "456", null, "Novo Bairro", "Nova Cidade", "87654-321", "NX");
        TipoUsuario tipo = new TipoUsuario(entity.getTipoCustomizado().getId(), "NOVO_TIPO", TipoNativo.CLIENTE);
        Cliente domain = new Cliente(entity.getId(), novoNome, entity.getEmail(), entity.getLogin(), entity.getSenha(), tipo, novoEndereco, entity.getDataUltimaAlteracao(), false);

        
        UsuarioEntityMapper.atualizarEntity(domain, entity);

        
        assertEquals(novoNome, entity.getNome());
        assertFalse(entity.getAtivo());
        assertEquals("Nova Rua", entity.getEndereco().getLogradouro());
        assertEquals("NOVO_TIPO", entity.getTipoCustomizado().getNome());
    }

    private UsuarioEntity criarUsuarioEntity(TipoNativo tipoNativo) {
        EnderecoEmbeddable endereco =
                new EnderecoEmbeddable("Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345-678", "UF");
        TipoUsuarioEntity tipo =
                new TipoUsuarioEntity(UUID.randomUUID(), tipoNativo.name(), tipoNativo);

        return new UsuarioEntity(UUID.randomUUID(), "Teste", "teste@email.com", "teste", "senha", tipo, endereco, LocalDateTime.now(), true);
    }
}
