package com.github.lilianjaf.restaurante_service.core.usecase;

import test.TestRestauranteServiceApplication;
import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Endereco;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.gateway.CardapioRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteRepository;
import jakarta.persistence.EntityManager;
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
class DeletarCardapioUseCaseIT {

    @Autowired
    private DeletarCardapioUseCase usecase;

    @Autowired
    private EntityManager entityManager;

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

    private Cardapio criarCardapio(UUID idRestaurante) {
        return cardapioRepository.salvar(new Cardapio("Menu Principal", idRestaurante, Collections.emptyList()));
    }

    @Test
    @DisplayName("Deve deletar cardápio com sucesso")
    void deveDeletarCardapioComSucesso() {
        UUID donoId = autenticarComo(UUID.randomUUID());
        Restaurante restaurante = criarRestaurante(donoId);
        Cardapio cardapio = criarCardapio(restaurante.getId());
        entityManager.flush();

        assertDoesNotThrow(() -> usecase.executar(cardapio.getId()));

        entityManager.flush();
        entityManager.clear();
        assertTrue(cardapioRepository.findById(cardapio.getId()).isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é dono do restaurante")
    void deveLancarExcecaoQuandoNaoEDono() {
        UUID donoId = UUID.randomUUID();
        UUID outroId = UUID.randomUUID();
        Restaurante restaurante = criarRestaurante(donoId);
        Cardapio cardapio = criarCardapio(restaurante.getId());

        autenticarComo(outroId);

        assertThrows(CardapioException.class, () -> usecase.executar(cardapio.getId()));

        assertTrue(cardapioRepository.existeNomeParaRestaurante("Menu Principal", restaurante.getId()));
    }
}
