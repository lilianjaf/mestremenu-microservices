package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.rules.CriacaoTipoUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorCriacaoTipoUsuarioRule;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorPermissaoRule;

import java.util.List;

public class CriarTipoUsuarioUsecaseImpl implements CriarTipoUsuarioUsecase {

    private final TipoUsuarioRepository repository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final TransactionGateway transactionGateway;
    private final List<ValidadorPermissaoRule> permissaoRules;
    private final List<ValidadorCriacaoTipoUsuarioRule> rules;

    public CriarTipoUsuarioUsecaseImpl(TipoUsuarioRepository repository,
                                       ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                       TransactionGateway transactionGateway,
                                       List<ValidadorPermissaoRule> permissaoRules,
                                       List<ValidadorCriacaoTipoUsuarioRule> rules) {
        this.repository = repository;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public TipoUsuario criar(String loginUsuarioLogado, String nome, TipoNativo tipoNativo) {
        UsuarioBase usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado().orElse(null);

        boolean existeComMesmoNome = repository.findByNome(nome).isPresent();
        CriacaoTipoUsuarioContext context = new CriacaoTipoUsuarioContext(nome, existeComMesmoNome, usuarioLogado);

        permissaoRules.forEach(rule -> rule.validar(usuarioLogado));
        rules.forEach(rule -> rule.validar(context));

        return transactionGateway.execute(() -> {
            TipoUsuario novoTipo = new TipoUsuario(nome, tipoNativo);
            return repository.salvar(novoTipo);
        });
    }
}