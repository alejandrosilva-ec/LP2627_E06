package org.example.dao;

import jakarta.persistence.criteria.Predicate;
import org.example.modelo.CategoriaObjeto;
import org.example.modelo.ConcelhoPorto;
import org.example.modelo.EstadoObjeto;
import org.example.modelo.ObjetoAchado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Consultas y cambios persistentes de objetos encontrados. */
public class ObjetoAchadoDAO extends BaseDAO {
    public ObjetoAchado guardar(ObjetoAchado objeto) {
        return ejecutar(s -> { s.persist(objeto); s.flush(); return objeto; }, true);
    }

    public Optional<ObjetoAchado> porId(Long id) {
        return ejecutar(s -> s.createQuery("SELECT o FROM ObjetoAchado o JOIN FETCH o.publicador WHERE o.id = :id", ObjetoAchado.class)
                .setParameter("id", id).uniqueResultOptional(), false);
    }

    public List<ObjetoAchado> catalogoAbierto(ConcelhoPorto concelho, CategoriaObjeto categoria,
                                               String cor, LocalDate desde, LocalDate ate) {
        return filtrar(EstadoObjeto.ABERTO, concelho, categoria, cor, desde, ate, null);
    }

    public List<ObjetoAchado> todos() { return filtrar(null, null, null, null, null, null, null); }

    public List<ObjetoAchado> porAutor(Long utilizadorId) {
        return filtrar(null, null, null, null, null, null, utilizadorId);
    }

    private List<ObjetoAchado> filtrar(EstadoObjeto estado, ConcelhoPorto concelho, CategoriaObjeto categoria,
                                       String cor, LocalDate desde, LocalDate ate, Long autorId) {
        return ejecutar(s -> {
            var cb = s.getCriteriaBuilder();
            var query = cb.createQuery(ObjetoAchado.class);
            var root = query.from(ObjetoAchado.class);
            root.fetch("publicador", jakarta.persistence.criteria.JoinType.INNER);
            List<Predicate> condiciones = new ArrayList<>();
            if (estado != null) condiciones.add(cb.equal(root.get("estado"), estado));
            if (concelho != null) condiciones.add(cb.equal(root.get("concelho"), concelho));
            if (categoria != null) condiciones.add(cb.equal(root.get("categoria"), categoria));
            if (cor != null && !cor.isBlank()) condiciones.add(cb.equal(cb.lower(root.get("cor")), cor.trim().toLowerCase()));
            if (desde != null) condiciones.add(cb.greaterThanOrEqualTo(root.get("dataEncontrado"), desde));
            if (ate != null) condiciones.add(cb.lessThanOrEqualTo(root.get("dataEncontrado"), ate));
            if (autorId != null) condiciones.add(cb.equal(root.get("publicador").get("id"), autorId));
            query.select(root).where(condiciones.toArray(Predicate[]::new)).orderBy(cb.desc(root.get("dataEncontrado")), cb.desc(root.get("id")));
            return s.createQuery(query).getResultList();
        }, false);
    }

    public ObjetoAchado actualizar(ObjetoAchado objeto) { return ejecutar(s -> s.merge(objeto), true); }

    public void eliminar(Long id) { ejecutar(s -> { ObjetoAchado o = s.find(ObjetoAchado.class, id); if (o != null) s.remove(o); return null; }, true); }

    public void eliminarSiSinConsultas(Long id) {
        ejecutar(s -> {
            Long count = s.createQuery("SELECT COUNT(r) FROM RegistoContacto r WHERE r.objetoConsultado.id = :id", Long.class)
                    .setParameter("id", id).uniqueResult();
            if (count > 0) throw new org.example.excepcion.ErrorAplicacion(
                    org.example.excepcion.ErrorAplicacion.Tipo.INTEGRIDAD, "No se puede eliminar: el objeto tiene auditorías asociadas.");
            ObjetoAchado o = s.find(ObjetoAchado.class, id); if (o != null) s.remove(o); return null;
        }, true);
    }
}
