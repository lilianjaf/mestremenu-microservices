package test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.github.lilianjaf.pedido_service")
@EntityScan(basePackages = "com.github.lilianjaf.pedido_service.infra.gateway.entity")
@EnableJpaRepositories(basePackages = "com.github.lilianjaf.pedido_service.infra.gateway")
public class TestPedidoServiceApplication {
}
