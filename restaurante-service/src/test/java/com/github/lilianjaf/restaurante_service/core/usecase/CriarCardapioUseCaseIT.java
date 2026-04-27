package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Cardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosCriacaoCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosCriacaoItemCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CriacaoCardapioNaoAutorizadaException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.CardapioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@Sql("/sql/setup_cardapio_test.sql")
class CriarCardapioUseCaseIT {

    @Autowired
    private CriarCardapioUseCase usecase;

    @Autowired
    private CardapioRepository cardapioRepository;

    private final UUID idDono = UUID.fromString("8c9c7e0c-84d4-4a4e-862d-0b70c3c54d3d");
    private final UUID idRestaurante = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final String username = "admin";

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, null, List.of())
        );
    }

    @Test
    @DisplayName("Deve criar e persistir um cardápio com sucesso quando os dados são válidos")
    void deveCriarEPersistirCardapioComSucesso() {
        DadosCriacaoItemCardapio itemDto = new DadosCriacaoItemCardapio(
                "Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", null);
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu Principal", idRestaurante, List.of(itemDto));

        Cardapio resultado = usecase.executar(dados);

        assertNotNull(resultado.getId());
        Cardapio persistido = cardapioRepository.findById(resultado.getId()).orElseThrow();
        assertEquals("Menu Principal", persistido.getNome());
        assertEquals(idRestaurante, persistido.getIdRestaurante());
        assertEquals(1, persistido.getItens().size());
        assertEquals("Burger", persistido.getItens().get(0).getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção e não persistir quando o usuário não for o dono do restaurante")
    void deveLancarExcecaoQuandoUsuarioNaoForDono() {
        // Altera o usuário para um que não é dono (pode ser um usuário aleatório mas autenticado)
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("outro.usuario", null, List.of())
        );

        DadosCriacaoItemCardapio itemDto = new DadosCriacaoItemCardapio(
                "Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", null);
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu Invalido", idRestaurante, List.of(itemDto));

        assertThrows(CriacaoCardapioNaoAutorizadaException.class, () -> usecase.executar(dados));
        
        boolean existe = cardapioRepository.existeNomeParaRestaurante("Menu Invalido", idRestaurante);
        assertFalse(existe);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome do cardápio já existir para o restaurante")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        DadosCriacaoItemCardapio itemDto = new DadosCriacaoItemCardapio(
                "Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", null);
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu Duplicado", idRestaurante, List.of(itemDto));

        usecase.executar(dados);

        assertThrows(RuntimeException.class, () -> usecase.executar(dados));
    }
}

