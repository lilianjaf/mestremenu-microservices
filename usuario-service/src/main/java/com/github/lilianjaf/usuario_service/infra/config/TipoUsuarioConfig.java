package com.github.lilianjaf.usuario_service.infra.config;

import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.core.rules.*;
import com.github.lilianjaf.usuario_service.core.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TipoUsuarioConfig {

    @Bean
    public CriarTipoUsuarioUsecase criarTipoUsuarioUsecase(
            TipoUsuarioRepository tipoUsuarioRepository,
            ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
            TransactionGateway transactionGateway) {
        return new CriarTipoUsuarioUsecaseImpl(
                tipoUsuarioRepository,
                obterUsuarioLogadoGateway,
                transactionGateway,
                List.of(new UsuarioDeveEstarAutenticadoRule()),
                List.of(
                        new ApenasDonoPodeCriarTipoUsuarioRule(),
                        new TipoUsuarioNomeDeveSerUnicoRule()
                )
        );
    }

    @Bean
    public AtualizarTipoUsuarioUsecase atualizarTipoUsuarioUsecase(
            TipoUsuarioRepository tipoUsuarioRepository,
            TransactionGateway transactionGateway,
            ObterUsuarioLogadoGateway obterUsuarioLogadoGateway) {
        return new AtualizarTipoUsuarioUsecaseImpl(
                tipoUsuarioRepository,
                transactionGateway,
                List.of(new NomeTipoUsuarioDeveSerUnicoRule()),
                List.of(
                        new UsuarioDeveEstarAutenticadoRule(),
                        new ValidarPermissaoDonoRule()
                ),
                obterUsuarioLogadoGateway
        );
    }

    @Bean
    public DeletarTipoUsuarioUsecase deletarTipoUsuarioUsecase(
            TipoUsuarioRepository tipoUsuarioRepository,
            UsuarioRepository usuarioRepository,
            TransactionGateway transactionGateway,
            ObterUsuarioLogadoGateway obterUsuarioLogadoGateway) {
        return new DeletarTipoUsuarioUsecaseImpl(
                tipoUsuarioRepository,
                usuarioRepository,
                transactionGateway,
                obterUsuarioLogadoGateway,
                List.of(
                        new UsuarioDeveEstarAutenticadoRule(),
                        new ApenasDonoPodeDeletarTipoUsuarioRule()
                ),
                List.of(
                        new TipoUsuarioDeveExistirRule(),
                        new TipoUsuarioNaoDeveEstarEmUsoRule()
                )
        );
    }
}