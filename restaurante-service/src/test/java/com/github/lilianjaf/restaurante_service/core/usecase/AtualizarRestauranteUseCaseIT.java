package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Endereco;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoRestaurante;
import com.github.lilianjaf.restaurante_service.core.exception.DomainException;
import com.github.lilianjaf.restaurante_service.core.exception.EdicaoRestauranteNaoAutorizadaException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteRepository;
import test.TestRestauranteServiceApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestRestauranteServiceApplication.class)
@ActiveProfiles("test")
@Transactional
@DisplayName("Teste de Integração - AtualizarRestauranteUseCase")
class AtualizarRestauranteUseCaseIT {

    @Autowired
    private AtualizarRestauranteUseCase usecase;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private UUID autenticarComoDono() {
        UUID userId = UUID.randomUUID();
        var authToken = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        authToken.setDetails("DONO");
        SecurityContextHolder.getContext().setAuthentication(authToken);
        return userId;
    }

    private Restaurante criarESalvarRestaurante(String nome, UUID idDono) {
        Endereco endereco = new Endereco("Rua Restaurante", "10", "A", "Centro", "Cidade", "12345000", "SP");
        Restaurante restaurante = new Restaurante(nome, endereco, "Italiana", "08:00 - 22:00", idDono);
        return restauranteRepository.salvar(restaurante);
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso quando o usuário é o dono")
    void deveAtualizarRestauranteComSucesso() {
        UUID donoId = autenticarComoDono();
        Restaurante restaurante = criarESalvarRestaurante("Restaurante Original", donoId);

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
        UUID idDonoReal = UUID.randomUUID();
        UUID idOutroDono = autenticarComoDono(); // outro usuário autenticado

        Restaurante restaurante = criarESalvarRestaurante("Restaurante do Dono Real", idDonoReal);
        DadosAtualizacaoRestaurante dados = new DadosAtualizacaoRestaurante("Tentativa", restaurante.getEndereco(), "Qualquer", "00:00");

        assertThrows(EdicaoRestauranteNaoAutorizadaException.class, () ->
                usecase.executar(restaurante.getId(), dados)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o restaurante não existe")
    void deveLancarExcecaoQuandoRestauranteNaoExiste() {
        autenticarComoDono();

        DadosAtualizacaoRestaurante dados = new DadosAtualizacaoRestaurante("Inexistente",
                new Endereco("Rua", "1", null, "B", "C", "12345", "SP"), "Tipo", "Hora");

        assertThrows(DomainException.class, () ->
                usecase.executar(UUID.randomUUID(), dados)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não está autenticado")
    void deveLancarExcecaoQuandoUsuarioNaoAutenticado() {
        UUID donoId = UUID.randomUUID();
        Restaurante restaurante = criarESalvarRestaurante("Restaurante Sem Login", donoId);

        DadosAtualizacaoRestaurante dados = new DadosAtualizacaoRestaurante("Falha", restaurante.getEndereco(), "Tipo", "Hora");

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () ->
                usecase.executar(restaurante.getId(), dados)
        );
    }
}
