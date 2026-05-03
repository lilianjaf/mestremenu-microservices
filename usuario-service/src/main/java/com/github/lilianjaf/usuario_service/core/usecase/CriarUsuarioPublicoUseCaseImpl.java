package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.*;
import com.github.lilianjaf.usuario_service.core.dto.DadosCriacaoUsuario;
import com.github.lilianjaf.usuario_service.core.exception.RegistroNaoEncontradoException;
import com.github.lilianjaf.usuario_service.core.gateway.CodificadorDeSenha;
import com.github.lilianjaf.usuario_service.core.gateway.TipoUsuarioRepository;
import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.core.rules.CriacaoUsuarioPublicoContext;
import com.github.lilianjaf.usuario_service.core.rules.ValidadorCriacaoUsuarioPublicoRule;

import java.util.List;
import java.util.UUID;

public class CriarUsuarioPublicoUseCaseImpl implements CriarUsuarioPublicoUseCase {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final TransactionGateway transactionGateway;
    private final CodificadorDeSenha codificadorDeSenha;
    private final List<ValidadorCriacaoUsuarioPublicoRule> rules;

    public CriarUsuarioPublicoUseCaseImpl(
            UsuarioRepository usuarioRepository,
            TipoUsuarioRepository tipoUsuarioRepository,
            List<ValidadorCriacaoUsuarioPublicoRule> rules,
            TransactionGateway transactionGateway,
            CodificadorDeSenha codificadorDeSenha) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.rules = rules;
        this.transactionGateway = transactionGateway;
        this.codificadorDeSenha = codificadorDeSenha;
    }

    @Override
    public UUID criar(
            String nome, String email, String login, String senhaPura,
            String logradouro, String numero, String complemento, String bairro, String cidade, String cep, String uf) {

        return transactionGateway.execute(() -> {
            TipoUsuario tipoCustomizado = tipoUsuarioRepository.findByNome("cliente")
                    .orElseThrow(() -> new RegistroNaoEncontradoException(
                            "Tipo de usuário 'cliente' não encontrado. Execute as migrations do banco de dados."));

            var context = new CriacaoUsuarioPublicoContext(
                    nome, email, login, senhaPura, tipoCustomizado.getNome(),
                    logradouro, numero, bairro, cidade, cep, uf,
                    () -> usuarioRepository.findByEmail(email).isPresent(),
                    () -> usuarioRepository.findByLogin(login).isPresent()
            );
            rules.forEach(rule -> rule.validar(context));

            Endereco endereco = new Endereco(logradouro, numero, complemento, bairro, cidade, cep, uf);
            String senhaCriptografada = codificadorDeSenha.codificar(senhaPura);
            var dadosCriacao = new DadosCriacaoUsuario(nome, email, login, senhaCriptografada, tipoCustomizado, endereco);
            UsuarioBase novoUsuario = UsuarioFactory.criar(dadosCriacao);

            UsuarioBase usuarioSalvo = usuarioRepository.salvar(novoUsuario);
            return usuarioSalvo.getId();
        });
    }
}
