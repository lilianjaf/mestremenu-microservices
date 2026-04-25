package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.DomainException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.ObterUsuarioLogadoRestauranteGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.RestauranteRepository;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.TransactionGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.InativacaoRestauranteContext;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.InativarRestauranteRule;

import java.util.List;
import java.util.UUID;

public class InativarRestauranteUseCaseImpl implements InativarRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway;
    private final TransactionGateway transactionGateway;
    private final List<InativarRestauranteRule> permissaoRules;
    private final List<InativarRestauranteRule> rules;

    public InativarRestauranteUseCaseImpl(RestauranteRepository restauranteRepository,
                                         ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway,
                                         TransactionGateway transactionGateway,
                                         List<InativarRestauranteRule> permissaoRules,
                                         List<InativarRestauranteRule> rules) {
        this.restauranteRepository = restauranteRepository;
        this.obterUsuarioLogadoRestauranteGateway = obterUsuarioLogadoRestauranteGateway;
        this.transactionGateway = transactionGateway;
        this.permissaoRules = permissaoRules;
        this.rules = rules;
    }

    @Override
    public void executar(UUID id) {
        if (id == null) {
            throw new DomainException("ID do restaurante não pode ser nulo para inativação.");
        }

        Usuario usuarioLogado = obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()
                .orElseThrow(() -> new UsuarioLogadoNaoEncontradoException("Usuário logado não encontrado"));

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new DomainException("Restaurante não encontrado para inativação."));

        InativacaoRestauranteContext context = new InativacaoRestauranteContext(usuarioLogado, restaurante);

        transactionGateway.execute(() -> {
            permissaoRules.forEach(rule -> rule.validar(context));
            rules.forEach(rule -> rule.validar(context));

            restaurante.inativar();
            restauranteRepository.salvar(restaurante);
        });
    }
}
