package com.github.lilianjaf.restaurante_service.core.usecase;

import test.TestRestauranteServiceApplication;
import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Endereco;
import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.exception.CriacaoItemNaoAutorizadaException;
import com.github.lilianjaf.restaurante_service.core.gateway.CardapioRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.ItemCardapioRepository;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestRestauranteServiceApplication.class)
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class CriarItemCardapioUseCaseIT {

    @Autowired
    private CriarItemCardapioUseCase usecase;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void autenticarComoDono(UUID userId) {
        var authToken = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        authToken.setDetails("DONO");
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private Restaurante criarRestaurante(UUID idDono) {
        Endereco endereco = new Endereco("Rua Principal", "100", null, "Centro", "Cidade", "12345000", "SP");
        return restauranteRepository.salvar(new Restaurante("Restaurante Teste", endereco, "Italiana", "08:00-22:00", idDono));
    }

    private Cardapio criarCardapio(UUID idRestaurante) {
        return cardapioRepository.salvar(new Cardapio("Menu Principal", idRestaurante, Collections.emptyList()));
    }

    @Test
    @DisplayName("Deve criar item no cardápio com sucesso")
    void deveCriarItemComSucesso() {
        UUID donoId = UUID.randomUUID();
        autenticarComoDono(donoId);
        Restaurante restaurante = criarRestaurante(donoId);
        Cardapio cardapio = criarCardapio(restaurante.getId());

        DadosCriacaoItemCardapio dados = new DadosCriacaoItemCardapio(
                "Novo Item", "Descrição", BigDecimal.valueOf(35), true, "item.jpg", cardapio.getId());

        ItemCardapio resultado = usecase.executar(dados);

        assertNotNull(resultado.getId());
        assertTrue(itemCardapioRepository.findById(resultado.getId()).isPresent());
        assertEquals("Novo Item", itemCardapioRepository.findById(resultado.getId()).get().getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome do item já existe no cardápio")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        UUID donoId = UUID.randomUUID();
        autenticarComoDono(donoId);
        Restaurante restaurante = criarRestaurante(donoId);
        Cardapio cardapio = criarCardapio(restaurante.getId());

        itemCardapioRepository.salvar(new ItemCardapio("Existente", "Desc", BigDecimal.TEN, true, "foto.jpg", cardapio.getId()));

        DadosCriacaoItemCardapio dados = new DadosCriacaoItemCardapio(
                "Existente", "Desc duplicada", BigDecimal.valueOf(20), true, "foto2.jpg", cardapio.getId());

        assertThrows(RuntimeException.class, () -> usecase.executar(dados));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é dono do restaurante")
    void deveLancarExcecaoQuandoNaoEDono() {
        UUID donoId = UUID.randomUUID();
        UUID outroId = UUID.randomUUID();
        Restaurante restaurante = criarRestaurante(donoId);
        Cardapio cardapio = criarCardapio(restaurante.getId());

        autenticarComoDono(outroId);

        DadosCriacaoItemCardapio dados = new DadosCriacaoItemCardapio(
                "Item Inválido", "Desc", BigDecimal.valueOf(10), true, "foto.jpg", cardapio.getId());

        assertThrows(CriacaoItemNaoAutorizadaException.class, () -> usecase.executar(dados));
    }
}
