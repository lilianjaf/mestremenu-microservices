package com.github.lilianjaf.restaurante_service.infra.config;

import com.github.lilianjaf.mestremenuclean.restaurante.core.api.RestauranteModuleFacade;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.ObterUsuarioLogadoRestauranteGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.RestauranteRepository;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.TransactionGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.UsuarioGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.*;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.UsuarioDeveEstarAutenticadoRule;
import com.github.lilianjaf.mestremenuclean.restaurante.core.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RestauranteConfig {

    @Bean
    public CriarRestauranteUseCase criarRestauranteUseCase(RestauranteRepository restauranteRepository,
                                                           UsuarioGateway usuarioGateway,
                                                           ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway,
                                                           TransactionGateway transactionGateway) {
        List<ValidadorCriacaoRestauranteRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoRule(),
                new ApenasDonoPodeCriarRestauranteRule()
        );
        List<ValidadorCriacaoRestauranteRule> rules = List.of(
                new RestauranteDeveTerDonoVinculadoRule()
        );
        return new CriarRestauranteUseCaseImpl(restauranteRepository, usuarioGateway, obterUsuarioLogadoRestauranteGateway, transactionGateway, permissaoRules, rules);
    }

    @Bean
    public BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase(RestauranteRepository restauranteRepository,
                                                                     ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway) {
        List<BuscarRestauranteRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoRule()
        );
        List<BuscarRestauranteRule> rules = List.of();
        return new BuscarRestaurantePorIdUseCaseImpl(restauranteRepository, obterUsuarioLogadoRestauranteGateway, permissaoRules, rules);
    }

    @Bean
    public ListarRestaurantesUseCase listarRestaurantesUseCase(RestauranteRepository restauranteRepository,
                                                             ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway) {
        List<ListarRestaurantesRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoRule()
        );
        List<ListarRestaurantesRule> rules = List.of();
        return new ListarRestaurantesUseCaseImpl(restauranteRepository, obterUsuarioLogadoRestauranteGateway, permissaoRules, rules);
    }

    @Bean
    public AtualizarRestauranteUseCase atualizarRestauranteUseCase(RestauranteRepository restauranteRepository,
                                                                 ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway,
                                                                 TransactionGateway transactionGateway) {
        List<AtualizarRestauranteRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoRule(),
                new ApenasDonoDoRestaurantePodeAtualizarRule()
        );
        List<AtualizarRestauranteRule> rules = List.of(
                new RestauranteDeveTerDonoVinculadoRule()
        );
        return new AtualizarRestauranteUseCaseImpl(restauranteRepository, obterUsuarioLogadoRestauranteGateway, transactionGateway, permissaoRules, rules);
    }

    @Bean
    public InativarRestauranteUseCase inativarRestauranteUseCase(RestauranteRepository restauranteRepository,
                                                                 ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway,
                                                                 TransactionGateway transactionGateway) {
        List<InativarRestauranteRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoRule(),
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
