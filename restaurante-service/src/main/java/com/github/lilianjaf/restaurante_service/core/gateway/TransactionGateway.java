package com.github.lilianjaf.restaurante_service.core.gateway;

import java.util.function.Supplier;

public interface TransactionGateway {
    <T> T execute(Supplier<T> operation);

    void execute(Runnable operation);
}