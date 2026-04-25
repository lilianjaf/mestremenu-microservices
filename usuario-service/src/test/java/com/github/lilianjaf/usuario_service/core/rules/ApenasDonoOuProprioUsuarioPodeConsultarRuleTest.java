package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.AcessoNegadoConsultaUsuarioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApenasDonoOuProprioUsuarioPodeConsultarRuleTest {

    @Mock
    private UsuarioBase usuarioLogado;

    @Mock
    private UsuarioBase usuarioBuscado;

    private final ApenasDonoOuProprioUsuarioPodeConsultarRule rule = new ApenasDonoOuProprioUsuarioPodeConsultarRule();

    @Test
    @DisplayName("Deve permitir consulta quando usuario logado eh dono")
    void devePermitirConsultaQuandoUsuarioLogadoEhDono() {
        Dono donoLogado = mock(Dono.class);
        ConsultaUsuarioContext context = new ConsultaUsuarioContext(donoLogado, usuarioBuscado);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve permitir consulta quando usuario logado eh o proprio buscado")
    void devePermitirConsultaQuandoEhOMesmoUsuario() {
        UUID id = UUID.randomUUID();
        when(usuarioLogado.getId()).thenReturn(id);
        when(usuarioBuscado.getId()).thenReturn(id);

        ConsultaUsuarioContext context = new ConsultaUsuarioContext(usuarioLogado, usuarioBuscado);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando nao tem permissao para consultar")
    void deveLancarExcecaoQuandoNaoTemPermissao() {
        when(usuarioLogado.getId()).thenReturn(UUID.randomUUID());
        when(usuarioBuscado.getId()).thenReturn(UUID.randomUUID());

        ConsultaUsuarioContext context = new ConsultaUsuarioContext(usuarioLogado, usuarioBuscado);
        assertThrows(AcessoNegadoConsultaUsuarioException.class, () -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve retornar sem erro se o usuario logado for nulo")
    void deveRetornarSeUsuarioLogadoForNulo() {
        ConsultaUsuarioContext context = new ConsultaUsuarioContext(null, usuarioBuscado);
        assertDoesNotThrow(() -> rule.validar(context));
    }
}
