package org.example.servicio;

import org.example.excepcion.ErrorAplicacion;
import org.example.modelo.ConcelhoPorto;

import java.net.URI;

/** Reglas sencillas para validar los datos antes de guardar. */
final class Validacion {
    private Validacion() { }
    static void obligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "El campo " + campo + " es obligatorio.");
    }
    static void urlImagen(String valor) {
        obligatorio(valor, "imagenUrl");
        try {
            URI uri = URI.create(valor.trim());
            String scheme = uri.getScheme();
            if (uri.getHost() == null || scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) throw new IllegalArgumentException();
        } catch (IllegalArgumentException ex) {
            throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "La imagen debe tener una URL HTTP o HTTPS válida.");
        }
    }
    static void concelho(String texto) {
        obligatorio(texto, "concelho");
        try { ConcelhoPorto.valueOf(texto.trim().toUpperCase().replace(' ', '_')); }
        catch (IllegalArgumentException ex) { throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "El concejo no pertenece al distrito de Porto."); }
    }
}
