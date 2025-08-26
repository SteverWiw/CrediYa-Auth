package co.com.powerup2025.usecase.shared;

import co.com.powerup2025.model.exception.iErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final iErrorCode errorCode;

    public BusinessException(iErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

}

