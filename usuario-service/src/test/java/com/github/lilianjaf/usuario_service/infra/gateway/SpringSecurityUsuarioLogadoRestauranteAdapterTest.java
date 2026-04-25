package com.github.lilianjaf.usuario_service.infra.gateway;

import com.github.lilianjaf.usuario_service.core.domain.*;
import com.github.lilianjaf.usuario_service.core.gateway.UsuarioRepository;
import com.github.lilianjaf.usuario_service.infra.gateway.SpringSecurityUsuarioLogadoAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpringSecurityUsuarioLogadoRestauranteAdapterTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SpringSecurityUsuarioLogadoAdapter adapter;

    @Test
    @DisplayName("Deve obter usuário logado a partir do SecurityContext")
    void deveObterUsuarioLogado() {
        
        String username = "testuser";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        TipoUsuario tipo = new TipoUsuario(UUID.randomUUID(), "CLIENTE", TipoNativo.CLIENTE);
        Endereco endereco = new Endereco("Rua A", "123", null, "Bairro X", "Cidade Y", "12345678", "UF");
        UsuarioBase usuario = new Cliente(UUID.randomUUID(), "Teste", "teste@email.com", username, "senha", tipo, endereco, null, true);
        when(usuarioRepository.findByLogin(username)).thenReturn(Optional.of(usuario));

        
        Optional<UsuarioBase> resultado = adapter.obterUsuarioLogado();

        
        assertTrue(resultado.isPresent());
        assertEquals(username, resultado.get().getLogin());
        
        
        SecurityContextHolder.clearContext();
    }
}
