package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.core.rules.InativacaoUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorInativacaoUsuarioRule;

import java.util.List;
import java.util.UUID;

public class InativarUsuarioUsecaseImpl implements InativarUsuarioUsecase {
    private final BuscarUsuarioUsecase buscarUsuarioUsecase;
    private final UsuarioRepository repository;
    private final TransactionGateway transactionGateway;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final List<ValidadorInativacaoUsuarioRule> permissaoRules;
    private final List<ValidadorInativacaoUsuarioRule> rules;

    public InativarUsuarioUsecaseImpl(BuscarUsuarioUsecase buscarUsuarioUsecase,
                                  UsuarioRepository usuarioRepository,
                                  TransactionGateway transactionGateway,
                                  ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
                                  List<ValidadorInativacaoUsuarioRule> permissaoRules,
                                  List<ValidadorInativacaoUsuarioRule> rules) {
        this.buscarUsuarioUsecase = buscarUsuarioUsecase;
        this.repository = usuarioRepository;
        this.transactionGateway = transactionGateway;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public void inativar(UUID id) {
        UsuarioBase usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        UsuarioBase usuarioAlvo = buscarUsuarioUsecase.buscarEntidade(id);

        InativacaoUsuarioContext context = new InativacaoUsuarioContext(usuarioLogado, usuarioAlvo);

        permissaoRules.forEach(rule -> rule.validar(context));
        rules.forEach(rule -> rule.validar(context));

        transactionGateway.execute(() -> {
            usuarioAlvo.desativar();
            repository.salvar(usuarioAlvo);
        });
    }
}