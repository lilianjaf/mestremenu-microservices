package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Cardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.BuscarCardapioPorRestauranteRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.CardapioRepository;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.TransactionGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.rules.ValidadorPermissaoCardapioRule;

import java.util.List;
import java.util.UUID;

public class BuscarCardapioPorRestauranteUseCaseImpl implements BuscarCardapioPorRestauranteUseCase {

    private final CardapioRepository cardapioRepository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorPermissaoCardapioRule<BuscarCardapioPorRestauranteRuleContextDto>> permissaoRules;

    public BuscarCardapioPorRestauranteUseCaseImpl(CardapioRepository cardapioRepository,
                                                  ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                  TransactionGateway transactionGateway,
                                                  List<ValidadorPermissaoCardapioRule<BuscarCardapioPorRestauranteRuleContextDto>> permissaoRules) {
        this.cardapioRepository = cardapioRepository;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
    }

    @Override
    public List<Cardapio> executar(UUID idRestaurante) {
        Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        BuscarCardapioPorRestauranteRuleContextDto context = new BuscarCardapioPorRestauranteRuleContextDto(usuarioLogado);

        return transactionGateway.execute(() -> {
            permissaoRules.forEach(rule -> rule.validar(context));

            return cardapioRepository.buscarPorIdRestaurante(idRestaurante);
        });
    }
}
