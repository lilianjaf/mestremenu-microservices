package com.github.lilianjaf.usuario_service.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Dono extends UsuarioBase {

    private List<Restaurante> restaurantes;

    public Dono(String nome, String email, String login, String senha, TipoUsuario tipoCustomizado, Endereco endereco, List<Restaurante> restaurantes) {
        super(nome, email, login, senha, tipoCustomizado, endereco);
        this.restaurantes = restaurantes != null ? restaurantes : new ArrayList<>();
    }

    public Dono(UUID id, String nome, String email, String login, String senha, TipoUsuario tipoCustomizado, Endereco endereco, LocalDateTime dataUltimaAlteracao, Boolean ativo, List<Restaurante> restaurantes) {
        super(id, nome, email, login, senha, tipoCustomizado, endereco, dataUltimaAlteracao, ativo);
        this.restaurantes = restaurantes != null ? restaurantes : new ArrayList<>();
    }

    public List<Restaurante> getRestaurantes() {
        return restaurantes;
    }

    public boolean isProprietario(Long idRestaurante) {
        if (this.restaurantes == null || this.restaurantes.isEmpty()) {
            return false;
        }
        return restaurantes.stream().anyMatch(r -> r.getId().equals(idRestaurante));
    }
}