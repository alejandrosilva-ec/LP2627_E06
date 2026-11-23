package org.example.configuracion;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/** Centraliza la creación y el cierre del acceso Hibernate. */
public final class HibernateUtil {
    private static final SessionFactory FACTORY = crearFactory();

    private HibernateUtil() { }

    private static SessionFactory crearFactory() {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        ponerSiExiste(cfg, "hibernate.connection.url", "DB_URL");
        ponerSiExiste(cfg, "hibernate.connection.username", "DB_USERNAME");
        ponerSiExiste(cfg, "hibernate.connection.password", "DB_PASSWORD");
        String url = cfg.getProperty("hibernate.connection.url");
        String usuario = cfg.getProperty("hibernate.connection.username");
        if (url == null || url.isBlank() || usuario == null || usuario.isBlank()) {
            throw new IllegalStateException("Configure DB_URL y DB_USERNAME antes de iniciar la aplicacion.");
        }
        return cfg.buildSessionFactory();
    }

    private static void ponerSiExiste(Configuration cfg, String property, String variable) {
        String value = System.getenv(variable);
        if (value != null && !value.isBlank()) cfg.setProperty(property, value);
    }

    public static SessionFactory getSessionFactory() { return FACTORY; }

    public static void cerrar() {
        if (!FACTORY.isClosed()) FACTORY.close();
    }
}
