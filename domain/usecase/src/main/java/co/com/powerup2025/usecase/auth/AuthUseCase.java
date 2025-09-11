package co.com.powerup2025.usecase.auth;

import co.com.powerup2025.model.auth.gateways.IAuthUseCase;
import co.com.powerup2025.model.auth.gateways.PasswordEncoderPort;
import co.com.powerup2025.model.auth.gateways.TokenGeneratorPort;
import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import co.com.powerup2025.model.exception.exceptions.InvalidCredentialsException;
import co.com.powerup2025.model.logger.gateways.LoggerFactoryPort;
import co.com.powerup2025.model.logger.gateways.LoggerRepository;
import co.com.powerup2025.model.role.gateways.RoleRepository;
import co.com.powerup2025.model.user.gateways.IUserUseCase;

import reactor.core.publisher.Mono;


public class AuthUseCase implements IAuthUseCase {

    private final IUserUseCase userUseCase;
    private final RoleRepository roleRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenGeneratorPort tokenGenerator;
    private final LoggerRepository logger;

    public AuthUseCase(IUserUseCase userUseCase, RoleRepository roleRepository,
                       PasswordEncoderPort passwordEncoder,
                       TokenGeneratorPort tokenGenerator,
                       LoggerFactoryPort logger) {
        this.userUseCase = userUseCase;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
        this.logger = logger.getLogger(AuthUseCase.class);
    }

    @Override
    public Mono<String> authenticate(String email, String rawPassword) {
        logger.info(String.format("Iniciando autenticación para email: %s", email));

        return userUseCase.findUserByEmail(email)
                .doOnSubscribe(sub -> logger.info(String.format("Buscando usuario en BD para: %s", email)))
                .doOnNext(user -> logger.info(String.format("Usuario encontrado: %s", user.getEmail())))
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn(String.format("Usuario no encontrado para email: %s", email));
                    return Mono.error(new BusinessException(ErrorCode.USR_001));
                }))
                .flatMap(user -> {
                    boolean valid = passwordEncoder.matches(rawPassword, user.getPassword());
                    if (!valid) {
                        logger.warn(String.format("Contraseña inválida para usuario: %s", user.getEmail()));
                        return Mono.error(new InvalidCredentialsException("Credenciales inválidas"));
                    }
                    logger.info(String.format("UserRole recibido: %s", user.getIdRol()));
                    return roleRepository.findByIdRole(user.getIdRol())
                            .map(role -> {
                                logger.info(String.format("RoleEntity recibido: %s", role));
                                return role.getNombre();
                            })
                            .flatMap(role -> {
                                        logger.info(String.format("Rol encontrado para usuario %s: %s", user.getEmail(), role));
                                        String token = tokenGenerator.generateToken(user.getEmail(), role);
                                        logger.info(String.format("Token generado exitosamente para usuario: %s", user.getEmail()));
                                        return Mono.just(token);
                                    });

                })
                .doOnSuccess(token -> logger.info(String.format("Autenticación completada para: %s", email)))
                .doOnError(error -> logger.error(String.format("Error en autenticación para %s: %s", email, error.getMessage())));
    }



}

