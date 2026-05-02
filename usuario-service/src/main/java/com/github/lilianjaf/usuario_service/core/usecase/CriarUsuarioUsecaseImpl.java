package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.*;
import com.github.lilianjaf.usuario_service.core.dto.DadosCriacaoUsuario;
import com.github.lilianjaf.usuario_service.core.exception.RegraDeNegocioException;
import com.github.lilianjaf.usuario_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.usuario_service.core.gateway.*;
import com.github.lilianjaf.usuario_service.core.rules.CriacaoUsuarioContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorCriacaoUsuarioRule;

import java.util.List;
import java.util.UUID;

public class CriarUsuarioUsecaseImpl implements CriarUsuarioUsecase {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final CodificadorDeSenha codificadorDeSenha;
    private final TransactionGateway transactionGateway;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    private final List<ValidadorCriacaoUsuarioRule> permissaoRules;
    private final List<ValidadorCriacaoUsuarioRule> rules;

    public CriarUsuarioUsecaseImpl(
            UsuarioRepository usuarioRepository,
            TipoUsuarioRepository tipoUsuarioRepository,
            CodificadorDeSenha codificadorDeSenha,
            TransactionGateway transactionGateway,
            ObterUsuarioLogadoGateway obterUsuarioLogadoGateway,
            List<ValidadorCriacaoUsuarioRule> permissaoRules,
            List<ValidadorCriacaoUsuarioRule> rules) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.codificadorDeSenha = codificadorDeSenha;
        this.transactionGateway = transactionGateway;
        this.obterUsuarioLogadoGateway = obterUsuarioLogadoGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public UUID criar(
            String nome, String email, String login, String senhaPura,
            String nomeTipoDesejado, TipoNativo tipoNativoDesejado,
            String logradouro, String numero, String complemento, String bairro, String cidade, String cep, String uf) {

        UsuarioBase usuarioLogado = obterUsuarioLogadoGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        CriacaoUsuarioContext context = new CriacaoUsuarioContext(
                nome, email, login, senhaPura, nomeTipoDesejado, tipoNativoDesejado,
                () -> usuarioRepository.findByEmail(email).isPresent(),
                () -> usuarioRepository.findByLogin(login).isPresent(),
                usuarioLogado
        );

        permissaoRules.forEach(rule -> rule.validar(context));
        rules.forEach(rule -> rule.validar(context));

        return transactionGateway.execute(() -> {
            Endereco endereco = new Endereco(logradouro, numero, complemento, bairro, cidade, cep, uf);

            TipoUsuario tipoCustomizado = tipoUsuarioRepository.findByNome(nomeTipoDesejado)
                    .orElseGet(() -> {
                        if (tipoNativoDesejado == null) {
                            throw new RegraDeNegocioException("Tipo de usuário '" + nomeTipoDesejado + "' não existe. Informe o 'tipoNativo' (DONO ou CLIENTE) para criá-lo.");
                        }

                        TipoUsuario novoTipo = new TipoUsuario(nomeTipoDesejado, tipoNativoDesejado);
                        return tipoUsuarioRepository.salvar(novoTipo);
                    });

            String senhaCriptografada = codificadorDeSenha.codificar(senhaPura);
            var dadosCriacao = new DadosCriacaoUsuario(nome, email, login, senhaCriptografada, tipoCustomizado, endereco);
            UsuarioBase novoUsuario = UsuarioFactory.criar(dadosCriacao);

            UsuarioBase usuarioSalvo = usuarioRepository.salvar(novoUsuario);
            return usuarioSalvo.getId();
        });
    }
}