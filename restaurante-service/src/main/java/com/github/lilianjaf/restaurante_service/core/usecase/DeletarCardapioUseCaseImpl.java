package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.DeletarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.CardapioRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorCardapioRule;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorPermissaoCardapioRule;

import java.util.List;
import java.util.UUID;

public class DeletarCardapioUseCaseImpl implements DeletarCardapioUseCase {

    private final CardapioRepository cardapioRepository;
    private final RestauranteGateway restauranteGateway;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorPermissaoCardapioRule<? super DeletarCardapioRuleContextDto>> permissaoRules;
    private final List<ValidadorCardapioRule<? super DeletarCardapioRuleContextDto>> rules;

    public DeletarCardapioUseCaseImpl(CardapioRepository cardapioRepository,
                                     RestauranteGateway restauranteGateway,
                                     ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                     TransactionGateway transactionGateway,
                                     List<ValidadorPermissaoCardapioRule<? super DeletarCardapioRuleContextDto>> permissaoRules,
                                     List<ValidadorCardapioRule<? super DeletarCardapioRuleContextDto>> rules) {
        this.cardapioRepository = cardapioRepository;
        this.restauranteGateway = restauranteGateway;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public void executar(UUID idCardapio) {
        Cardapio cardapio = cardapioRepository.findById(idCardapio)
                .orElseThrow(() -> new CardapioException("Cardápio não encontrado para exclusão."));

        Restaurante restaurante = restauranteGateway.buscarPorId(cardapio.getIdRestaurante())
                .orElseThrow(() -> new CardapioException("Restaurante do cardápio não encontrado."));

        Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        boolean isDonoDoRestaurante = restaurante.getIdDono().equals(usuarioLogado.getId());

        DeletarCardapioRuleContextDto context = new DeletarCardapioRuleContextDto(
                usuarioLogado,
                restaurante,
                cardapio,
                isDonoDoRestaurante
        );

        transactionGateway.execute(() -> {
            permissaoRules.forEach(r -> r.validar(context));
            rules.forEach(r -> r.validar(context));

            cardapioRepository.deletar(idCardapio);
        });
    }
}
