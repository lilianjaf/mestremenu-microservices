package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioNaoAutenticadoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UsuarioDeveEstarAutenticadoRuleTest {

    @Mock
    private UsuarioBase usuarioLogado;

    private final UsuarioDeveEstarAutenticadoRule rule = new UsuarioDeveEstarAutenticadoRule();

    @Test
    @DisplayName("Deve validar com sucesso quando usuario esta autenticado")
    void deveValidarComSucessoQuandoUsuarioEstaAutenticado() {
        assertDoesNotThrow(() -> rule.validar(usuarioLogado));
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao esta autenticado")
    void deveLancarExcecaoQuandoUsuarioNaoEstaAutenticado() {
        assertThrows(UsuarioNaoAutenticadoException.class, () -> rule.validar((UsuarioBase) null));
    }

    @Test
    @DisplayName("Deve validar contexto de inativacao")
    void deveValidarContextoInativacao() {
        InativacaoUsuarioContext context = new InativacaoUsuarioContext(usuarioLogado, null);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve validar contexto de exclusao de tipo")
    void deveValidarContextoExclusaoTipo() {
        ExclusaoTipoUsuarioContext context = new ExclusaoTipoUsuarioContext(Optional.empty(), () -> false, usuarioLogado);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve validar com usuario buscado")
    void deveValidarComUsuarioBuscado() {
        ConsultaUsuarioContext context = new ConsultaUsuarioContext(usuarioLogado, null);
        assertDoesNotThrow(() -> rule.validar(context));
    }
}
