package com.github.lilianjaf.restaurante_service.infra.config;

import com.github.lilianjaf.restaurante_service.core.dto.*;
import com.github.lilianjaf.restaurante_service.core.gateway.*;
import com.github.lilianjaf.restaurante_service.core.rules.*;
import com.github.lilianjaf.restaurante_service.core.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CardapioConfig {

    @Bean
    public CriarItemCardapioUseCase criarItemCardapioUseCase(ItemCardapioRepository itemRepository,
                                                           CardapioRepository cardapioRepository,
                                                           ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                           RestauranteGateway restauranteGateway,
                                                           TransactionGateway transactionGateway) {
        List<ValidadorPermissaoItemCardapioRule<ItemCardapioRuleContext>> permissionRules = new ArrayList<>();
        permissionRules.add(new ApenasDonoPodeCriarItemParaProprioRestauranteRule());
        permissionRules.add(new ItemDeveTerRestauranteVinculadoRule());

        List<ValidadorItemCardapioRule<ItemCardapioRuleContext>> businessRules = new ArrayList<>();
        businessRules.add(new CamposObrigatoriosItemRule());
        businessRules.add(new NomeItemDeveSerUnicoNoRestauranteRule());
        businessRules.add(new PrecoItemDeveSerMaiorQueZeroRule());

        return new CriarItemCardapioUseCaseImpl(itemRepository, cardapioRepository, obterUsuarioLogadoGateway, restauranteGateway, transactionGateway, permissionRules, businessRules);
    }

    @Bean
    public AlterarItemCardapioUseCase alterarItemCardapioUseCase(ItemCardapioRepository itemRepository,
                                                                 CardapioRepository cardapioRepository,
                                                                 RestauranteGateway restauranteGateway,
                                                                 ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                                 TransactionGateway transactionGateway) {
        List<ValidadorPermissaoItemCardapioRule<ItemCardapioRuleContext>> permissaoRules = new ArrayList<>();
        permissaoRules.add(new ApenasDonoDoRestaurantePodeAlterarItemRule());
        permissaoRules.add(new ItemDeveTerRestauranteVinculadoRule());

        List<ValidadorItemCardapioRule<ItemCardapioRuleContext>> rules = new ArrayList<>();
        rules.add(new CamposObrigatoriosItemRule());
        rules.add(new NomeItemDeveSerUnicoNoRestauranteRule());
        rules.add(new PrecoItemDeveSerMaiorQueZeroRule());

        return new AlterarItemCardapioUseCaseImpl(
                itemRepository,
                cardapioRepository,
                restauranteGateway,
                obterUsuarioLogadoGateway,
                transactionGateway,
                permissaoRules,
                rules
        );
    }

    @Bean
    public DeletarItemCardapioUseCase deletarItemCardapioUseCase(ItemCardapioRepository itemRepository,
                                                               CardapioRepository cardapioRepository,
                                                               RestauranteGateway restauranteGateway,
                                                               ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                               TransactionGateway transactionGateway) {
        List<ValidadorPermissaoCardapioRule<DeletarItemCardapioRuleContextDto>> permissaoRules = List.of(
                new ApenasUsuarioDonoPodeDeletarItemRule()
        );
        List<ValidadorPermissaoCardapioRule<DeletarItemCardapioRuleContextDto>> rules = List.of(
                new ApenasDonoDoRestaurantePodeDeletarItemRule()
        );
        return new DeletarItemCardapioUseCaseImpl(itemRepository, cardapioRepository, restauranteGateway, obterUsuarioLogadoGateway, transactionGateway, permissaoRules, rules);
    }

    @Bean
    public BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase(ItemCardapioRepository itemRepository,
                                                                         ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                                         TransactionGateway transactionGateway) {
        return new BuscarItemCardapioPorIdUseCaseImpl(
                itemRepository,
                obterUsuarioLogadoGateway,
                transactionGateway,
                List.of(UsuarioDeveEstarAutenticadoRule.paraBuscarItem())
        );
    }

    @Bean
    public CriarCardapioUseCase criarCardapioUseCase(CardapioRepository cardapioRepository,
                                                   RestauranteGateway restauranteGateway,
                                                   ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                   TransactionGateway transactionGateway) {
        List<ValidadorPermissaoCardapioRule<? super CriarCardapioRuleContextDto>> permissionRules = List.of(
                new ApenasDonoPodeGerenciarCardapioRule()
        );
        List<ValidadorCardapioRule<? super CriarCardapioRuleContextDto>> businessRules = List.of(
                new CardapioDeveTerNomeRule(),
                new NomeCardapioDeveSerUnicoNoRestauranteRule(),
                new CardapioDeveTerPeloMenosUmItemRule(),
                new ItensCardapioNaoPodemSerDuplicadosRule()
        );
        return new CriarCardapioUseCaseImpl(cardapioRepository, restauranteGateway, obterUsuarioLogadoGateway, transactionGateway, permissionRules, businessRules);
    }

    @Bean
    public AlterarCardapioUseCase alterarCardapioUseCase(CardapioRepository cardapioRepository,
                                                        RestauranteGateway restauranteGateway,
                                                        ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                        TransactionGateway transactionGateway) {
        List<ValidadorPermissaoCardapioRule<? super AlterarCardapioRuleContextDto>> permissaoRules = List.of(
                new ApenasDonoPodeGerenciarCardapioRule()
        );

        List<ValidadorCardapioRule<? super AlterarCardapioRuleContextDto>> rules = List.of(
                new ApenasCardapioDoProprioRestaurantePodeSerAlteradoRule(),
                new CardapioDeveTerNomeRule(),
                new NomeCardapioDeveSerUnicoNoRestauranteRule(),
                new CardapioDeveTerPeloMenosUmItemRule(),
                new ItensCardapioNaoPodemSerDuplicadosRule()
        );

        return new AlterarCardapioUseCaseImpl(
                cardapioRepository,
                restauranteGateway,
                obterUsuarioLogadoGateway,
                transactionGateway,
                permissaoRules,
                rules
        );
    }

    @Bean
    public DeletarCardapioUseCase deletarCardapioUseCase(CardapioRepository cardapioRepository,
                                                       RestauranteGateway restauranteGateway,
                                                       ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                       TransactionGateway transactionGateway) {
        List<ValidadorPermissaoCardapioRule<? super DeletarCardapioRuleContextDto>> permissionRules = List.of(
                new ApenasDonoPodeGerenciarCardapioRule()
        );
        List<ValidadorCardapioRule<? super DeletarCardapioRuleContextDto>> businessRules = List.of(
                new ApenasCardapioDoProprioRestaurantePodeSerDeletadoRule()
        );
        return new DeletarCardapioUseCaseImpl(cardapioRepository, restauranteGateway, obterUsuarioLogadoGateway, transactionGateway, permissionRules, businessRules);
    }

    @Bean
    public BuscarCardapioPorRestauranteUseCase buscarCardapioPorRestauranteUseCase(CardapioRepository cardapioRepository,
                                                                                 ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                                                                 TransactionGateway transactionGateway) {
        return new BuscarCardapioPorRestauranteUseCaseImpl(
                cardapioRepository,
                obterUsuarioLogadoGateway,
                transactionGateway,
                List.of(UsuarioDeveEstarAutenticadoRule.paraBuscarCardapio())
        );
    }
}
