package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.DomainException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.ObterUsuarioLogadoRestauranteGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.RestauranteRepository;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.BuscarRestauranteRule;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.BuscarRestauranteRuleContextDto;

import java.util.List;
import java.util.UUID;

public class BuscarRestaurantePorIdUseCaseImpl implements BuscarRestaurantePorIdUseCase {

    private final RestauranteRepository restauranteRepository;
    private final ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway;
    private final List<BuscarRestauranteRule> permissaoRules;
    private final List<BuscarRestauranteRule> rules;

    public BuscarRestaurantePorIdUseCaseImpl(RestauranteRepository restauranteRepository,
                                           ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway,
                                           List<BuscarRestauranteRule> permissaoRules,
                                           List<BuscarRestauranteRule> rules) {
        this.restauranteRepository = restauranteRepository;
        this.obterUsuarioLogadoRestauranteGateway = obterUsuarioLogadoRestauranteGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public Restaurante executar(UUID id) {
        Usuario usuarioLogado = obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        if (id == null) {
            throw new DomainException("ID do restaurante não pode ser nulo.");
        }

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new DomainException("Restaurante não encontrado."));

        BuscarRestauranteRuleContextDto context = new BuscarRestauranteRuleContextDto(usuarioLogado, restaurante);

        permissaoRules.forEach(rule -> rule.validar(context));
        rules.forEach(rule -> rule.validar(context));

        return restaurante;
    }
}
