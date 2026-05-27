package com.github.lilianjaf.usuario_service.infra.gateway.mapper;

import com.github.lilianjaf.usuario_service.core.domain.*;
import com.github.lilianjaf.usuario_service.core.dto.UsuarioOutput;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.EnderecoEmbeddable;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.TipoUsuarioEntity;
import com.github.lilianjaf.usuario_service.infra.gateway.entity.UsuarioEntity;

public class UsuarioEntityMapper {

    public static UsuarioEntity toEntity(UsuarioBase domain) {
        if (domain == null) return null;

        EnderecoEmbeddable enderecoEntity = new EnderecoEmbeddable(
                domain.getEndereco().logradouro(),
                domain.getEndereco().numero(),
                domain.getEndereco().complemento(),
                domain.getEndereco().bairro(),
                domain.getEndereco().cidade(),
                domain.getEndereco().cep(),
                domain.getEndereco().uf()
        );

        TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity(
                domain.getTipoCustomizado().getId(),
                domain.getTipoCustomizado().getNome(),
                domain.getTipoCustomizado().getTipoNativo()
        );

        return new UsuarioEntity(
                domain.getId(),
                domain.getNome(),
                domain.getEmail(),
                domain.getLogin(),
                domain.getSenha(),
                tipoEntity,
                enderecoEntity,
                domain.getDataUltimaAlteracao(),
                domain.getAtivo()
        );
    }

    public static void atualizarEntity(UsuarioBase domain, UsuarioEntity entity) {
        if (domain == null || entity == null) return;

        entity.setNome(domain.getNome());
        entity.setEmail(domain.getEmail());
        entity.setLogin(domain.getLogin());
        entity.setSenha(domain.getSenha());
        entity.setDataUltimaAlteracao(domain.getDataUltimaAlteracao());
        entity.setAtivo(domain.getAtivo());

        if (domain.getEndereco() != null) {
            entity.setEndereco(new EnderecoEmbeddable(
                    domain.getEndereco().logradouro(),
                    domain.getEndereco().numero(),
                    domain.getEndereco().complemento(),
                    domain.getEndereco().bairro(),
                    domain.getEndereco().cidade(),
                    domain.getEndereco().cep(),
                    domain.getEndereco().uf()
            ));
        }

        if (domain.getTipoCustomizado() != null) {
            entity.setTipoCustomizado(new TipoUsuarioEntity(
                    domain.getTipoCustomizado().getId(),
                    domain.getTipoCustomizado().getNome(),
                    domain.getTipoCustomizado().getTipoNativo()
            ));
        }
    }

    public static UsuarioBase toDomain(UsuarioEntity entity) {
        if (entity == null) return null;

        Endereco enderecoDomain = new Endereco(
                entity.getEndereco().getLogradouro(),
                entity.getEndereco().getNumero(),
                entity.getEndereco().getComplemento(),
                entity.getEndereco().getBairro(),
                entity.getEndereco().getCidade(),
                entity.getEndereco().getCep(),
                entity.getEndereco().getUf()
        );

        TipoUsuario tipoDomain = new TipoUsuario(
                entity.getTipoCustomizado().getId(),
                entity.getTipoCustomizado().getNome(),
                entity.getTipoCustomizado().getTipoNativo()
        );

        if (tipoDomain.getTipoNativo() == TipoNativo.DONO) {
            return new Dono(
                    entity.getId(),
                    entity.getNome(),
                    entity.getEmail(),
                    entity.getLogin(),
                    entity.getSenha(),
                    tipoDomain,
                    enderecoDomain,
                    entity.getDataUltimaAlteracao(),
                    entity.getAtivo()
            );
        }

        return new Cliente(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getSenha(),
                tipoDomain,
                enderecoDomain,
                entity.getDataUltimaAlteracao(),
                entity.getAtivo()
        );
    }

    public static UsuarioOutput toOutput(UsuarioEntity entity) {
        if (entity == null) return null;

        return new UsuarioOutput(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getTipoCustomizado().getNome(),
                entity.getTipoCustomizado().getTipoNativo().name(),
                entity.getAtivo()
        );
    }
}