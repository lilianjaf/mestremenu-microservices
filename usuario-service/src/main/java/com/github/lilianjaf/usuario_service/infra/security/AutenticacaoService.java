package com.github.lilianjaf.usuario_service.infra.security;

import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usuarioRepository.findByLogin(username)
                .map(usuarioBase -> {
                    String roleName = usuarioBase.getTipoCustomizado().getTipoNativo().name();

                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + roleName)
                    );

                    return new User(
                            usuarioBase.getLogin(),
                            usuarioBase.getSenha(),
                            authorities
                    );
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}