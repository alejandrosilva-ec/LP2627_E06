package org.example.excepcion;

/** Error controlado de validación, duplicidad, ausencia o base de datos. */
public class ErrorAplicacion extends RuntimeException {
    public enum Tipo { VALIDACION, DUPLICADO, NO_ENCONTRADO, BASE_DATOS, INTEGRIDAD }
    private final Tipo tipo;

    public ErrorAplicacion(Tipo tipo, String mensaje) { super(mensaje); this.tipo = tipo; }
    public ErrorAplicacion(Tipo tipo, String mensaje, Throwable causa) { super(mensaje, causa); this.tipo = tipo; }
    public Tipo getTipo() { return tipo; }
}
