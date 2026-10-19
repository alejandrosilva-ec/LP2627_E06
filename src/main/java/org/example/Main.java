package org.example;

import org.hibernate.Session; // Representa la sesión física con la base de datos
import org.hibernate.SessionFactory;
import org.hibernate.Transaction; // Insertar o modificar registros
import org.hibernate.cfg.Configuration; // Lee archivos de configuración y registra las clases del modelo mapeadas
import org.example.modelo.*; // Importa mi modelo de datos: Construye SessionFactory

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Iniciar Hibernate y configuración
        System.out.println("A iniciar o Hibernate...");
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            // Iniciar transacción para escribir en la base de datos
            Transaction tx = session.beginTransaction();

            // Creación de base de datos (Seed)

            // Crear Súper Administrador
            Long adminCount = session.createQuery("SELECT COUNT(u) FROM Utilizador u WHERE u.email = 'admin@upt.pt'", Long.class).uniqueResult();
            Utilizador adminPrincipal = null;

            if (adminCount == 0) {
                adminPrincipal = new Utilizador("Admin Supremo", "admin@upt.pt", "1234", "000000000", Perfil.ADMIN);
                session.persist(adminPrincipal);
                System.out.println("-> Super Administrador criado na base de dados.");
            } else {
                adminPrincipal = session.createQuery("FROM Utilizador u WHERE u.email = 'admin@upt.pt'", Utilizador.class).uniqueResult();
            }

            // US02:  Crear un usuario común US02
            Long userCount = session.createQuery("SELECT COUNT(u) FROM Utilizador u WHERE u.email = 'esteban@upt.pt'", Long.class).uniqueResult();
            Utilizador usuarioComun = null;

            if (userCount == 0) {
                usuarioComun = new Utilizador("Esteban Villegas", "esteban@upt.pt", "secreta", "123456789", Perfil.USER);
                session.persist(usuarioComun);
                System.out.println("-> Utilizador comum (Esteban) criado.");
            } else {
                usuarioComun = session.createQuery("FROM Utilizador u WHERE u.email = 'esteban@upt.pt'", Utilizador.class).uniqueResult();
            }

            // (US04 y Relación 1:N) Crear un objeto encontrado asociado al usuario
            Long objCount = session.createQuery("SELECT COUNT(o) FROM ObjetoAchado o", Long.class).uniqueResult();
            if (objCount == 0) {
                ObjetoAchado objeto1 = new ObjetoAchado(
                        "iPhone 16 Perdido", "Encontrado na biblioteca, tem uma capa vermelha.",
                        CategoriaObjeto.ELETRONICA, "Preto",
                        ConcelhoPorto.PORTO, "Biblioteca da UPT",
                        LocalDate.now(), "987654321", "http://imagem.com/iphone.jpg",
                        EstadoObjeto.ABERTO, usuarioComun
                );
                session.persist(objeto1);
                System.out.println("-> Objeto Achado publicado e associado ao utilizador.");
            }

            // Confirmar y guardar todos los cambios anteriores en MySQL
            tx.commit();


            // Ejemplo de consulta JPA (US01)
            System.out.println("\n--- RESULTADOS DA PESQUISA PÚBLICA ---");

            // Buscar objetos filtrando con el concelho de PORTO
            List<ObjetoAchado> objetosEnPorto = session.createQuery(
                            "FROM ObjetoAchado o WHERE o.concelho = :filtroConcelho", ObjetoAchado.class)
                    .setParameter("filtroConcelho", ConcelhoPorto.PORTO)
                    .getResultList();

            System.out.println("A procurar objetos no concelho do PORTO...");
            if (objetosEnPorto.isEmpty()) {
                System.out.println("Não foram encontrados objetos.");
            } else {
                for (ObjetoAchado obj : objetosEnPorto) {
                    System.out.println("- Título: " + obj.getTitulo() + " | Categoria: " + obj.getCategoria() + " | Publicado por: " + obj.getPublicador().getNome());
                }
            }


            // US08: Ejemplo reglas de negocio y administración
            System.out.println("\n--- TESTE DE GESTÃO DE ADMINISTRADORES (US08) ---");

            // 1: User Esteban  intenta crear una cuenta de administrador (Debe fallar)
            System.out.println("Tentativa: 'Esteban' (USER) tenta criar um ADMIN...");
            tentarCriarAdmin(usuarioComun, "Hacker", "hacker@upt.pt", "99999", session);

            // 2: Admin crea un nuevo administrador (Debe funcionar)
            System.out.println("\nTentativa: 'Admin tenta criar um Admin...");
            tentarCriarAdmin(adminPrincipal, "Novo Admin", "admin2@upt.pt", "88888", session);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ends connections 
            session.close();
            sessionFactory.close();
            System.out.println("\nLigação à base de dados encerrada com sucesso.");
        }
    }

    /**
     * Método auxiliar para demostrar la lógica de la US08.
     * Valida el perfil de quien intenta crear la cuenta antes de hacer la inserción.
     */
    public static void tentarCriarAdmin(Utilizador criador, String nome, String email, String doc, Session session) {
        // Validación de la regla de negocio
        if (criador.getPerfil() != Perfil.ADMIN) {
            System.out.println("[ERRO] Permissão negada: Apenas administradores podem criar outras contas ADMIN.");
            return; // Cortamos
        }

        try {
            // Si valida, crea y guarda en la base de datos
            Transaction tx = session.beginTransaction();
            Utilizador novoAdmin = new Utilizador(nome, email, "pass123", doc, Perfil.ADMIN);
            session.persist(novoAdmin);
            tx.commit();
            System.out.println("[SUCESSO] O administrador '" + nome + "' foi criado com sucesso na base de dados.");
        } catch (Exception e) {
            System.out.println("[ERRO] Não foi possível criar o administrador (O email ou documento já existe?).");
        }
    }
}

