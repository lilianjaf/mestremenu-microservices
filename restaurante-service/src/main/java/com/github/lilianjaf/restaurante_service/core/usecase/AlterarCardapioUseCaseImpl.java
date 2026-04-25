package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Cardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.ItemCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosAtualizacaoCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CardapioException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.CardapioRepository;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.RestauranteGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.TransactionGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.rules.*;

import java.util.List;
import java.util.stream.Collectors;

public class AlterarCardapioUseCaseImpl implements AlterarCardapioUseCase {

    private final CardapioRepository cardapioRepository;
    private final RestauranteGateway restauranteGateway;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorPermissaoCardapioRule<AlterarCardapioRuleContextDto>> permissaoRules;
    private final List<ValidadorCardapioRule<AlterarCardapioRuleContextDto>> rules;

    public AlterarCardapioUseCaseImpl(CardapioRepository cardapioRepository,
                                      RestauranteGateway restauranteGateway,
                                      ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                      TransactionGateway transactionGateway,
                                      List<ValidadorPermissaoCardapioRule<AlterarCardapioRuleContextDto>> permissaoRules,
                                      List<ValidadorCardapioRule<AlterarCardapioRuleContextDto>> rules) {
        this.cardapioRepository = cardapioRepository;
        this.restauranteGateway = restauranteGateway;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public Cardapio executar(DadosAtualizacaoCardapio dados) {
        Cardapio cardapio = cardapioRepository.findById(dados.idCardapio())
                .orElseThrow(() -> new CardapioException("Cardápio não encontrado."));

        Restaurante restaurante = restauranteGateway.buscarPorId(cardapio.getIdRestaurante())
                .orElseThrow(() -> new CardapioException("Restaurante do cardápio não encontrado."));

        Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        boolean isCardapioDoProprioRestaurante = restaurante.getIdDono().equals(usuarioLogado.getId());
        
        boolean isNomeUnico = true;
        if (dados.nome() != null && !dados.nome().equalsIgnoreCase(cardapio.getNome())) {
            isNomeUnico = !cardapioRepository.existeNomeParaRestaurante(dados.nome(), cardapio.getIdRestaurante());
        }

        AlterarCardapioRuleContextDto context = new AlterarCardapioRuleContextDto(
                usuarioLogado, restaurante, cardapio, dados, isCardapioDoProprioRestaurante, isNomeUnico);

        return transactionGateway.execute(() -> {
            permissaoRules.forEach(r -> r.validar(context));
            rules.forEach(r -> r.validar(context));

            List<ItemCardapio> novosItens = null;
            if (dados.itens() != null) {
                novosItens = dados.itens().stream()
                        .map(item -> new ItemCardapio(
                                item.nome(),
                                item.descricao(),
                                item.preco(),
                                item.disponibilidadeRestaurante(),
                                item.caminhoFoto(),
                                cardapio.getId()
                        ))
                        .collect(Collectors.toList());
            }

            cardapio.atualizar(dados.nome(), novosItens);

            return cardapioRepository.salvar(cardapio);
        });
    }
}
