package org.example.dao;

import org.example.modelo.RegistoContacto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/** Guarda y consulta las auditorías de visualización de contactos. */
public class RegistoContactoDAO extends BaseDAO {
    public RegistoContacto guardar(RegistoContacto registro) {
        return ejecutar(s -> { s.persist(registro); s.flush(); return registro; }, true);
    }
    public Optional<RegistoContacto> porId(Long id) {
        return ejecutar(s -> s.createQuery("SELECT r FROM RegistoContacto r JOIN FETCH r.utilizadorQueConsulta JOIN FETCH r.objetoConsultado WHERE r.id = :id", RegistoContacto.class)
                .setParameter("id", id).uniqueResultOptional(), false);
    }
    public List<RegistoContacto> porUsuarioDesde(Long usuarioId, LocalDateTime desde) {
        return ejecutar(s -> s.createQuery("SELECT r FROM RegistoContacto r JOIN FETCH r.utilizadorQueConsulta JOIN FETCH r.objetoConsultado WHERE r.utilizadorQueConsulta.id = :uid AND r.dataConsulta >= :desde ORDER BY r.dataConsulta", RegistoContacto.class)
                .setParameter("uid", usuarioId).setParameter("desde", desde).getResultList(), false);
    }
    public List<RegistoContacto> todos() {
        return ejecutar(s -> s.createQuery("SELECT r FROM RegistoContacto r JOIN FETCH r.utilizadorQueConsulta JOIN FETCH r.objetoConsultado ORDER BY r.dataConsulta DESC", RegistoContacto.class).getResultList(), false);
    }
    public RegistoContacto actualizar(RegistoContacto registro) { return ejecutar(s -> s.merge(registro), true); }
    public void eliminar(Long id) { ejecutar(s -> { RegistoContacto r = s.find(RegistoContacto.class, id); if (r != null) s.remove(r); return null; }, true); }
}
