package com.github.lilianjaf.restaurante_service.infra.config;

import com.github.lilianjaf.restaurante_service.core.api.RestauranteModuleFacade;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.restaurante_service.core.rules.*;
import com.github.lilianjaf.restaurante_service.core.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RestauranteConfig {

    @Bean
    public CriarRestauranteUseCase criarRestauranteUseCase(RestauranteRepository restauranteRepository,
                                                           ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway,
                                                           TransactionGateway transactionGateway) {
        List<ValidadorCriacaoRestauranteRule> permissaoRules = List.of(
                new ApenasDonoPodeCriarRestauranteRule()
        );
        List<ValidadorCriacaoRestauranteRule> rules = List.of();
        return new CriarRestauranteUseCaseImpl(restauranteRepository, obterUsuarioLogadoRestauranteGateway, transactionGateway, permissaoRules, rules);
    }

    @Bean
    public BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase(RestauranteRepository restauranteRepository,
                                                                     ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway) {
        List<BuscarRestauranteRule> permissaoRules = List.of();
        List<BuscarRestauranteRule> rules = List.of();
        return new BuscarRestaurantePorIdUseCaseImpl(restauranteRepository, obterUsuarioLogadoRestauranteGateway, permissaoRules, rules);
    }

    @Bean
    public ListarRestaurantesUseCase listarRestaurantesUseCase(RestauranteRepository restauranteRepository,
                                                             ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway) {
        List<ListarRestaurantesRule> permissaoRules = List.of();
        List<ListarRestaurantesRule> rules = List.of();
        return new ListarRestaurantesUseCaseImpl(restauranteRepository, obterUsuarioLogadoRestauranteGateway, permissaoRules, rules);
    }

    @Bean
    public AtualizarRestauranteUseCase atualizarRestauranteUseCase(RestauranteRepository restauranteRepository,
                                                                 ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway,
                                                                 TransactionGateway transactionGateway) {
        List<AtualizarRestauranteRule> permissaoRules = List.of(
                new ApenasDonoDoRestaurantePodeAtualizarRule()
        );
        List<AtualizarRestauranteRule> rules = List.of(
                new RestauranteDeveTerDonoVinculadoRule()
        );
        return new AtualizarRestauranteUseCaseImpl(restauranteRepository, obterUsuarioLogadoRestauranteGateway, transactionGateway, permissaoRules, rules);
    }

    @Bean
    public InativarRestauranteUseCase inativarRestauranteUseCase(RestauranteRepository restauranteRepository,
                                                                 ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway,
                                                                 TransactionGateway transactionGateway) {
        List<InativarRestauranteRule> permissaoRules = List.of(
                new ApenasDonoPodeInativarProprioRestauranteRule()
        );
        List<InativarRestauranteRule> rules = List.of(
                new RestauranteDeveEstarAtivoRule()
        );
        return new InativarRestauranteUseCaseImpl(restauranteRepository, obterUsuarioLogadoRestauranteGateway, transactionGateway, permissaoRules, rules);
    }

    @Bean
    public RestauranteModuleFacade restauranteModuleFacade(RestauranteRepository restauranteRepository) {
        return new RestauranteModuleFacadeImpl(restauranteRepository);
    }
}
