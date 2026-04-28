package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.DeletarItemCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.*;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorPermissaoCardapioRule;

import java.util.List;
import java.util.UUID;

public class DeletarItemCardapioUseCaseImpl implements DeletarItemCardapioUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final CardapioRepository cardapioRepository;
    private final RestauranteGateway restauranteGateway;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorPermissaoCardapioRule<DeletarItemCardapioRuleContextDto>> permissaoRules;
    private final List<ValidadorPermissaoCardapioRule<DeletarItemCardapioRuleContextDto>> rules;

    public DeletarItemCardapioUseCaseImpl(ItemCardapioRepository itemCardapioRepository,
                                         CardapioRepository cardapioRepository,
                                         RestauranteGateway restauranteGateway,
                                         ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                         TransactionGateway transactionGateway,
                                         List<ValidadorPermissaoCardapioRule<DeletarItemCardapioRuleContextDto>> permissaoRules,
                                         List<ValidadorPermissaoCardapioRule<DeletarItemCardapioRuleContextDto>> rules) {
        this.itemCardapioRepository = itemCardapioRepository;
        this.cardapioRepository = cardapioRepository;
        this.restauranteGateway = restauranteGateway;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public void executar(UUID idItem) {
        ItemCardapio item = itemCardapioRepository.findById(idItem)
                .orElseThrow(() -> new CardapioException("Item do cardápio não encontrado para exclusão."));

        var cardapio = cardapioRepository.findById(item.getIdCardapio())
                .orElseThrow(() -> new CardapioException("Cardápio associado não encontrado."));

        Restaurante restaurante = restauranteGateway.buscarPorId(cardapio.getIdRestaurante())
                .orElseThrow(() -> new CardapioException("Restaurante não encontrado."));

        Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        boolean isItemDoProprioRestaurante = restaurante.getIdDono().equals(usuarioLogado.getId());

        DeletarItemCardapioRuleContextDto context = new DeletarItemCardapioRuleContextDto(
                usuarioLogado,
                restaurante,
                item,
                isItemDoProprioRestaurante
        );

        transactionGateway.execute(() -> {
            permissaoRules.forEach(r -> r.validar(context));
            rules.forEach(r -> r.validar(context));

            itemCardapioRepository.deletar(idItem);
        });
    }
}
