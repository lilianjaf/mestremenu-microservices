package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.TipoUsuarioNaoEncontradoException;
import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.rules.AtualizacaoTipoUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorAtualizacaoTipoUsuarioRule;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorPermissaoRule;

import java.util.List;
import java.util.UUID;

public class AtualizarTipoUsuarioUsecaseImpl implements AtualizarTipoUsuarioUsecase {

    private final TipoUsuarioRepository repository;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorAtualizacaoTipoUsuarioRule> rules;
    private final List<ValidadorPermissaoRule> permissaoRules;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    public AtualizarTipoUsuarioUsecaseImpl(TipoUsuarioRepository repository,
                                           TransactionGateway transactionGateway,
                                           List<ValidadorAtualizacaoTipoUsuarioRule> rules,
                                           List<ValidadorPermissaoRule> permissaoRules,
                                           ObterUsuarioLogadoGateway obterUsuarioLogadoGateway) {
        this.repository = repository;
        this.transactionGateway = transactionGateway;
        this.rules = rules;
        this.permissaoRules = permissaoRules;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
    }

    @Override
    public void atualizar(UUID id, String novoNome) {
        UsuarioBase usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado().orElse(null);

        permissaoRules.forEach(rule -> rule.validar(usuarioLogado));

        transactionGateway.execute(() -> {
            TipoUsuario tipo = repository.findById(id)
                    .orElseThrow(() -> new TipoUsuarioNaoEncontradoException("Tipo de usuário não encontrado."));

            TipoUsuario tipoComMesmoNome = repository.findByNome(novoNome).orElse(null);

            AtualizacaoTipoUsuarioContext context = new AtualizacaoTipoUsuarioContext(tipo, tipoComMesmoNome, usuarioLogado);

            rules.forEach(rule -> rule.validar(context));

            tipo.atualizarNome(novoNome);

            repository.salvar(tipo);
        });
    }
}