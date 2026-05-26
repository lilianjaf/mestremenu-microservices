package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.Endereco;
import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.AcessoNegadoCriacaoUsuarioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ApenasDonoPodeCriarNovosUsuariosRuleTest {

    private final ApenasDonoPodeCriarNovosUsuariosRule rule = new ApenasDonoPodeCriarNovosUsuariosRule();

    private CriacaoUsuarioContext contexto(UsuarioBase logado) {
        return new CriacaoUsuarioContext("nome", "email", "login", "senha", "tipo",
                TipoNativo.CLIENTE, () -> false, () -> false, logado);
    }

    private static Dono dono() {
        Endereco endereco = new Endereco("Rua A", "1", "", "Bairro", "Cidade", "01310-000", "SP");
        return new Dono("Dono", "d@e.com", "dono", "senha",
                new TipoUsuario("DONO", TipoNativo.DONO), endereco, List.of());
    }

    @Test
    @DisplayName("Deve passar quando usuário logado é Dono")
    void devePassarQuandoEhDono() {
        assertDoesNotThrow(() -> rule.validar(contexto(dono())));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não é Dono")
    void deveLancarExcecaoQuandoNaoEhDono() {
        UsuarioBase naoEhDono = mock(UsuarioBase.class);
        assertThrows(AcessoNegadoCriacaoUsuarioException.class, () -> rule.validar(contexto(naoEhDono)));
    }
}
