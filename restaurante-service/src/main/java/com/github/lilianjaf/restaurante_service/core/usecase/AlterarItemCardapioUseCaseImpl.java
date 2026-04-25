package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.ItemCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.AlterarItemCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosAtualizacaoItemCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CardapioException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.*;
import com.github.lilianjaf.mestremenuclean.cardapio.core.rules.ValidadorItemCardapioRule;
import com.github.lilianjaf.mestremenuclean.cardapio.core.rules.ValidadorPermissaoItemCardapioRule;

import java.util.List;

public class AlterarItemCardapioUseCaseImpl implements AlterarItemCardapioUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final CardapioRepository cardapioRepository;
    private final RestauranteGateway restauranteGateway;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorPermissaoItemCardapioRule<ItemCardapioRuleContext>> permissaoRules;
    private final List<ValidadorItemCardapioRule<ItemCardapioRuleContext>> rules;

    public AlterarItemCardapioUseCaseImpl(ItemCardapioRepository itemCardapioRepository,
                                          CardapioRepository cardapioRepository,
                                          RestauranteGateway restauranteGateway,
                                          ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                          TransactionGateway transactionGateway,
                                          List<ValidadorPermissaoItemCardapioRule<ItemCardapioRuleContext>> permissaoRules,
                                          List<ValidadorItemCardapioRule<ItemCardapioRuleContext>> rules) {
        this.itemCardapioRepository = itemCardapioRepository;
        this.cardapioRepository = cardapioRepository;
        this.restauranteGateway = restauranteGateway;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public ItemCardapio executar(DadosAtualizacaoItemCardapio dados) {
        ItemCardapio item = itemCardapioRepository.findById(dados.idItemCardapio())
                .orElseThrow(() -> new CardapioException("Item do cardápio não encontrado."));

        var cardapio = cardapioRepository.findById(item.getIdCardapio())
                .orElseThrow(() -> new CardapioException("Cardápio associado não encontrado."));

        var restauranteOpt = restauranteGateway.buscarPorId(cardapio.getIdRestaurante());
        
        Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        boolean isUsuarioDonoDoRestauranteDoItem = restauranteOpt
                .map(r -> r.getIdDono().equals(usuarioLogado.getId()))
                .orElse(false);

        boolean isNomeUnico = !itemCardapioRepository.existeNomeNoCardapioExcetoId(
                dados.nome(), 
                item.getIdCardapio(), 
                item.getId()
        );

        AlterarItemCardapioRuleContextDto context = new AlterarItemCardapioRuleContextDto(
                usuarioLogado,
                restauranteOpt.orElse(null),
                item,
                dados,
                isUsuarioDonoDoRestauranteDoItem,
                restauranteOpt.isPresent(),
                isNomeUnico
        );

        return transactionGateway.execute(() -> {
            permissaoRules.forEach(r -> r.validar(context));
            rules.forEach(r -> r.validar(context));

            item.atualizar(
                    dados.nome(),
                    dados.descricao(),
                    dados.preco(),
                    dados.disponibilidadeRestaurante(),
                    dados.caminhoFoto()
            );

            return itemCardapioRepository.salvar(item);
        });
    }
}
