package com.github.lilianjaf.pedido_service.infra.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic pedidoCriadoTopic() {
        return TopicBuilder.name("pedido.criado")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic pagamentoAprovadoTopic() {
        return TopicBuilder.name("pagamento.aprovado")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic pagamentoPendenteTopic() {
        return TopicBuilder.name("pagamento.pendente")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic pagamentoFalhouTopic() {
        return TopicBuilder.name("pagamento.falhou")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
