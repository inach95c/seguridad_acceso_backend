/*// com/seguridad/notificaciones/TipoEvento.java
package com.seguridad.notificaciones;

public enum TipoEvento {
    // Operativos
    REGISTRO_INGRESO,
    REGISTRO_SALIDA,
    REGISTRO_ELIMINADO,
    ERROR_FOTO,
    SOSPECHOSO_DETECTADO,

    // Administrativos
    USUARIO_CREADO,
    USUARIO_DESACTIVADO,
    FALLO_SISTEMA,
    ALERTA_SEGURIDAD,

    // Críticos / Master
    ERROR_GRAVE,
    INTENTO_ACCESO_NO_AUTORIZADO
}

*/


package com.seguridad.notificaciones;

public enum TipoEvento {
    // Operativos
    REGISTRO_INGRESO,
    REGISTRO_SALIDA,
    REGISTRO_ELIMINADO,
    ERROR_FOTO,
    SOSPECHOSO_DETECTADO,

    // Administrativos
    USUARIO_CREADO,
    USUARIO_DESACTIVADO,
    USUARIO_ELIMINADO,   // 👈 AGREGADO
    FALLO_SISTEMA,
    ALERTA_SEGURIDAD,

    // Críticos / Master
    ERROR_GRAVE,
    INTENTO_ACCESO_NO_AUTORIZADO
}
