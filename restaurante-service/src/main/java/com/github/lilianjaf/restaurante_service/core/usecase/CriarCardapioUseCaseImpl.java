package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Cardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.ItemCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.CriarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosCriacaoCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CardapioException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.CardapioRepository;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.RestauranteGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.TransactionGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.rules.*;

import java.util.List;
import java.util.stream.Collectors;

public class CriarCardapioUseCaseImpl implements CriarCardapioUseCase {

        private final CardapioRepository cardapioRepository;
        private final RestauranteGateway restauranteGateway;
        private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
        private final TransactionGateway transactionGateway;
        private final List<ValidadorPermissaoCardapioRule<CriarCardapioRuleContextDto>> permissionRules;
        private final List<ValidadorCardapioRule<CriarCardapioRuleContextDto>> rules;

        public CriarCardapioUseCaseImpl(CardapioRepository cardapioRepository,
                        RestauranteGateway restauranteGateway,
                        ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                        TransactionGateway transactionGateway,
                        List<ValidadorPermissaoCardapioRule<CriarCardapioRuleContextDto>> permissionRules,
                        List<ValidadorCardapioRule<CriarCardapioRuleContextDto>> rules) {
                this.cardapioRepository = cardapioRepository;
                this.restauranteGateway = restauranteGateway;
                this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
                this.transactionGateway = transactionGateway;
                this.permissionRules = permissionRules;
                this.rules = rules;
        }

        @Override
        public Cardapio executar(DadosCriacaoCardapio dados) {
                Usuario usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

                Restaurante restaurante = restauranteGateway.buscarPorId(dados.idRestaurante())
                                .orElseThrow(() -> new CardapioException("Restaurante não encontrado."));

                boolean nomeUnico = !cardapioRepository.existeNomeParaRestaurante(dados.nome(), dados.idRestaurante());

                CriarCardapioRuleContextDto context = new CriarCardapioRuleContextDto(usuarioLogado, restaurante, dados,
                                nomeUnico);

                return transactionGateway.execute(() -> {
                        permissionRules.forEach(r -> r.validar(context));
                        rules.forEach(r -> r.validar(context));

                        Cardapio cardapio = new Cardapio(dados.nome(), dados.idRestaurante(), null);

                        List<ItemCardapio> itensToCreate = dados.itens().stream()
                                        .map(itemDados -> new ItemCardapio(
                                                        itemDados.nome(),
                                                        itemDados.descricao(),
                                                        itemDados.preco(),
                                                        itemDados.disponibilidadeRestaurante(),
                                                        itemDados.caminhoFoto(),
                                                        cardapio.getId()))
                                        .collect(Collectors.toList());

                        cardapio.atualizar(cardapio.getNome(), itensToCreate);

                        return cardapioRepository.salvar(cardapio);
                });
        }
}
