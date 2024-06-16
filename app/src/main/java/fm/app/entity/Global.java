package fm.app.entity;

public class Global {

    // Enum para el estado
    public enum Estado {
        A,
        I
    }

    // Tipos de respuesta
    public static final String TIPO_RESULTADO = "resultado";
    public static final String TIPO_DATOS = "datos";
    public static final String TIPO_AUTENTICACION = "autenticacion";
    public static final String TIPO_ERROR = "ERROR";
    public static final String TIPO_EXITO = "EXITO";
    public static final String TIPO_ADVERTENCIA = "ADVERTENCIA";

    // Códigos de respuesta
    public static final int RESPUESTA_OK = 1;
    public static final int RESPUESTA_ADVERTENCIA = 0;
    public static final int RESPUESTA_ERROR = -1;

    // Mensajes de operación comunes
    public static final String OPERACION_EXITOSA = "Operación finalizada correctamente";
    public static final String OPERACION_INCORRECTA = "No se ha podido culminar la operación";
    public static final String OPERACION_ERRONEA = "Ha ocurrido un error al realizar la operación";
    public static final String AUTENTICACION_EXITOSA = "Autenticación exitosa";
    public static final String AUTENTICACION_FALLIDA = "Fallo en la autenticación, por favor verifica tus credenciales";
    public static final String USUARIO_NO_ENCONTRADO = "Usuario no encontrado";
    public static final String USUARIO_YA_EXISTE = "El usuario ya existe";
    public static final String DATOS_INSUFICIENTES = "Datos insuficientes para completar la operación";
    public static final String ACCION_NO_PERMITIDA = "Acción no permitida";

}
