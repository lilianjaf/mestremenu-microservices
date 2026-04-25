package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.rules.CriacaoTipoUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorCriacaoTipoUsuarioRule;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorPermissaoRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarTipoUsuarioUsecaseImplTest {

    @Mock
    private TipoUsuarioRepository repository;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private ValidadorPermissaoRule validadorPermissao;

    @Mock
    private ValidadorCriacaoTipoUsuarioRule validadorCriacao;

    private CriarTipoUsuarioUsecaseImpl usecase;

    @BeforeEach
    void setUp() {
        usecase = new CriarTipoUsuarioUsecaseImpl(
                repository,
                obterUsuarioLogadoGateway,
                transactionGateway,
                List.of(validadorPermissao),
                List.of(validadorCriacao)
        );

        when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
    }

    @Test
    @DisplayName("Deve criar um tipo de usuário com sucesso")
    void deveCriarTipoUsuarioComSucesso() {
        String nome = "Novo Tipo";
        TipoNativo tipoNativo = TipoNativo.CLIENTE;
        UsuarioBase usuarioLogado = mock(UsuarioBase.class);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(repository.findByNome(nome)).thenReturn(Optional.empty());
        when(repository.salvar(any(TipoUsuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TipoUsuario result = usecase.criar("admin", nome, tipoNativo);

        assertNotNull(result);
        assertEquals(nome, result.getNome());
        assertEquals(tipoNativo, result.getTipoNativo());
        verify(validadorPermissao).validar(usuarioLogado);
        verify(validadorCriacao).validar(any(CriacaoTipoUsuarioContext.class));
        verify(repository).salvar(any(TipoUsuario.class));
    }
}
