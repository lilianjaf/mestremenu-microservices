package com.github.lilianjaf.usuario_service.infra.config;

import com.github.lilianjaf.mestremenuclean.usuario.core.api.UsuarioModuleFacade;
import com.github.lilianjaf.mestremenuclean.usuario.core.gateway.*;
import com.github.lilianjaf.mestremenuclean.usuario.core.rules.*;
import com.github.lilianjaf.mestremenuclean.usuario.core.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UsuarioConfig {

    @Bean
    public CriarUsuarioPublicoUseCase criarUsuarioPublicoUseCase(
            UsuarioRepository usuarioRepository,
            TipoUsuarioRepository tipoUsuarioRepository,
            TransactionGateway transactionGateway,
            CodificadorDeSenha codificadorDeSenha) {

        List<ValidadorCriacaoUsuarioPublicoRule> rules = List.of(
                new UsuarioPublicoDeveSerTipoClienteRule(),
                new EmailUsuarioPublicoDeveSerUnicoRule(),
                new LoginUsuarioPublicoDeveSerUnicoRule(),
                new SenhaUsuarioPublicoNaoPodeSerVaziaRule()
        );

        return new CriarUsuarioPublicoUseCaseImpl(usuarioRepository,
                tipoUsuarioRepository,
                rules,
                transactionGateway,
                codificadorDeSenha);
    }

    @Bean
    public CriarUsuarioUsecase criarUsuarioUsecase(
            UsuarioRepository usuarioRepository,
            TipoUsuarioRepository tipoUsuarioRepository,
            CodificadorDeSenha codificadorDeSenha,
            TransactionGateway transactionGateway,
            ObterUsuarioLogadoGateway obterUsuarioLogadoGateway) {

        List<ValidadorCriacaoUsuarioRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoRule(),
                new ApenasDonoPodeCriarNovosUsuariosRule()
        );

        List<ValidadorCriacaoUsuarioRule> rules = List.of(
                new SenhaDeveSerInformadaRule(),
                new TipoUsuarioNativoDeveExistirRule(),
                new EmailELoginDevemSerUnicosRule()
        );

        return new CriarUsuarioUsecaseImpl(
                usuarioRepository,
                tipoUsuarioRepository,
                codificadorDeSenha,
                transactionGateway,
                obterUsuarioLogadoGateway,
                permissaoRules,
                rules
        );
    }

    @Bean
    public BuscarUsuarioUsecase buscarUsuarioUsecase(
            UsuarioRepository usuarioRepository,
            ObterUsuarioLogadoGateway obterUsuarioLogadoGateway) {

        List<ValidadorConsultaUsuarioRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoRule()
        );

        List<ValidadorConsultaUsuarioRule> rules = List.of(
                new ConsultaUsuarioDeveExistirRule(),
                new ApenasDonoOuProprioUsuarioPodeConsultarRule()
        );

        return new BuscarUsuarioUsecaseImpl(
                usuarioRepository,
                obterUsuarioLogadoGateway,
                permissaoRules,
                rules
        );
    }

    @Bean
    public AtualizarUsuarioUsecase atualizarUsuarioUsecase(
            UsuarioRepository usuarioRepository,
            TransactionGateway transactionGateway,
            ObterUsuarioLogadoGateway obterUsuarioLogadoGateway) {

        List<ValidadorPermissaoAtualizacaoUsuarioRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoRule()
        );

        List<ValidadorAtualizacaoUsuarioRule> rules = List.of(
                new UsuarioDeveExistirRule(),
                new ApenasDonoOuProprioUsuarioPodeEditarRule(),
                new EmailUsuarioDeveSerUnicoRule()
        );

        return new AtualizarUsuarioUsecaseImpl(
                usuarioRepository,
                transactionGateway,
                obterUsuarioLogadoGateway,
                permissaoRules,
                rules
        );
    }

    @Bean
    public InativarUsuarioUsecase inativarUsuarioUsecase(
            BuscarUsuarioUsecase buscarUsuarioUsecase,
            UsuarioRepository usuarioRepository,
            TransactionGateway transactionGateway,
            ObterUsuarioLogadoGateway obterUsuarioLogadoGateway) {

        List<ValidadorInativacaoUsuarioRule> permissaoRules = List.of(
                new UsuarioDeveEstarAutenticadoRule(),
                new ApenasDonoOuProprioUsuarioPodeInativarRule()
        );

        List<ValidadorInativacaoUsuarioRule> rules = List.of(
                new UsuarioSemRestauranteVinculadoRule()
        );

        return new InativarUsuarioUsecaseImpl(
                buscarUsuarioUsecase,
                usuarioRepository,
                transactionGateway,
                obterUsuarioLogadoGateway,
                permissaoRules,
                rules
        );
    }

    @Bean
    public UsuarioModuleFacade usuarioModuleFacade(BuscarUsuarioUsecase buscarUsuarioUsecase) {
        return new UsuarioModuleFacadeImpl(buscarUsuarioUsecase);
    }
}