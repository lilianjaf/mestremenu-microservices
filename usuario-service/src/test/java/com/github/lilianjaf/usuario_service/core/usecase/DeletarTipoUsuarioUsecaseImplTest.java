package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.core.rules.ExclusaoTipoUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorExclusaoTipoUsuarioRule;
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
class DeletarTipoUsuarioUsecaseImplTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private ValidadorExclusaoTipoUsuarioRule validadorPermissao;

    @Mock
    private ValidadorExclusaoTipoUsuarioRule validadorExclusao;

    private DeletarTipoUsuarioUsecaseImpl usecase;

    @BeforeEach
    void setUp() {
        usecase = new DeletarTipoUsuarioUsecaseImpl(
                tipoUsuarioRepository,
                usuarioRepository,
                transactionGateway,
                obterUsuarioLogadoGateway,
                List.of(validadorPermissao),
                List.of(validadorExclusao)
        );

        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(transactionGateway).execute(any(Runnable.class));
    }

    @Test
    @DisplayName("Deve deletar um tipo de usuário com sucesso")
    void deveDeletarTipoUsuarioComSucesso() {
        UUID id = UUID.randomUUID();
        TipoUsuario tipo = new TipoUsuario(id, "Tipo", TipoNativo.CLIENTE);
        UsuarioBase usuarioLogado = mock(UsuarioBase.class);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(tipoUsuarioRepository.findById(id)).thenReturn(Optional.of(tipo));

        usecase.deletar(id);

        verify(validadorPermissao).validar(any(ExclusaoTipoUsuarioContext.class));
        verify(validadorExclusao).validar(any(ExclusaoTipoUsuarioContext.class));
        verify(tipoUsuarioRepository).deletar(tipo);
    }
}
