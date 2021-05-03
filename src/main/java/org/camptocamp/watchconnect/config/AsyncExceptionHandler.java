package org.camptocamp.watchconnect.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncExceptionHandler.class);
    @Override
    public void handleUncaughtException(
            final Throwable throwable,
            final Method method,
            final Object... obj
    ) {
        // FIXME
        LOGGER.error("Exception message - " + throwable.getMessage());
        LOGGER.error("Method name - " + method.getName());
        for (Object param : obj) {
            LOGGER.error("Parameter value - " + param);
        }
    }
}
