package co.com.powerup2025.model.exception;

import co.com.powerup2025.model.exception.enums.ErrorModule;
import co.com.powerup2025.model.exception.enums.Severity;

public interface iErrorCode {
    String code();
    String message();
    Severity severity();
    ErrorModule module();

}
