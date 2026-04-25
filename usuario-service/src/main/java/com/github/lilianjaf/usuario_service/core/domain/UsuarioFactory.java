package com.github.lilianjaf.usuario_service.core.domain;

import com.github.lilianjaf.mestremenuclean.usuario.core.dto.DadosCriacaoUsuario;
import com.github.lilianjaf.mestremenuclean.usuario.core.exception.DomainException;

import java.util.Map;
import java.util.function.Function;

public class UsuarioFactory {

    private static final Map<TipoNativo, Function<DadosCriacaoUsuario, UsuarioBase>> REGISTRY = Map.of(
            TipoNativo.CLIENTE, dados -> new Cliente(
                    dados.nome(),
                    dados.email(),
                    dados.login(),
                    dados.senha(),
                    dados.tipoCustomizado(),
                    dados.endereco()
            ),
            TipoNativo.DONO, dados -> new Dono(
                    dados.nome(),
                    dados.email(),
                    dados.login(),
                    dados.senha(),
                    dados.tipoCustomizado(),
                    dados.endereco(),
                    null
            )
    );

    public static UsuarioBase criar(DadosCriacaoUsuario dados) {
        if (dados == null || dados.tipoCustomizado() == null) {
            throw new DomainException("Dados de criação incompletos ou tipo de usuário ausente.");
        }

        TipoNativo tipoBase = dados.tipoCustomizado().getTipoNativo();
        Function<DadosCriacaoUsuario, UsuarioBase> construtor = REGISTRY.get(tipoBase);

        if (construtor == null) {
            throw new DomainException("Tipo de usuário nativo não suportado.");
        }

        return construtor.apply(dados);
    }
}