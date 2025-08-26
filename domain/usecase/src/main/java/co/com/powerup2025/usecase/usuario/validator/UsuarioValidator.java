package co.com.powerup2025.usecase.usuario.validator;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.usecase.shared.BusinessException;
import co.com.powerup2025.usecase.spi.LoggerPort;
import reactor.core.publisher.Mono;

public class UsuarioValidator {
    private static final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private UsuarioValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static Mono<Usuario> validar(Usuario usuario) {
        return Mono.just(usuario)
                .filter(u -> u.getNombre() != null && !u.getNombre().isBlank())
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_001)))

                .filter(u -> u.getApellido() != null && !u.getApellido().isBlank())
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_002)))

                .filter(u -> u.getEmail() != null && !u.getEmail().isBlank())
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_003)))

                .filter(u -> emailPattern.matcher(u.getEmail()).matches())
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_009)))

                .filter(u -> u.getDocumentoIdentidad() != null && u.getDocumentoIdentidad() >= 1000000)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_005)))

                .filter(u -> u.getTelefono() != null && !u.getTelefono().isBlank())
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_006)))

                .filter(u -> u.getIdRol() != null)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_007)))

                .filter(u -> u.getSalarioBase() != null &&
                        u.getSalarioBase().compareTo(BigDecimal.ZERO) > 0 &&
                        u.getSalarioBase().compareTo(new BigDecimal("15000000")) <= 0)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.VAL_008)));
    }
}