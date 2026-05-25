package com.github.lilianjaf.restaurante_service.core.usecase;

import test.TestRestauranteServiceApplication;
import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Endereco;
import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.exception.AlteracaoItemNaoAutorizadaException;
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
class AlterarItemCardapioUseCaseIT {

    @Autowired
    private AlterarItemCardapioUseCase usecase;

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

    private ItemCardapio criarItem(UUID idCardapio) {
        return itemCardapioRepository.salvar(new ItemCardapio("Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", idCardapio));
    }

    @Test
    @DisplayName("Deve alterar item do cardápio com sucesso")
    void deveAlterarItemComSucesso() {
        UUID donoId = autenticarComo(UUID.randomUUID());
        Restaurante restaurante = criarRestaurante(donoId);
        Cardapio cardapio = criarCardapio(restaurante.getId());
        ItemCardapio item = criarItem(cardapio.getId());

        DadosAtualizacaoItemCardapio dados = new DadosAtualizacaoItemCardapio(
                item.getId(), "Burger Duplo", "Com dois patties", BigDecimal.valueOf(40), true, "burger2.jpg");

        ItemCardapio resultado = usecase.executar(dados);

        assertEquals("Burger Duplo", resultado.getNome());
        assertEquals(BigDecimal.valueOf(40), resultado.getPreco());
        ItemCardapio persistido = itemCardapioRepository.findById(resultado.getId()).orElseThrow();
        assertEquals("Burger Duplo", persistido.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não é dono do restaurante do item")
    void deveLancarExcecaoQuandoNaoEDono() {
        UUID donoId = UUID.randomUUID();
        UUID outroId = UUID.randomUUID();
        Restaurante restaurante = criarRestaurante(donoId);
        Cardapio cardapio = criarCardapio(restaurante.getId());
        ItemCardapio item = criarItem(cardapio.getId());

        autenticarComo(outroId);

        DadosAtualizacaoItemCardapio dados = new DadosAtualizacaoItemCardapio(
                item.getId(), "Nome Inválido", "Desc", BigDecimal.valueOf(10), true, "foto.jpg");

        assertThrows(AlteracaoItemNaoAutorizadaException.class, () -> usecase.executar(dados));
    }
}
