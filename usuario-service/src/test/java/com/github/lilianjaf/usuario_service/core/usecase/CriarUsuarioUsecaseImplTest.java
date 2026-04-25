package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.usuario_service.core.gateway.*;
import com.github.lilianjaf.usuario_service.core.rules.CriacaoUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorCriacaoUsuarioRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarUsuarioUsecaseImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private CodificadorDeSenha codificadorDeSenha;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private ValidadorCriacaoUsuarioRule validadorPermissao;

    @Mock
    private ValidadorCriacaoUsuarioRule validadorCriacao;

    private CriarUsuarioUsecaseImpl usecase;

    @BeforeEach
    void setUp() {
        usecase = new CriarUsuarioUsecaseImpl(
                usuarioRepository,
                tipoUsuarioRepository,
                codificadorDeSenha,
                transactionGateway,
                obterUsuarioLogadoGateway,
                List.of(validadorPermissao),
                List.of(validadorCriacao)
        );

        lenient().when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso utilizando tipo existente")
    void deveCriarUsuarioComSucessoTipoExistente() {
        String nome = "Novo Usuario";
        String email = "novo@email.com";
        String login = "novo.login";
        String senhaPura = "senha123";
        String nomeTipo = "ADMIN";
        UsuarioBase usuarioLogado = mock(UsuarioBase.class);
        TipoUsuario tipoExistente = new TipoUsuario(nomeTipo, TipoNativo.DONO);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(tipoUsuarioRepository.findByNome(nomeTipo)).thenReturn(Optional.of(tipoExistente));
        when(codificadorDeSenha.codificar(senhaPura)).thenReturn("senhaCripto");
        when(usuarioRepository.salvar(any(UsuarioBase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID result = usecase.criar(
                nome, email, login, senhaPura, nomeTipo, TipoNativo.DONO,
                "Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345678", "UF"
        );

        assertNotNull(result);
        verify(validadorPermissao).validar(any(CriacaoUsuarioContext.class));
        verify(validadorCriacao).validar(any(CriacaoUsuarioContext.class));
        verify(usuarioRepository).salvar(any(UsuarioBase.class));
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso criando novo tipo")
    void deveCriarUsuarioComSucessoNovoTipo() {
        String nome = "Novo Usuario";
        String email = "novo@email.com";
        String login = "novo.login";
        String senhaPura = "senha123";
        String nomeTipo = "GERENTE";
        UsuarioBase usuarioLogado = mock(UsuarioBase.class);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(tipoUsuarioRepository.findByNome(nomeTipo)).thenReturn(Optional.empty());
        when(tipoUsuarioRepository.salvar(any(TipoUsuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(codificadorDeSenha.codificar(senhaPura)).thenReturn("senhaCripto");
        when(usuarioRepository.salvar(any(UsuarioBase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID result = usecase.criar(
                nome, email, login, senhaPura, nomeTipo, TipoNativo.DONO,
                "Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345678", "UF"
        );

        assertNotNull(result);
        verify(tipoUsuarioRepository).salvar(any(TipoUsuario.class));
        verify(usuarioRepository).salvar(any(UsuarioBase.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não for encontrado")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoEncontrado() {
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> usecase.criar(
                "nome", "email", "login", "senha", "tipo", TipoNativo.CLIENTE,
                "rua", "1", "", "bairro", "cidade", "cep", "uf"
        ));
    }
}
