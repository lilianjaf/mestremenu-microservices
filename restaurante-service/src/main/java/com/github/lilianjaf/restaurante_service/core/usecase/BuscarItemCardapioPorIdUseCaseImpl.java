package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.BuscarItemCardapioPorIdRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.ItemCardapioRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorPermissaoCardapioRule;

import java.util.List;
import java.util.UUID;

public class BuscarItemCardapioPorIdUseCaseImpl implements BuscarItemCardapioPorIdUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorPermissaoCardapioRule<BuscarItemCardapioPorIdRuleContextDto>> permissaoRules;

    public BuscarItemCardapioPorIdUseCaseImpl(ItemCardapioRepository itemCardapioRepository,
                                             ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                             TransactionGateway transactionGateway,
                                             List<ValidadorPermissaoCardapioRule<BuscarItemCardapioPorIdRuleContextDto>> permissaoRules) {
        this.itemCardapioRepository = itemCardapioRepository;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
    }

    @Override
    public ItemCardapio executar(UUID id) {
        Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        BuscarItemCardapioPorIdRuleContextDto context = new BuscarItemCardapioPorIdRuleContextDto(usuarioLogado);

        return transactionGateway.execute(() -> {
            permissaoRules.forEach(rule -> rule.validar(context));

            return itemCardapioRepository.findById(id)
                    .orElseThrow(() -> new CardapioException("Item do cardápio não encontrado."));
        });
    }
}
