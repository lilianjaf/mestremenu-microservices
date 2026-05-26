package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.CriarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.exception.RegistroNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.CardapioRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.restaurante_service.core.rules.*;

import java.util.List;
import java.util.stream.Collectors;

public class CriarCardapioUseCaseImpl implements CriarCardapioUseCase {

        private final CardapioRepository cardapioRepository;
        private final RestauranteGateway restauranteGateway;
        private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
        private final TransactionGateway transactionGateway;
        private final List<ValidadorPermissaoCardapioRule<? super CriarCardapioRuleContextDto>> permissionRules;
        private final List<ValidadorCardapioRule<? super CriarCardapioRuleContextDto>> rules;

        public CriarCardapioUseCaseImpl(CardapioRepository cardapioRepository,
                        RestauranteGateway restauranteGateway,
                        ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                        TransactionGateway transactionGateway,
                        List<ValidadorPermissaoCardapioRule<? super CriarCardapioRuleContextDto>> permissionRules,
                        List<ValidadorCardapioRule<? super CriarCardapioRuleContextDto>> rules) {
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
                                .orElseThrow(() -> new RegistroNaoEncontradoException("Restaurante não encontrado."));

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
