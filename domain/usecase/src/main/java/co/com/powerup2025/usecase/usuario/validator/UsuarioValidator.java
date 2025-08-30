package co.com.powerup2025.usecase.usuario.validator;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import co.com.powerup2025.model.exception.enums.ErrorCode;
import co.com.powerup2025.model.usuario.Usuario;
import co.com.powerup2025.model.exception.exceptions.BusinessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class UsuarioValidator {
    private static final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private UsuarioValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static Mono<Usuario> validar(Usuario usuario) {
        return Flux.just(
                Tuples.of(isBlank(usuario.getNombre()), ErrorCode.VAL_001),
                Tuples.of(isBlank(usuario.getApellido()), ErrorCode.VAL_002),
                Tuples.of(isBlank(usuario.getEmail()), ErrorCode.VAL_003),
                Tuples.of(!emailPattern.matcher(usuario.getEmail()).matches(),
                        ErrorCode.VAL_009),
                Tuples.of(usuario.getIdRol() == null, ErrorCode.VAL_007),
                Tuples.of(isSalarioInvalido(usuario.getSalarioBase()), ErrorCode.VAL_008))
                .filter(Tuple2::getT1)
                .map(Tuple2::getT2)
                .collectList()
                .flatMap(errores -> errores.isEmpty()
                        ? Mono.just(usuario)
                        : Mono.error(new BusinessException(errores)));
    }

    public static Mono<Boolean> validarEmail(String email) {
        return (isBlank(email) || !emailPattern.matcher(email).matches())
                    ? Mono.error(new BusinessException(ErrorCode.VAL_003))
                    : Mono.just(true);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static boolean isSalarioInvalido(BigDecimal salario) {
        return salario == null ||
                salario.compareTo(BigDecimal.ZERO) <= 0 ||
                salario.compareTo(new BigDecimal("15000000")) > 0;
    }

}