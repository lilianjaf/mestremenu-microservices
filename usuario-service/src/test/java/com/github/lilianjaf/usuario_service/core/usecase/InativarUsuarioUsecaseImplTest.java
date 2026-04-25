package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.core.rules.InativacaoUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorInativacaoUsuarioRule;
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
class InativarUsuarioUsecaseImplTest {

    @Mock
    private BuscarUsuarioUsecase buscarUsuarioUsecase;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private ValidadorInativacaoUsuarioRule validadorPermissao;

    @Mock
    private ValidadorInativacaoUsuarioRule validadorInativacao;

    private InativarUsuarioUsecaseImpl usecase;

    @BeforeEach
    void setUp() {
        usecase = new InativarUsuarioUsecaseImpl(
                buscarUsuarioUsecase,
                repository,
                transactionGateway,
                obterUsuarioLogadoGateway,
                List.of(validadorPermissao),
                List.of(validadorInativacao)
        );

        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(transactionGateway).execute(any(Runnable.class));
    }

    @Test
    @DisplayName("Deve inativar um usuário com sucesso")
    void deveInativarUsuarioComSucesso() {
        UUID id = UUID.randomUUID();
        UsuarioBase usuarioLogado = mock(UsuarioBase.class);
        UsuarioBase usuarioAlvo = mock(UsuarioBase.class);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(buscarUsuarioUsecase.buscarEntidade(id)).thenReturn(usuarioAlvo);

        usecase.inativar(id);

        verify(validadorPermissao).validar(any(InativacaoUsuarioContext.class));
        verify(validadorInativacao).validar(any(InativacaoUsuarioContext.class));
        verify(usuarioAlvo).desativar();
        verify(repository).salvar(usuarioAlvo);
    }
}
