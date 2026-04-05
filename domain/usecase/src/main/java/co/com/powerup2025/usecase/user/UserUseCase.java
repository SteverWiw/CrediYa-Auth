package co.com.powerup2025.usecase.user;

import co.com.powerup2025.model.auth.gateways.PasswordEncoderPort;
import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.logger.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.user.User;
import co.com.powerup2025.model.user.gateways.UserRepository;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.logger.gateways.LoggerRepository;
import co.com.powerup2025.usecase.user.validator.UserValidator;
import reactor.core.publisher.Mono;


public class UserUseCase implements co.com.powerup2025.model.user.gateways.IUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final LoggerRepository logger;

    public UserUseCase(UserRepository userRepository, PasswordEncoderPort passwordEncoder, LoggerFactoryPort logger) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.logger = logger.getLogger(UserUseCase.class);
    }

    @Override
    public Mono<User> createUser(User user) {
        return UserValidator.validar(user)
                .doOnSubscribe(s -> logger.info("Iniciando validación de usuario"))
                .flatMap(v -> userRepository.existsByEmail(user.getEmail()))
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new BusinessException(ErrorCode.USR_002));
                    }
                    String encodedPassword = passwordEncoder.encode(user.getPassword());
                    user.setPassword(encodedPassword);
                    return userRepository.save(user);
                })
                .doOnSuccess(u -> logger.info("Usuario creado exitosamente"));
    }



    @Override
    public Mono<User> findUserByEmail(String email) {
        return UserValidator.validarEmail(email)
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? userRepository.getUserByEmail(email)
                        .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USR_003)))
                        : Mono.error(new BusinessException(ErrorCode.USR_001))
                );
    }

}
