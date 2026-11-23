package org.example.dao;

import org.example.modelo.Utilizador;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/** Consultas y cambios persistentes de usuarios. */
public class UtilizadorDAO extends BaseDAO {
    public Utilizador guardar(Utilizador utilizador) {
        return ejecutar(s -> { s.persist(utilizador); s.flush(); return utilizador; }, true);
    }

    public Optional<Utilizador> porId(Long id) {
        return ejecutar(s -> Optional.ofNullable(s.find(Utilizador.class, id)), false);
    }

    public Optional<Utilizador> porEmail(String email) {
        return ejecutar(s -> s.createQuery("FROM Utilizador u WHERE lower(u.email) = :email", Utilizador.class)
                .setParameter("email", email.toLowerCase()).uniqueResultOptional(), false);
    }

    public boolean existeEmail(String email, Long excluirId) {
        return ejecutar(s -> s.createQuery("SELECT COUNT(u) FROM Utilizador u WHERE lower(u.email) = :email AND (:id IS NULL OR u.id <> :id)", Long.class)
                .setParameter("email", email.toLowerCase()).setParameter("id", excluirId).uniqueResult() > 0, false);
    }

    public boolean existeDocumento(String docId, Long excluirId) {
        return ejecutar(s -> s.createQuery("SELECT COUNT(u) FROM Utilizador u WHERE u.docId = :doc AND (:id IS NULL OR u.id <> :id)", Long.class)
                .setParameter("doc", docId).setParameter("id", excluirId).uniqueResult() > 0, false);
    }

    public List<Utilizador> todos() {
        return ejecutar(s -> s.createQuery("FROM Utilizador u ORDER BY u.id", Utilizador.class).getResultList(), false);
    }

    public Utilizador actualizar(Utilizador usuario) {
        return ejecutar(s -> s.merge(usuario), true);
    }

    public void eliminar(Long id) {
        ejecutar(s -> { Utilizador u = s.find(Utilizador.class, id); if (u != null) s.remove(u); return null; }, true);
    }

    public boolean tieneRelaciones(Session s, Long id) {
        Long anuncios = s.createQuery("SELECT COUNT(o) FROM ObjetoAchado o WHERE o.publicador.id = :id", Long.class)
                .setParameter("id", id).uniqueResult();
        Long consultas = s.createQuery("SELECT COUNT(r) FROM RegistoContacto r WHERE r.utilizadorQueConsulta.id = :id", Long.class)
                .setParameter("id", id).uniqueResult();
        return anuncios > 0 || consultas > 0;
    }

    public void eliminarSiSinRelaciones(Long id) {
        ejecutar(s -> {
            Utilizador u = s.find(Utilizador.class, id);
            if (u == null) return null;
            if (tieneRelaciones(s, id)) throw new org.example.excepcion.ErrorAplicacion(
                    org.example.excepcion.ErrorAplicacion.Tipo.INTEGRIDAD, "No se puede eliminar: el usuario tiene registros asociados.");
            s.remove(u); return null;
        }, true);
    }
}
