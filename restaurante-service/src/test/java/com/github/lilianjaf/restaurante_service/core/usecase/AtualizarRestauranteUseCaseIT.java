package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Endereco;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.dto.DadosAtualizacaoRestaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.DomainException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.EdicaoRestauranteNaoAutorizadaException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.RestauranteRepository;
import com.github.lilianjaf.mestremenuclean.usuario.core.domain.Dono;
import com.github.lilianjaf.mestremenuclean.usuario.core.domain.TipoNativo;
import com.github.lilianjaf.mestremenuclean.usuario.core.domain.TipoUsuario;
import com.github.lilianjaf.mestremenuclean.usuario.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.mestremenuclean.usuario.core.gateway.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Teste de Integração - AtualizarRestauranteUseCase")
class AtualizarRestauranteUseCaseIT {

    @Autowired
    private AtualizarRestauranteUseCase usecase;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void autenticar(String login) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(login, null)
        );
    }

    private Dono criarESalvarDono(String login, String email) {
        TipoUsuario tipoDono = new TipoUsuario("DONO_" + UUID.randomUUID(), TipoNativo.DONO);
        tipoUsuarioRepository.salvar(tipoDono);
        com.github.lilianjaf.mestremenuclean.usuario.core.domain.Endereco endereco =
                new com.github.lilianjaf.mestremenuclean.usuario.core.domain.Endereco("Rua", "123", null, "Bairro", "Cidade", "12345678", "SP");
        Dono dono = new Dono("Dono Teste", email, login, "senha", tipoDono, endereco, null);
        usuarioRepository.salvar(dono);
        return dono;
    }

    private Restaurante criarESalvarRestaurante(String nome, UUID idDono) {
        Endereco endereco = new Endereco("Rua Restaurante", "10", "A", "Centro", "Cidade", "12345000", "SP");
        Restaurante restaurante = new Restaurante(nome, endereco, "Italiana", "08:00 - 22:00", idDono);
        return restauranteRepository.salvar(restaurante);
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso quando o usuário é o dono")
    void deveAtualizarRestauranteComSucesso() {
        Dono dono = criarESalvarDono("dono1", "dono1@teste.com");
        Restaurante restaurante = criarESalvarRestaurante("Restaurante Original", dono.getId());
        autenticar(dono.getLogin());

        String novoNome = "Restaurante Atualizado";
        Endereco novoEndereco = new Endereco("Nova Rua", "20", null, "Novo Bairro", "Nova Cidade", "54321000", "RJ");
        DadosAtualizacaoRestaurante dados = new DadosAtualizacaoRestaurante(novoNome, novoEndereco, "Japonesa", "10:00 - 23:00");

        Restaurante atualizado = usecase.executar(restaurante.getId(), dados);

        assertNotNull(atualizado);
        assertEquals(novoNome, atualizado.getNome());
        assertEquals("Japonesa", atualizado.getTipoCozinha());
        assertEquals(novoEndereco.logradouro(), atualizado.getEndereco().logradouro());

        Restaurante doBanco = restauranteRepository.findById(restaurante.getId()).orElseThrow();
        assertEquals(novoNome, doBanco.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário autenticado não é o dono do restaurante")
    void deveLancarExcecaoQuandoNaoForODono() {
        Dono donoLegitimo = criarESalvarDono("dono_real", "dono_real@teste.com");
        Dono outroDono = criarESalvarDono("outro_dono", "outro_dono@teste.com");
        Restaurante restaurante = criarESalvarRestaurante("Restaurante do Dono Real", donoLegitimo.getId());
        autenticar(outroDono.getLogin());

        DadosAtualizacaoRestaurante dados = new DadosAtualizacaoRestaurante("Tentativa", restaurante.getEndereco(), "Qualquer", "00:00");

        assertThrows(EdicaoRestauranteNaoAutorizadaException.class, () ->
                usecase.executar(restaurante.getId(), dados)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
        Dono dono = criarESalvarDono("dono2", "dono2@teste.com");
        autenticar(dono.getLogin());

        DadosAtualizacaoRestaurante dados = new DadosAtualizacaoRestaurante("Inexistente",
                new Endereco("Rua", "1", null, "B", "C", "12345", "SP"), "Tipo", "Hora");

        assertThrows(DomainException.class, () ->
                usecase.executar(UUID.randomUUID(), dados)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não está autenticado")
    void deveLancarExcecaoQuandoUsuarioNaoAutenticado() {
        Dono dono = criarESalvarDono("dono3", "dono3@teste.com");
        Restaurante restaurante = criarESalvarRestaurante("Restaurante Sem Login", dono.getId());
        // Sem chamar autenticar()

        DadosAtualizacaoRestaurante dados = new DadosAtualizacaoRestaurante("Falha", restaurante.getEndereco(), "Tipo", "Hora");

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () ->
                usecase.executar(restaurante.getId(), dados)
        );
    }
}
