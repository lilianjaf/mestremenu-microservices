package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.ObterUsuarioLogadoRestauranteGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.RestauranteRepository;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.ListarRestaurantesRule;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.ListarRestaurantesRuleContextDto;

import java.util.List;

public class ListarRestaurantesUseCaseImpl implements ListarRestaurantesUseCase {

    private final RestauranteRepository restauranteRepository;
    private final ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway;
    private final List<ListarRestaurantesRule> permissaoRules;
    private final List<ListarRestaurantesRule> rules;

    public ListarRestaurantesUseCaseImpl(RestauranteRepository restauranteRepository,
                                         ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway,
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
