package com.github.lilianjaf.restaurante_service.core.usecase;

import test.TestRestauranteServiceApplication;
import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Endereco;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.exception.CriacaoCardapioNaoAutorizadaException;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestRestauranteServiceApplication.class)
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class CriarCardapioUseCaseIT {

    @Autowired
    private CriarCardapioUseCase usecase;

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

    @Test
    @DisplayName("Deve criar e persistir um cardápio com sucesso quando os dados são válidos")
    void deveCriarEPersistirCardapioComSucesso() {
        UUID donoId = autenticarComo(UUID.randomUUID());
        Restaurante restaurante = criarRestaurante(donoId);

        DadosCriacaoItemCardapio itemDto = new DadosCriacaoItemCardapio(
                "Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", null);
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu Principal", restaurante.getId(), List.of(itemDto));

        Cardapio resultado = usecase.executar(dados);

        assertNotNull(resultado.getId());
        Cardapio persistido = cardapioRepository.findById(resultado.getId()).orElseThrow();
        assertEquals("Menu Principal", persistido.getNome());
        assertEquals(restaurante.getId(), persistido.getIdRestaurante());
        assertEquals(1, persistido.getItens().size());
        assertEquals("Burger", persistido.getItens().get(0).getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção e não persistir quando o usuário não for o dono do restaurante")
    void deveLancarExcecaoQuandoUsuarioNaoForDono() {
        UUID donoId = UUID.randomUUID();
        UUID outroId = UUID.randomUUID();
        Restaurante restaurante = criarRestaurante(donoId);
        autenticarComo(outroId); // outro usuário, não é o dono do restaurante

        DadosCriacaoItemCardapio itemDto = new DadosCriacaoItemCardapio(
                "Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", null);
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu Invalido", restaurante.getId(), List.of(itemDto));

        assertThrows(CriacaoCardapioNaoAutorizadaException.class, () -> usecase.executar(dados));

        boolean existe = cardapioRepository.existeNomeParaRestaurante("Menu Invalido", restaurante.getId());
        assertFalse(existe);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome do cardápio já existir para o restaurante")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        UUID donoId = autenticarComo(UUID.randomUUID());
        Restaurante restaurante = criarRestaurante(donoId);

        DadosCriacaoItemCardapio itemDto = new DadosCriacaoItemCardapio(
                "Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", null);
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu Duplicado", restaurante.getId(), List.of(itemDto));

        usecase.executar(dados);

        assertThrows(RuntimeException.class, () -> usecase.executar(dados));
    }
}
