package co.com.powerup2025.model.logger.gateways;

public interface LoggerFactoryPort {
    LoggerRepository getLogger(Class<?> clazz);
}