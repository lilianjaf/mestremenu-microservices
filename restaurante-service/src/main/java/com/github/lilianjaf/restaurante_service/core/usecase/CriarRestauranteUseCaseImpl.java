package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoRestaurante;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.restaurante_service.core.rules.CriacaoRestauranteContext;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorCriacaoRestauranteRule;

import java.util.List;

public class CriarRestauranteUseCaseImpl implements CriarRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorCriacaoRestauranteRule> permissaoRules;
    private final List<ValidadorCriacaoRestauranteRule> rules;

    public CriarRestauranteUseCaseImpl(RestauranteRepository restauranteRepository,
                                       ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway,
                                       TransactionGateway transactionGateway,
                                       List<ValidadorCriacaoRestauranteRule> permissaoRules,
                                       List<ValidadorCriacaoRestauranteRule> rules) {
        this.restauranteRepository = restauranteRepository;
        this.obterUsuarioLogadoRestauranteGateway = obterUsuarioLogadoRestauranteGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public Restaurante executar(DadosCriacaoRestaurante dados) {
        Usuario usuarioLogado = obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        CriacaoRestauranteContext context = new CriacaoRestauranteContext(usuarioLogado, dados);

        return transactionGateway.execute(() -> {
            permissaoRules.forEach(rule -> rule.validar(context));
            rules.forEach(rule -> rule.validar(context));

            Restaurante restaurante = new Restaurante(
                    dados.nome(),
                    dados.endereco(),
                    dados.tipoCozinha(),
                    dados.horarioFuncionamento(),
                    usuarioLogado.getId()
            );

            return restauranteRepository.salvar(restaurante);
        });
    }
}
