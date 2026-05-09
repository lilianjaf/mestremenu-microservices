package com.github.lilianjaf.pagamento_service.infra.gateway;

import com.github.lilianjaf.pagamento_service.core.gateway.DomainLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jDomainLoggerAdapter implements DomainLogger {

    private final Logger logger;

    public Slf4jDomainLoggerAdapter(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void info(String message, Object... params) {
        logger.info(message, params);
    }

    @Override
    public void warn(String message, Object... params) {
        logger.warn(message, params);
    }

    @Override
    public void error(String message, Object... params) {
        logger.error(message, params);
    }

    @Override
    public void error(String message, Throwable cause) {
        logger.error(message, cause);
    }
}
