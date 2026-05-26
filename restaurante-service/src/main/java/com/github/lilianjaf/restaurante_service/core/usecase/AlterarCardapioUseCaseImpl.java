package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.exception.RegistroNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.CardapioRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.restaurante_service.core.rules.*;

import java.util.List;
import java.util.stream.Collectors;

public class AlterarCardapioUseCaseImpl implements AlterarCardapioUseCase {

    private final CardapioRepository cardapioRepository;
    private final RestauranteGateway restauranteGateway;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorPermissaoCardapioRule<? super AlterarCardapioRuleContextDto>> permissaoRules;
    private final List<ValidadorCardapioRule<? super AlterarCardapioRuleContextDto>> rules;

    public AlterarCardapioUseCaseImpl(CardapioRepository cardapioRepository,
                                      RestauranteGateway restauranteGateway,
                                      ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                      TransactionGateway transactionGateway,
                                      List<ValidadorPermissaoCardapioRule<? super AlterarCardapioRuleContextDto>> permissaoRules,
                                      List<ValidadorCardapioRule<? super AlterarCardapioRuleContextDto>> rules) {
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
                .orElseThrow(() -> new RegistroNaoEncontradoException("Cardápio não encontrado."));

        Restaurante restaurante = restauranteGateway.buscarPorId(cardapio.getIdRestaurante())
                .orElseThrow(() -> new RegistroNaoEncontradoException("Restaurante do cardápio não encontrado."));

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
