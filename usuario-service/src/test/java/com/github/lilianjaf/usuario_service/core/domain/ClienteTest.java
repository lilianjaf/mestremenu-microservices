package com.github.lilianjaf.usuario_service.core.domain;

import com.github.lilianjaf.usuario_service.core.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Endereco criarEnderecoValido() {
        return new Endereco("Rua", "123", null, "Bairro", "Cidade", "00000-000", "UF");
    }

    private TipoUsuario criarTipoUsuarioValido() {
        return new TipoUsuario("Standard", TipoNativo.CLIENTE);
    }

    @Test
    @DisplayName("Deve criar um cliente válido")
    void deveCriarClienteValido() {
        String nome = "João Silva";
        String email = "joao@email.com";
        String login = "joao.silva";
        String senha = "senha123";
        TipoUsuario tipo = criarTipoUsuarioValido();
        Endereco endereco = criarEnderecoValido();

        Cliente cliente = assertDoesNotThrow(() -> 
            new Cliente(nome, email, login, senha, tipo, endereco)
        );

        assertNotNull(cliente.getId());
        assertEquals(nome, cliente.getNome());
        assertEquals(email, cliente.getEmail());
        assertEquals(login, cliente.getLogin());
        assertEquals(senha, cliente.getSenha());
        assertEquals(tipo, cliente.getTipoCustomizado());
        assertEquals(endereco, cliente.getEndereco());
        assertTrue(cliente.getAtivo());
        assertNotNull(cliente.getDataUltimaAlteracao());
    }

    @Test
    @DisplayName("Deve criar um cliente com ID e outros dados específicos")
    void deveCriarClienteComId() {
        UUID id = UUID.randomUUID();
        LocalDateTime data = LocalDateTime.now().minusDays(1);
        Cliente cliente = new Cliente(id, "Nome", "email@test.com", "login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido(), data, false);

        assertEquals(id, cliente.getId());
        assertEquals(data, cliente.getDataUltimaAlteracao());
        assertFalse(cliente.getAtivo());
    }

    @Test
    @DisplayName("Deve desativar um cliente ativo")
    void deveDesativarCliente() {
        Cliente cliente = new Cliente("Nome", "email@test.com", "login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido());
        LocalDateTime dataAntes = cliente.getDataUltimaAlteracao();

        cliente.desativar();

        assertFalse(cliente.getAtivo());
        assertTrue(cliente.getDataUltimaAlteracao().isAfter(dataAntes) || cliente.getDataUltimaAlteracao().isEqual(dataAntes));
    }

    @Test
    @DisplayName("Deve lançar exceção ao desativar um cliente já inativo")
    void deveLancarExcecaoAoDesativarClienteInativo() {
        Cliente cliente = new Cliente(UUID.randomUUID(), "Nome", "email@test.com", "login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido(), LocalDateTime.now(), false);

        assertThrows(DomainException.class, cliente::desativar);
    }

    @Test
    @DisplayName("Deve atualizar o endereço do cliente")
    void deveAtualizarEndereco() {
        Cliente cliente = new Cliente("Nome", "email@test.com", "login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido());
        Endereco novoEndereco = new Endereco("Nova Rua", "456", null, "Novo Bairro", "Nova Cidade", "11111-111", "XX");

        cliente.atualizarEndereco(novoEndereco);

        assertEquals(novoEndereco, cliente.getEndereco());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar endereço para nulo")
    void deveLancarExcecaoAoAtualizarEnderecoNulo() {
        Cliente cliente = new Cliente("Nome", "email@test.com", "login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido());

        assertThrows(DomainException.class, () -> cliente.atualizarEndereco(null));
    }

    @Test
    @DisplayName("Deve atualizar dados básicos do cliente")
    void deveAtualizarDadosBasicos() {
        Cliente cliente = new Cliente("Nome", "email@test.com", "login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido());
        String novoNome = "Novo Nome";
        String novoEmail = "novo@email.com";

        cliente.atualizarDadosBasicos(novoNome, novoEmail);

        assertEquals(novoNome, cliente.getNome());
        assertEquals(novoEmail, cliente.getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar dados básicos com nome inválido")
    void deveLancarExcecaoAoAtualizarDadosBasicosNomeInvalido() {
        Cliente cliente = new Cliente("Nome", "email@test.com", "login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido());

        assertThrows(DomainException.class, () -> cliente.atualizarDadosBasicos("", "email@test.com"));
        assertThrows(DomainException.class, () -> cliente.atualizarDadosBasicos(null, "email@test.com"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar dados básicos com email inválido")
    void deveLancarExcecaoAoAtualizarDadosBasicosEmailInvalido() {
        Cliente cliente = new Cliente("Nome", "email@test.com", "login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido());

        assertThrows(DomainException.class, () -> cliente.atualizarDadosBasicos("Nome", ""));
        assertThrows(DomainException.class, () -> cliente.atualizarDadosBasicos("Nome", null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com nome inválido")
    void deveLancarExcecaoNomeInvalido() {
        assertThrows(DomainException.class, () -> 
            new Cliente(null, "email@test.com", "login", "senha", criarTipoUsuarioValido(), criarEnderecoValido())
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com login inválido")
    void deveLancarExcecaoLoginInvalido() {
        assertThrows(DomainException.class, () -> 
            new Cliente("Nome", "email@test.com", "  ", "senha", criarTipoUsuarioValido(), criarEnderecoValido())
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com senha inválida")
    void deveLancarExcecaoSenhaInvalida() {
        assertThrows(DomainException.class, () -> 
            new Cliente("Nome", "email@test.com", "login", null, criarTipoUsuarioValido(), criarEnderecoValido())
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com tipo de usuário nulo")
    void deveLancarExcecaoTipoUsuarioNulo() {
        assertThrows(DomainException.class, () -> 
            new Cliente("Nome", "email@test.com", "login", "senha", null, criarEnderecoValido())
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar com endereço nulo")
    void deveLancarExcecaoEnderecoNulo() {
        assertThrows(DomainException.class, () -> 
            new Cliente("Nome", "email@test.com", "login", "senha", criarTipoUsuarioValido(), null)
        );
    }

    @Test
    @DisplayName("Deve testar equals e hashCode")
    void deveTestarEqualsHashCode() {
        UUID id = UUID.randomUUID();
        Cliente c1 = new Cliente(id, "Nome", "email@test.com", "login", "senha", 
                criarTipoUsuarioValido(), criarEnderecoValido(), LocalDateTime.now(), true);
        Cliente c2 = new Cliente(id, "Outro Nome", "outro@email.com", "outro.login", "outra.senha", 
                criarTipoUsuarioValido(), criarEnderecoValido(), LocalDateTime.now(), true);
        
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(new Object(), c1);
    }
}
