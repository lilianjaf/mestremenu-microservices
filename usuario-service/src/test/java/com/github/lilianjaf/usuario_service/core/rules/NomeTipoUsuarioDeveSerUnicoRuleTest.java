package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.exception.NomeTipoUsuarioJaEmUsoException;
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
class NomeTipoUsuarioDeveSerUnicoRuleTest {

    @Mock
    private TipoUsuario tipoAtual;

    @Mock
    private TipoUsuario tipoComMesmoNome;

    private final NomeTipoUsuarioDeveSerUnicoRule rule = new NomeTipoUsuarioDeveSerUnicoRule();

    @Test
    @DisplayName("Deve permitir quando nao existe outro tipo com mesmo nome")
    void devePermitirQuandoNaoExisteOutroComMesmoNome() {
        AtualizacaoTipoUsuarioContext context = new AtualizacaoTipoUsuarioContext(tipoAtual, null, null);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve permitir quando o tipo com mesmo nome eh o proprio tipo atual")
    void devePermitirQuandoEhOMesmoTipo() {
        UUID id = UUID.randomUUID();
        when(tipoAtual.getId()).thenReturn(id);
        when(tipoComMesmoNome.getId()).thenReturn(id);

        AtualizacaoTipoUsuarioContext context = new AtualizacaoTipoUsuarioContext(tipoAtual, tipoComMesmoNome, null);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lancar excecao quando nome ja esta em uso por outro tipo")
    void deveLancarExcecaoQuandoNomeEmUsoPorOutro() {
        when(tipoAtual.getId()).thenReturn(UUID.randomUUID());
        when(tipoComMesmoNome.getId()).thenReturn(UUID.randomUUID());
        when(tipoComMesmoNome.getNome()).thenReturn("VIP");

        AtualizacaoTipoUsuarioContext context = new AtualizacaoTipoUsuarioContext(tipoAtual, tipoComMesmoNome, null);
        assertThrows(NomeTipoUsuarioJaEmUsoException.class, () -> rule.validar(context));
    }
}
