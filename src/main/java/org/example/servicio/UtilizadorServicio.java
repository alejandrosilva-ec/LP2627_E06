package org.example.servicio;

import org.example.dao.UtilizadorDAO;
import org.example.excepcion.ErrorAplicacion;
import org.example.modelo.Perfil;
import org.example.modelo.Utilizador;

import java.util.List;

/** Reglas de negocio para cuentas de usuario. */
public class UtilizadorServicio {
    private final UtilizadorDAO dao;
    public UtilizadorServicio() { this(new UtilizadorDAO()); }
    public UtilizadorServicio(UtilizadorDAO dao) { this.dao = dao; }

    public Utilizador registrar(String nombre, String email, String password, String docId) {
        Validacion.obligatorio(nombre, "nombre"); Validacion.obligatorio(email, "email");
        Validacion.obligatorio(password, "password"); Validacion.obligatorio(docId, "docId");
        if (!email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "El email no tiene un formato válido.");
        email = email.trim().toLowerCase(); docId = docId.trim();
        comprobarUnicos(email, docId, null);
        Utilizador u = new Utilizador(nombre.trim(), email, password, docId, Perfil.USER);
        return dao.guardar(u);
    }

    public Utilizador crearAdmin(String nombre, String email, String password, String docId) {
        Validacion.obligatorio(nombre, "nombre"); Validacion.obligatorio(email, "email");
        Validacion.obligatorio(password, "password"); Validacion.obligatorio(docId, "docId");
        if (!email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "El email no tiene un formato válido.");
        email = email.trim().toLowerCase(); docId = docId.trim(); comprobarUnicos(email, docId, null);
        return dao.guardar(new Utilizador(nombre.trim(), email, password, docId, Perfil.ADMIN));
    }

    public Utilizador obtener(Long id) { return dao.porId(id).orElseThrow(() -> noEncontrado("Usuario", id)); }
    public List<Utilizador> listar() { return dao.todos(); }

    public Utilizador actualizar(Long id, String nombre, String email, String docId) {
        Utilizador u = obtener(id); Validacion.obligatorio(nombre, "nombre"); Validacion.obligatorio(email, "email"); Validacion.obligatorio(docId, "docId");
        if (!email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "El email no tiene un formato válido.");
        email = email.trim().toLowerCase(); docId = docId.trim(); comprobarUnicos(email, docId, id);
        u.setNome(nombre.trim()); u.setEmail(email); u.setDocId(docId); return dao.actualizar(u);
    }

    public void eliminar(Long id) { obtener(id); dao.eliminarSiSinRelaciones(id); }
    private void comprobarUnicos(String email, String docId, Long excluirId) {
        if (dao.existeEmail(email, excluirId)) throw new ErrorAplicacion(ErrorAplicacion.Tipo.DUPLICADO, "El email ya está registrado.");
        if (dao.existeDocumento(docId, excluirId)) throw new ErrorAplicacion(ErrorAplicacion.Tipo.DUPLICADO, "El documento ya está registrado.");
    }
    private ErrorAplicacion noEncontrado(String tipo, Long id) { return new ErrorAplicacion(ErrorAplicacion.Tipo.NO_ENCONTRADO, tipo + " " + id + " no existe."); }
}
