package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.Endereco;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.core.rules.AtualizacaoUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorAtualizacaoUsuarioRule;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorPermissaoAtualizacaoUsuarioRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioUsecaseImplTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private ValidadorPermissaoAtualizacaoUsuarioRule validadorPermissao;

    @Mock
    private ValidadorAtualizacaoUsuarioRule validadorAtualizacao;

    private AtualizarUsuarioUsecaseImpl usecase;

    @BeforeEach
    void setUp() {
        usecase = new AtualizarUsuarioUsecaseImpl(
                repository,
                transactionGateway,
                obterUsuarioLogadoGateway,
                List.of(validadorPermissao),
                List.of(validadorAtualizacao)
        );

        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(transactionGateway).execute(any(Runnable.class));
    }

    @Test
    @DisplayName("Deve atualizar usuário com endereço corretamente")
    void deveAtualizarUsuarioComEnderecoCorretamente() {
        UUID id = UUID.randomUUID();
        String novoNome = "Novo Nome";
        String novoEmail = "novo@email.com";
        UsuarioBase usuarioSendoEditado = mock(UsuarioBase.class);
        UsuarioBase usuarioLogado = mock(UsuarioBase.class);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(repository.findById(id)).thenReturn(Optional.of(usuarioSendoEditado));
        when(repository.findByEmail(novoEmail)).thenReturn(Optional.empty());

        usecase.atualizarComEndereco(id, novoNome, novoEmail, "Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345678", "UF");

        verify(validadorPermissao).validar(any(AtualizacaoUsuarioContext.class));
        verify(validadorAtualizacao).validar(any(AtualizacaoUsuarioContext.class));
        verify(usuarioSendoEditado).atualizarDadosBasicos(novoNome, novoEmail);
        verify(usuarioSendoEditado).atualizarEndereco(any(Endereco.class));
        verify(repository).salvar(usuarioSendoEditado);
    }

    @Test
    @DisplayName("Deve atualizar usuário sem endereço corretamente")
    void deveAtualizarUsuarioSemEnderecoCorretamente() {
        UUID id = UUID.randomUUID();
        String novoNome = "Novo Nome";
        String novoEmail = "novo@email.com";
        UsuarioBase usuarioSendoEditado = mock(UsuarioBase.class);
        UsuarioBase usuarioLogado = mock(UsuarioBase.class);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(repository.findById(id)).thenReturn(Optional.of(usuarioSendoEditado));
        when(repository.findByEmail(novoEmail)).thenReturn(Optional.empty());

        usecase.atualizarSemEndereco(id, novoNome, novoEmail);

        verify(validadorPermissao).validar(any(AtualizacaoUsuarioContext.class));
        verify(validadorAtualizacao).validar(any(AtualizacaoUsuarioContext.class));
        verify(usuarioSendoEditado).atualizarDadosBasicos(novoNome, novoEmail);
        verify(repository).salvar(usuarioSendoEditado);
        verify(usuarioSendoEditado, never()).atualizarEndereco(any());
    }
}
