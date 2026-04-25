package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.dto.UsuarioOutput;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioNaoEncontradoException;
import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.core.rules.ConsultaUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorConsultaUsuarioRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarUsuarioUsecaseImplTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private ValidadorConsultaUsuarioRule validadorPermissao;

    @Mock
    private ValidadorConsultaUsuarioRule validadorConsulta;

    private BuscarUsuarioUsecaseImpl usecase;

    @BeforeEach
    void setUp() {
        usecase = new BuscarUsuarioUsecaseImpl(
                repository,
                obterUsuarioLogadoGateway,
                List.of(validadorPermissao),
                List.of(validadorConsulta)
        );
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        UUID id = UUID.randomUUID();
        UsuarioBase usuarioLogado = mock(UsuarioBase.class);
        UsuarioBase usuarioBuscado = mock(UsuarioBase.class);
        TipoUsuario tipoUsuario = mock(TipoUsuario.class);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(repository.findById(id)).thenReturn(Optional.of(usuarioBuscado));
        when(usuarioBuscado.getId()).thenReturn(id);
        when(usuarioBuscado.getNome()).thenReturn("Nome Teste");
        when(usuarioBuscado.getEmail()).thenReturn("teste@email.com");
        when(usuarioBuscado.getLogin()).thenReturn("login.teste");
        when(usuarioBuscado.getTipoCustomizado()).thenReturn(tipoUsuario);
        when(tipoUsuario.getNome()).thenReturn("ADMIN");
        when(tipoUsuario.getTipoNativo()).thenReturn(TipoNativo.CLIENTE);
        when(usuarioBuscado.getAtivo()).thenReturn(true);

        UsuarioOutput result = usecase.buscarPorId(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Nome Teste", result.nome());
        verify(validadorPermissao).validar(any(ConsultaUsuarioContext.class));
        verify(validadorConsulta).validar(any(ConsultaUsuarioContext.class));
    }

    @Test
    @DisplayName("Deve buscar entidade de usuário com sucesso")
    void deveBuscarEntidadeComSucesso() {
        UUID id = UUID.randomUUID();
        UsuarioBase usuario = mock(UsuarioBase.class);
        when(repository.findById(id)).thenReturn(Optional.of(usuario));

        UsuarioBase result = usecase.buscarEntidade(id);

        assertNotNull(result);
        assertEquals(usuario, result);
    }

    @Test
    @DisplayName("Deve lançar exceção quando buscar entidade de usuário não existente")
    void deveLancarExcecaoQuandoBuscarEntidadeNaoExistente() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> usecase.buscarEntidade(id));
    }
}
