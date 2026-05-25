package com.github.lilianjaf.restaurante_service.core.usecase;

import test.TestRestauranteServiceApplication;
import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Endereco;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.gateway.CardapioRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
@AutoConfigureMockMvc
class AlterarCardapioUseCaseIT {

    @Autowired
    private AlterarCardapioUseCase usecase;

    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private UUID autenticarComo(UUID userId) {
        var authToken = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        authToken.setDetails("DONO");
        SecurityContextHolder.getContext().setAuthentication(authToken);
        return userId;
    }

    private Restaurante criarRestaurante(UUID idDono) {
        Endereco endereco = new Endereco("Rua Principal", "100", null, "Centro", "Cidade", "12345000", "SP");
        return restauranteRepository.salvar(new Restaurante("Restaurante Teste", endereco, "Italiana", "08:00-22:00", idDono));
    }

    private Cardapio criarCardapio(String nome, UUID idRestaurante) {
        return cardapioRepository.salvar(new Cardapio(nome, idRestaurante, Collections.emptyList()));
    }

    @Test
    @DisplayName("Deve alterar o nome do cardápio com sucesso")
    void deveAlterarNomeComSucesso() {
        UUID donoId = autenticarComo(UUID.randomUUID());
        Restaurante restaurante = criarRestaurante(donoId);
        Cardapio cardapio = criarCardapio("Menu Principal", restaurante.getId());

        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(cardapio.getId(), "Menu Atualizado", null);

        Cardapio resultado = usecase.executar(dados);

        assertEquals("Menu Atualizado", resultado.getNome());
        Cardapio persistido = cardapioRepository.findById(resultado.getId()).orElseThrow();
        assertEquals("Menu Atualizado", persistido.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é dono do restaurante")
    void deveLancarExcecaoQuandoNaoEDono() {
        UUID donoId = UUID.randomUUID();
        UUID outroId = UUID.randomUUID();
        Restaurante restaurante = criarRestaurante(donoId);
        Cardapio cardapio = criarCardapio("Menu Principal", restaurante.getId());

        autenticarComo(outroId);

        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(cardapio.getId(), "Nome Inválido", null);

        assertThrows(CardapioException.class, () -> usecase.executar(dados));
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome já existe para o restaurante")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        UUID donoId = autenticarComo(UUID.randomUUID());
        Restaurante restaurante = criarRestaurante(donoId);

        criarCardapio("Menu A", restaurante.getId());
        Cardapio cardapioB = criarCardapio("Menu B", restaurante.getId());

        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(cardapioB.getId(), "Menu A", null);

        assertThrows(RuntimeException.class, () -> usecase.executar(dados));
    }
}
