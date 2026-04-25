package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.gateway.CodificadorDeSenha;
import com.github.lilianjaf.usuario_service.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.core.rules.CriacaoUsuarioPublicoContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorCriacaoUsuarioPublicoRule;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarUsuarioPublicoUseCaseImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private CodificadorDeSenha codificadorDeSenha;

    @Mock
    private ValidadorCriacaoUsuarioPublicoRule validadorCriacao;

    private CriarUsuarioPublicoUseCaseImpl usecase;

    @BeforeEach
    void setUp() {
        usecase = new CriarUsuarioPublicoUseCaseImpl(
                usuarioRepository,
                tipoUsuarioRepository,
                List.of(validadorCriacao),
                transactionGateway,
                codificadorDeSenha
        );

        when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
    }

    @Test
    @DisplayName("Deve criar um usuário público com sucesso")
    void deveCriarUsuarioPublicoComSucesso() {
        String nome = "João Silva";
        String email = "joao@email.com";
        String login = "joao.silva";
        String senhaPura = "senha123";
        TipoUsuario tipoCliente = new TipoUsuario("cliente", TipoNativo.CLIENTE);

        when(tipoUsuarioRepository.findByNome("cliente")).thenReturn(Optional.of(tipoCliente));
        when(codificadorDeSenha.codificar(senhaPura)).thenReturn("senhaCripto");
        when(usuarioRepository.salvar(any(UsuarioBase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID result = usecase.criar(
                nome, email, login, senhaPura,
                "Rua A", "123", "Ap 1", "Bairro X", "Cidade Y", "12345678", "UF"
        );

        assertNotNull(result);
        verify(validadorCriacao).validar(any(CriacaoUsuarioPublicoContext.class));
        verify(codificadorDeSenha).codificar(senhaPura);
        verify(usuarioRepository).salvar(any(UsuarioBase.class));
    }
}
