package com.github.lilianjaf.restaurante_service.infra.gateway.mapper;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Endereco;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.infra.gateway.entity.EnderecoEmbeddable;
import com.github.lilianjaf.mestremenuclean.restaurante.infra.gateway.entity.RestauranteEntity;

public class RestauranteEntityMapper {

    public static Restaurante toDomain(RestauranteEntity entity) {
        if (entity == null) {
            return null;
        }

        Endereco endereco = new Endereco(
                entity.getEndereco().getLogradouro(),
                entity.getEndereco().getNumero(),
                entity.getEndereco().getComplemento(),
                entity.getEndereco().getBairro(),
                entity.getEndereco().getCidade(),
                entity.getEndereco().getCep(),
                entity.getEndereco().getUf()
        );

        return new Restaurante(
                entity.getId(),
                entity.getNome(),
                endereco,
                entity.getTipoCozinha(),
                entity.getHorarioFuncionamento(),
                entity.getIdDono(),
                entity.isAtivo()
        );
    }

    public static RestauranteEntity toEntity(Restaurante domain) {
        if (domain == null) {
            return null;
        }

        EnderecoEmbeddable enderecoEmbeddable = toEntityEndereco(domain.getEndereco());

        return new RestauranteEntity(
                domain.getId(),
                domain.getNome(),
                enderecoEmbeddable,
                domain.getTipoCozinha(),
                domain.getHorarioFuncionamento(),
                domain.getIdDono(),
                domain.isAtivo()
        );
    }

    public static void atualizarEntity(Restaurante restauranteDomain, RestauranteEntity entity) {
        entity.setNome(restauranteDomain.getNome());
        entity.setTipoCozinha(restauranteDomain.getTipoCozinha());
        entity.setHorarioFuncionamento(restauranteDomain.getHorarioFuncionamento());
        entity.setIdDono(restauranteDomain.getIdDono());
        entity.setEndereco(toEntityEndereco(restauranteDomain.getEndereco()));
        entity.setAtivo(restauranteDomain.isAtivo());
    }

    private static EnderecoEmbeddable toEntityEndereco(Endereco endereco) {
        return new EnderecoEmbeddable(
                endereco.logradouro(),
                endereco.numero(),
                endereco.complemento(),
                endereco.bairro(),
                endereco.cidade(),
                endereco.cep(),
                endereco.uf()
        );
    }
}
