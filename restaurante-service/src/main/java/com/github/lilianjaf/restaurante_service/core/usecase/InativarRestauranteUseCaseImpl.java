package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.exception.DomainException;
import com.github.lilianjaf.restaurante_service.core.exception.RegistroNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.restaurante_service.core.rules.InativacaoRestauranteContext;
import com.github.lilianjaf.restaurante_service.core.rules.InativarRestauranteRule;

import java.util.List;
import java.util.UUID;

public class InativarRestauranteUseCaseImpl implements InativarRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway;
    private final TransactionGateway transactionGateway;
    private final List<InativarRestauranteRule> permissaoRules;
    private final List<InativarRestauranteRule> rules;

    public InativarRestauranteUseCaseImpl(RestauranteRepository restauranteRepository,
                                         ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway,
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
                .orElseThrow(() -> new RegistroNaoEncontradoException("Restaurante não encontrado para inativação."));

        InativacaoRestauranteContext context = new InativacaoRestauranteContext(usuarioLogado, restaurante);

        transactionGateway.execute(() -> {
            permissaoRules.forEach(rule -> rule.validar(context));
            rules.forEach(rule -> rule.validar(context));

            restaurante.inativar();
            restauranteRepository.salvar(restaurante);
        });
    }
}
