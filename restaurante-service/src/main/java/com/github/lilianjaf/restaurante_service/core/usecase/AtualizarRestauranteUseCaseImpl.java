package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.restaurante.core.dto.DadosAtualizacaoRestaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.DomainException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.ObterUsuarioLogadoRestauranteGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.RestauranteRepository;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.TransactionGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.AtualizarRestauranteRule;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.AtualizarRestauranteRuleContextDto;

import java.util.List;
import java.util.UUID;

public class AtualizarRestauranteUseCaseImpl implements AtualizarRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway;
    private final TransactionGateway transactionGateway;
    private final List<AtualizarRestauranteRule> permissaoRules;
    private final List<AtualizarRestauranteRule> rules;

    public AtualizarRestauranteUseCaseImpl(RestauranteRepository restauranteRepository,
                                           ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway,
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
                .orElseThrow(() -> new DomainException("Restaurante não encontrado."));

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
