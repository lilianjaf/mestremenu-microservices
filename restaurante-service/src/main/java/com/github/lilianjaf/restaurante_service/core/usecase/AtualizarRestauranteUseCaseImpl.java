package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoRestaurante;
import com.github.lilianjaf.restaurante_service.core.exception.RegistroNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.restaurante_service.core.rules.AtualizarRestauranteRule;
import com.github.lilianjaf.restaurante_service.core.rules.AtualizarRestauranteRuleContextDto;

import java.util.List;
import java.util.UUID;

public class AtualizarRestauranteUseCaseImpl implements AtualizarRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway;
    private final TransactionGateway transactionGateway;
    private final List<AtualizarRestauranteRule> permissaoRules;
    private final List<AtualizarRestauranteRule> rules;

    public AtualizarRestauranteUseCaseImpl(RestauranteRepository restauranteRepository,
                                           ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway,
                                           TransactionGateway transactionGateway,
                                           List<AtualizarRestauranteRule> permissaoRules,
                                           List<AtualizarRestauranteRule> rules) {
        this.restauranteRepository = restauranteRepository;
        this.obterUsuarioLogadoRestauranteGateway = obterUsuarioLogadoRestauranteGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public Restaurante executar(UUID id, DadosAtualizacaoRestaurante dados) {
        Usuario usuarioLogado = obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Restaurante não encontrado."));

        AtualizarRestauranteRuleContextDto context = new AtualizarRestauranteRuleContextDto(usuarioLogado, restaurante);

        return transactionGateway.execute(() -> {
            permissaoRules.forEach(rule -> rule.validar(context));
            rules.forEach(rule -> rule.validar(context));

            restaurante.atualizar(
                    dados.nome(),
                    dados.endereco(),
                    dados.tipoCozinha(),
                    dados.horarioFuncionamento()
            );

            return restauranteRepository.salvar(restaurante);
        });
    }
}
