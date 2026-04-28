package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteRepository;
import com.github.lilianjaf.restaurante_service.core.rules.ListarRestaurantesRule;
import com.github.lilianjaf.restaurante_service.core.rules.ListarRestaurantesRuleContextDto;

import java.util.List;

public class ListarRestaurantesUseCaseImpl implements ListarRestaurantesUseCase {

    private final RestauranteRepository restauranteRepository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway;
    private final List<ListarRestaurantesRule> permissaoRules;
    private final List<ListarRestaurantesRule> rules;

    public ListarRestaurantesUseCaseImpl(RestauranteRepository restauranteRepository,
                                         ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway,
                                         List<ListarRestaurantesRule> permissaoRules,
                                         List<ListarRestaurantesRule> rules) {
        this.restauranteRepository = restauranteRepository;
        this.obterUsuarioLogadoRestauranteGateway = obterUsuarioLogadoRestauranteGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public List<Restaurante> executar() {
        Usuario usuarioLogado = obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        ListarRestaurantesRuleContextDto context = new ListarRestaurantesRuleContextDto(null);

        permissaoRules.forEach(rule -> rule.validar(context));
        rules.forEach(rule -> rule.validar(context));

        return restauranteRepository.buscarTodos();
    }
}
