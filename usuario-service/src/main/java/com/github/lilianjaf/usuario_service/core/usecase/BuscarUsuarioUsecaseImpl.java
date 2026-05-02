package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.dto.UsuarioOutput;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioNaoEncontradoException;
import com.github.lilianjaf.usuario_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.core.rules.ConsultaUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorConsultaUsuarioRule;

import java.util.List;
import java.util.UUID;

public class BuscarUsuarioUsecaseImpl implements BuscarUsuarioUsecase {
    private final UsuarioRepository repository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final List<ValidadorConsultaUsuarioRule> permissaoRules;
    private final List<ValidadorConsultaUsuarioRule> rules;

    public BuscarUsuarioUsecaseImpl(UsuarioRepository repository,
            ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
            List<ValidadorConsultaUsuarioRule> permissaoRules,
            List<ValidadorConsultaUsuarioRule> rules) {
        this.repository = repository;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public UsuarioOutput buscarPorId(UUID id) {
        UsuarioBase usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado().orElse(null);
        UsuarioBase usuarioBuscado = repository.findById(id).orElse(null);

        ConsultaUsuarioContext context = new ConsultaUsuarioContext(usuarioLogado, usuarioBuscado);

        permissaoRules.forEach(rule -> rule.validar(context));
        rules.forEach(rule -> rule.validar(context));

        return new UsuarioOutput(
                usuarioBuscado.getId(),
                usuarioBuscado.getNome(),
                usuarioBuscado.getEmail(),
                usuarioBuscado.getLogin(),
                usuarioBuscado.getTipoCustomizado().getNome(),
                usuarioBuscado.getTipoCustomizado().getTipoNativo().name(),
                usuarioBuscado.getAtivo());
    }

    @Override
    public UsuarioBase buscarEntidade(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));
    }

    @Override
    public UsuarioOutput buscarPorUsername(String username) {
        return repository.findUserByLogin(username)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));
    }
}