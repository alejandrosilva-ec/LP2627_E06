package org.example.dao;

import org.example.configuracion.HibernateUtil;
import org.example.excepcion.ErrorAplicacion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import java.sql.SQLException;

import java.util.function.Function;

/** Ejecuta cada operación con sesión y transacción cerradas de forma segura. */
abstract class BaseDAO {
    protected <T> T ejecutar(Function<Session, T> operacion, boolean escritura) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            T resultado = operacion.apply(session);
            tx.commit();
            return resultado;
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (RuntimeException rollback) { ex.addSuppressed(rollback); }
            }
            if (ex instanceof ErrorAplicacion) throw ex;
            if (esDuplicado(ex)) {
                throw new ErrorAplicacion(ErrorAplicacion.Tipo.DUPLICADO, "El email o el documento ya están registrados.", ex);
            }
            if (esRestriccion(ex)) throw new ErrorAplicacion(ErrorAplicacion.Tipo.INTEGRIDAD, "La operación viola una restricción de integridad de la base de datos.", ex);
            throw new ErrorAplicacion(ErrorAplicacion.Tipo.BASE_DATOS,
                    escritura ? "No se pudo guardar el cambio en la base de datos." : "No se pudo consultar la base de datos.", ex);
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    private boolean esDuplicado(Throwable error) {
        for (Throwable causa = error; causa != null; causa = causa.getCause()) {
            if (causa instanceof SQLException sql && (sql.getErrorCode() == 1062 || "23505".equals(sql.getSQLState()))) return true;
        }
        return false;
    }

    private boolean esRestriccion(Throwable error) {
        for (Throwable causa = error; causa != null; causa = causa.getCause()) if (causa instanceof ConstraintViolationException) return true;
        return false;
    }
}
