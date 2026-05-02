package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.*;
import com.github.lilianjaf.usuario_service.core.exception.EdicaoUsuarioNaoAutorizadaException;
import com.github.lilianjaf.usuario_service.core.exception.EmailUsuarioJaEmUsoException;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioNaoEncontradoException;
import com.github.lilianjaf.usuario_service.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Teste de Integração - AtualizarUsuarioUsecase")
class AtualizarUsuarioUsecaseIT {

    @Autowired
    private AtualizarUsuarioUsecase usecase;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void autenticar(String login) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(login, null)
        );
    }

    private Dono criarESalvarDono(String login, String email) {
        TipoUsuario tipoDono = new TipoUsuario("DONO_" + UUID.randomUUID(), TipoNativo.DONO);
        tipoUsuarioRepository.salvar(tipoDono);
        Endereco endereco = new Endereco("Rua", "123", null, "Bairro", "Cidade", "12345678", "SP");
        Dono dono = new Dono("Dono Teste", email, login, "senha", tipoDono, endereco, null);
        usuarioRepository.salvar(dono);
        return dono;
    }

    private Cliente criarESalvarCliente(String login, String email) {
        TipoUsuario tipoCliente = new TipoUsuario("CLIENTE_" + UUID.randomUUID(), TipoNativo.CLIENTE);
        tipoUsuarioRepository.salvar(tipoCliente);
        Endereco endereco = new Endereco("Rua", "123", null, "Bairro", "Cidade", "12345678", "SP");
        Cliente cliente = new Cliente("Cliente Teste", email, login, "senha", tipoCliente, endereco);
        usuarioRepository.salvar(cliente);
        return cliente;
    }

    @Test
    @DisplayName("Deve atualizar dados básicos do próprio usuário com sucesso")
    void deveAtualizarProprioUsuarioComSucesso() {
        Cliente cliente = criarESalvarCliente("cliente1", "cliente1@teste.com");
        autenticar(cliente.getLogin());

        String novoNome = "Cliente Atualizado";
        String novoEmail = "novoemail@teste.com";

        usecase.atualizarSemEndereco(cliente.getId(), novoNome, novoEmail);

        UsuarioBase atualizado = usuarioRepository.findById(cliente.getId()).orElseThrow();
        assertEquals(novoNome, atualizado.getNome());
        assertEquals(novoEmail, atualizado.getEmail());
    }

    @Test
    @DisplayName("Deve atualizar dados e endereço do próprio usuário com sucesso")
    void deveAtualizarProprioUsuarioEEnderecoComSucesso() {
        Cliente cliente = criarESalvarCliente("cliente2", "cliente2@teste.com");
        autenticar(cliente.getLogin());

        String novoNome = "Cliente Com Endereco";
        String novoEmail = "cliente_end@teste.com";
        String logradouro = "Nova Rua";
        String numero = "456";
        String cep = "87654321";

        usecase.atualizarComEndereco(cliente.getId(), novoNome, novoEmail, logradouro, numero, null, "Novo Bairro", "Nova Cidade", cep, "RJ");

        UsuarioBase atualizado = usuarioRepository.findById(cliente.getId()).orElseThrow();
        assertEquals(novoNome, atualizado.getNome());
        assertEquals(logradouro, atualizado.getEndereco().logradouro());
        assertEquals(numero, atualizado.getEndereco().numero());
        assertEquals(cep, atualizado.getEndereco().cep());
    }

    @Test
    @DisplayName("Deve permitir que um DONO atualize outro usuário")
    void devePermitirDonoAtualizarOutroUsuario() {
        Dono dono = criarESalvarDono("dono1", "dono1@teste.com");
        Cliente cliente = criarESalvarCliente("cliente3", "cliente3@teste.com");
        autenticar(dono.getLogin());

        String novoNome = "Nome Alterado Pelo Dono";

        usecase.atualizarSemEndereco(cliente.getId(), novoNome, cliente.getEmail());

        UsuarioBase atualizado = usuarioRepository.findById(cliente.getId()).orElseThrow();
        assertEquals(novoNome, atualizado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando e-mail já está em uso por outro usuário")
    void deveLancarExcecaoQuandoEmailJaEmUso() {
        Cliente cliente1 = criarESalvarCliente("cliente4", "cliente4@teste.com");
        Cliente cliente2 = criarESalvarCliente("cliente5", "cliente5@teste.com");
        autenticar(cliente1.getLogin());

        assertThrows(EmailUsuarioJaEmUsoException.class, () ->
            usecase.atualizarSemEndereco(cliente1.getId(), "Novo Nome", cliente2.getEmail())
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário tenta editar outro usuário sem ser DONO")
    void deveLancarExcecaoQuandoNaoAutorizado() {
        Cliente cliente1 = criarESalvarCliente("cliente6", "cliente6@teste.com");
        Cliente cliente2 = criarESalvarCliente("cliente7", "cliente7@teste.com");
        autenticar(cliente1.getLogin());

        assertThrows(EdicaoUsuarioNaoAutorizadaException.class, () ->
            usecase.atualizarSemEndereco(cliente2.getId(), "Tentativa", "tentativa@teste.com")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        Dono dono = criarESalvarDono("dono2", "dono2@teste.com");
        autenticar(dono.getLogin());

        assertThrows(UsuarioNaoEncontradoException.class, () ->
            usecase.atualizarSemEndereco(UUID.randomUUID(), "Inexistente", "inexistente@teste.com")
        );
    }
}
