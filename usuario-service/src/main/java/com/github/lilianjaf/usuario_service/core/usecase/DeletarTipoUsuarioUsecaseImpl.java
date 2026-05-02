package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.core.rules.ExclusaoTipoUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorExclusaoTipoUsuarioRule;

import java.util.List;
import java.util.UUID;

public class DeletarTipoUsuarioUsecaseImpl implements DeletarTipoUsuarioUsecase {

    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final TransactionGateway transactionGateway;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final List<ValidadorExclusaoTipoUsuarioRule> permissaoRules;
    private final List<ValidadorExclusaoTipoUsuarioRule> rules;

    public DeletarTipoUsuarioUsecaseImpl(TipoUsuarioRepository tipoUsuarioRepository,
                                         UsuarioRepository usuarioRepository,
                                         TransactionGateway transactionGateway,
                                         ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                         List<ValidadorExclusaoTipoUsuarioRule> permissaoRules,
                                         List<ValidadorExclusaoTipoUsuarioRule> rules) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.transactionGateway = transactionGateway;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public void deletar(UUID id) {
        UsuarioBase usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        ExclusaoTipoUsuarioContext context = new ExclusaoTipoUsuarioContext(
                tipoUsuarioRepository.findById(id),
                () -> usuarioRepository.existeUsuarioComTipo(id),
                usuarioLogado
        );

        permissaoRules.forEach(rule -> rule.validar(context));
        rules.forEach(rule -> rule.validar(context));

        transactionGateway.execute(() -> {
            TipoUsuario tipo = context.tipoUsuarioASerDeletado().get();
            tipoUsuarioRepository.deletar(tipo);
        });
    }
}