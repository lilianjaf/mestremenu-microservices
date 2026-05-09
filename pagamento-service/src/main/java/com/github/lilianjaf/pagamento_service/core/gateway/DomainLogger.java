package com.github.lilianjaf.pagamento_service.core.gateway;

public interface DomainLogger {
    void info(String message, Object... params);
    void warn(String message, Object... params);
    void error(String message, Object... params);
    void error(String message, Throwable cause);
}
