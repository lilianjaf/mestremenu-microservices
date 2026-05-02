package com.github.lilianjaf.usuario_service.infra.gateway;

import com.github.lilianjaf.usuario_service.core.gateway.TransactionGateway;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionGatewayUsuarioImpl implements TransactionGateway {

    @Override
    @Transactional
    public <T> T execute(Supplier<T> operation) {
        return operation.get();
    }

    @Override
    @Transactional
    public void execute(Runnable operation) {
        operation.run();
    }
}