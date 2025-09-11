package co.com.powerup2025.model.exception.enums;


import co.com.powerup2025.model.exception.gateways.iErrorCode;

public enum ErrorCode implements iErrorCode {

    USR_001("El usuario no fue encontrado", Severity.HIGH, ErrorModule.USER),
    USR_002("El email ya esta en uso", Severity.HIGH, ErrorModule.USER),
    USR_003("El usuario no fue encontrado", Severity.HIGH, ErrorModule.USER),
    USR_004("El usuario no tiene roles asociados", Severity.HIGH, ErrorModule.USER),

    VAL_001 ("El nombre es obligatorio", Severity.MEDIUM, ErrorModule.VALI),
    VAL_002 ("El apellido es obligatorio", Severity.MEDIUM, ErrorModule.VALI),
    VAL_003 ("El email es obligatorio", Severity.MEDIUM, ErrorModule.VALI),
    VAL_005 ("Documento inválido", Severity.MEDIUM, ErrorModule.VALI),
    VAL_006 ("El teléfono es obligatorio", Severity.MEDIUM, ErrorModule.VALI),
    VAL_007 ("El rol es obligatorio", Severity.MEDIUM, ErrorModule.VALI),
    VAL_008( "El salario debe estar entre 0 y 15.000.000", Severity.MEDIUM, ErrorModule.VALI),
    VAL_009 ("El email debe tener formato válido", Severity.MEDIUM, ErrorModule.VALI),
    VAL_010 ("La contraseña es obligatoria",Severity.MEDIUM, ErrorModule.VALI),

    AUT_001("Credenciales inválidas",Severity.MEDIUM, ErrorModule.AUTH),
    AUT_002("Acceso denegado",Severity.MEDIUM, ErrorModule.AUTH),

    SYS_001("Error inesperado en el sistema", Severity.CRITICAL, ErrorModule.USER);


    private final String message;
    private final Severity severity;
    private final ErrorModule module;

    ErrorCode(String message, Severity severity, ErrorModule module) {
        this.message = message;
        this.severity = severity;
        this.module = module;
    }

    @Override public String code() {
        return this.name();
    }

    @Override public String message() {
        return message;
    }

    @Override public Severity severity() {
        return severity;
    }

    @Override public ErrorModule module() {
        return module;
    }



}