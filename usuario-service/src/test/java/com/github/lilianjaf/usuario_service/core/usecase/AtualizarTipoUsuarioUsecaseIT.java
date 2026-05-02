package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.*;
import com.github.lilianjaf.usuario_service.core.exception.AcessoNegadoException;
import com.github.lilianjaf.usuario_service.core.exception.NomeTipoUsuarioJaEmUsoException;
import com.github.lilianjaf.usuario_service.core.exception.TipoUsuarioNaoEncontradoException;
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
@DisplayName("Teste de Integração - AtualizarTipoUsuarioUsecase")
class AtualizarTipoUsuarioUsecaseIT {

    @Autowired
    private AtualizarTipoUsuarioUsecase usecase;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private Dono criarESalvarDono(String login) {
        TipoUsuario tipoDono = new TipoUsuario("DONO_TESTE_" + UUID.randomUUID(), TipoNativo.DONO);
        tipoUsuarioRepository.salvar(tipoDono);

        Endereco endereco = new Endereco("Rua", "123", null, "Bairro", "Cidade", "12345678", "SP");
        Dono dono = new Dono("Dono Teste", "dono@teste.com", login, "senha", tipoDono, endereco, null);
        usuarioRepository.salvar(dono);
        return dono;
    }

    private Cliente criarESalvarCliente(String login) {
        TipoUsuario tipoCliente = new TipoUsuario("CLIENTE_TESTE_" + UUID.randomUUID(), TipoNativo.CLIENTE);
        tipoUsuarioRepository.salvar(tipoCliente);

        Endereco endereco = new Endereco("Rua", "123", null, "Bairro", "Cidade", "12345678", "SP");
        Cliente cliente = new Cliente("Cliente Teste", "cliente@teste.com", login, "senha", tipoCliente, endereco);
        usuarioRepository.salvar(cliente);
        return cliente;
    }

    private void autenticar(String login) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(login, null)
        );
    }

    @Test
    @DisplayName("Deve atualizar o nome do tipo de usuário quando dados são válidos e usuário tem permissão")
    void deveAtualizarNomeComSucesso() {
        Dono dono = criarESalvarDono("dono_sucesso");
        autenticar(dono.getLogin());

        TipoUsuario tipoParaAtualizar = new TipoUsuario("ANTIGO_NOME", TipoNativo.CLIENTE);
        tipoUsuarioRepository.salvar(tipoParaAtualizar);
        UUID id = tipoParaAtualizar.getId();
        String novoNome = "NOVO_NOME_INTEGRACAO";

        usecase.atualizar(id, novoNome);

        TipoUsuario atualizado = tipoUsuarioRepository.findById(id).orElseThrow();
        assertEquals(novoNome, atualizado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o novo nome já está em uso por outro tipo de usuário")
    void deveLancarExcecaoQuandoNomeJaEmUso() {
        Dono dono = criarESalvarDono("dono_duplicado");
        autenticar(dono.getLogin());

        TipoUsuario tipo1 = new TipoUsuario("NOME_UM", TipoNativo.CLIENTE);
        TipoUsuario tipo2 = new TipoUsuario("NOME_DOIS", TipoNativo.CLIENTE);
        tipoUsuarioRepository.salvar(tipo1);
        tipoUsuarioRepository.salvar(tipo2);

        assertThrows(NomeTipoUsuarioJaEmUsoException.class, () -> 
            usecase.atualizar(tipo1.getId(), "NOME_DOIS")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário logado não tem permissão de DONO")
    void deveLancarExcecaoQuandoSemPermissao() {
        Cliente cliente = criarESalvarCliente("cliente_sem_permissao");
        autenticar(cliente.getLogin());

        TipoUsuario tipo = new TipoUsuario("NOME_TESTE", TipoNativo.CLIENTE);
        tipoUsuarioRepository.salvar(tipo);

        assertThrows(AcessoNegadoException.class, () -> 
            usecase.atualizar(tipo.getId(), "NOVO_NOME")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o tipo de usuário não existe")
    void deveLancarExcecaoQuandoTipoNaoEncontrado() {
        Dono dono = criarESalvarDono("dono_nao_encontrado");
        autenticar(dono.getLogin());

        assertThrows(TipoUsuarioNaoEncontradoException.class, () -> 
            usecase.atualizar(UUID.randomUUID(), "QUALQUER_NOME")
        );
    }
}
