package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.EmailUsuarioJaEmUsoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailUsuarioDeveSerUnicoRuleTest {

    @Mock
    private UsuarioBase usuarioSendoEditado;

    @Mock
    private UsuarioBase usuarioComMesmoEmail;

    private final EmailUsuarioDeveSerUnicoRule rule = new EmailUsuarioDeveSerUnicoRule();

    @Test
    @DisplayName("Deve permitir quando nao existe usuario com mesmo email")
    void devePermitirQuandoNaoExisteUsuarioComMesmoEmail() {
        AtualizacaoUsuarioContext context = new AtualizacaoUsuarioContext(usuarioSendoEditado, null, null);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve permitir quando o usuario com mesmo email eh o proprio sendo editado")
    void devePermitirQuandoEhOMesmoUsuario() {
        UUID id = UUID.randomUUID();
        when(usuarioSendoEditado.getId()).thenReturn(id);
        when(usuarioComMesmoEmail.getId()).thenReturn(id);
        AtualizacaoUsuarioContext context = new AtualizacaoUsuarioContext(usuarioSendoEditado, usuarioComMesmoEmail, null);

        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando email ja esta em uso por outro usuario")
    void deveLancarExcecaoQuandoEmailEmUsoPorOutro() {
        when(usuarioSendoEditado.getId()).thenReturn(UUID.randomUUID());
        when(usuarioComMesmoEmail.getId()).thenReturn(UUID.randomUUID());
        AtualizacaoUsuarioContext context = new AtualizacaoUsuarioContext(usuarioSendoEditado, usuarioComMesmoEmail, null);

        assertThrows(EmailUsuarioJaEmUsoException.class, () -> rule.validar(context));
    }
}
