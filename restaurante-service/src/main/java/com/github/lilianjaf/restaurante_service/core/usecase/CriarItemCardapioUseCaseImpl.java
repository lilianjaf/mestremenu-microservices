package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.CriarItemCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.RegistroNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.*;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorItemCardapioRule;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorPermissaoItemCardapioRule;

import java.util.List;

public class CriarItemCardapioUseCaseImpl implements CriarItemCardapioUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final CardapioRepository cardapioRepository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final RestauranteGateway restauranteGateway;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorPermissaoItemCardapioRule<ItemCardapioRuleContext>> permissionRules;
    private final List<ValidadorItemCardapioRule<ItemCardapioRuleContext>> rules;

    public CriarItemCardapioUseCaseImpl(ItemCardapioRepository itemCardapioRepository,
                                       CardapioRepository cardapioRepository,
                                       ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                       RestauranteGateway restauranteGateway,
                                       TransactionGateway transactionGateway,
                                       List<ValidadorPermissaoItemCardapioRule<ItemCardapioRuleContext>> permissionRules,
                                       List<ValidadorItemCardapioRule<ItemCardapioRuleContext>> rules) {
        this.itemCardapioRepository = itemCardapioRepository;
        this.cardapioRepository = cardapioRepository;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.restauranteGateway = restauranteGateway;
        this.transactionGateway = transactionGateway;
        this.permissionRules = permissionRules;
        this.rules = rules;
    }

    @Override
    public ItemCardapio executar(DadosCriacaoItemCardapio dados) {
        Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        var cardapio = cardapioRepository.findById(dados.idCardapio())
                .orElseThrow(() -> new RegistroNaoEncontradoException("Cardápio não encontrado."));

        Restaurante restaurante = restauranteGateway.buscarPorId(cardapio.getIdRestaurante())
                .orElse(null);

        boolean nomeUnico = !itemCardapioRepository.existeNomeNoCardapio(dados.nome(), dados.idCardapio());

        CriarItemCardapioRuleContextDto context = new CriarItemCardapioRuleContextDto(usuarioLogado, restaurante, dados, nomeUnico);

        return transactionGateway.execute(() -> {
            permissionRules.forEach(r -> r.validar(context));
            rules.forEach(r -> r.validar(context));

            ItemCardapio item = new ItemCardapio(
                    dados.nome(),
                    dados.descricao(),
                    dados.preco(),
                    dados.disponibilidadeRestaurante(),
                    dados.caminhoFoto(),
                    dados.idCardapio()
            );

            return itemCardapioRepository.salvar(item);
        });
    }
}
