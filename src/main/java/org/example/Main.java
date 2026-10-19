package org.example;

import org.hibernate.Session; // Representa a sessão com a base de dados
import org.hibernate.SessionFactory;
import org.hibernate.Transaction; // Permite inserir ou alterar registos
import org.hibernate.cfg.Configuration; // Lê a configuração e regista as classes do modelo
import org.example.modelo.*; // Importa as classes do modelo de dados

import java.time.LocalDate;
import java.util.List;

/**
 * Ponto de entrada da aplicação de gestão de objetos achados.
 * <p>
 * Configura o Hibernate, cria dados de exemplo quando ainda não existem,
 * pesquisa objetos publicados num concelho e demonstra a regra de negócio
 * que restringe a criação de contas de administrador a utilizadores com perfil
 * {@link Perfil#ADMIN}. No final, encerra a sessão e a fábrica de sessões.
 */
public class Main {
    /**
     * Inicia a aplicação, prepara os dados de exemplo, executa uma pesquisa
     * pública de objetos no Porto e demonstra a criação autorizada e não
     * autorizada de contas de administrador.
     *
     * @param args argumentos da linha de comandos (não utilizados)
     */
    public static void main(String[] args) {

        // Iniciar o Hibernate e carregar a configuração
        System.out.println("A iniciar o Hibernate...");
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            // Iniciar uma transação para escrever na base de dados
            Transaction tx = session.beginTransaction();

            // Criar dados iniciais na base de dados

            // Criar o superadministrador
            Long adminCount = session.createQuery("SELECT COUNT(u) FROM Utilizador u WHERE u.email = 'admin@upt.pt'", Long.class).uniqueResult();
            Utilizador adminPrincipal = null;

            if (adminCount == 0) {
                adminPrincipal = new Utilizador("Admin Supremo", "admin@upt.pt", "1234", "000000000", Perfil.ADMIN);
                session.persist(adminPrincipal);
                System.out.println("-> Super Administrador criado na base de dados.");
            } else {
                adminPrincipal = session.createQuery("FROM Utilizador u WHERE u.email = 'admin@upt.pt'", Utilizador.class).uniqueResult();
            }

            // US02: Criar um utilizador comum
            Long userCount = session.createQuery("SELECT COUNT(u) FROM Utilizador u WHERE u.email = 'esteban@upt.pt'", Long.class).uniqueResult();
            Utilizador usuarioComun = null;

            if (userCount == 0) {
                usuarioComun = new Utilizador("Esteban Villegas", "esteban@upt.pt", "secreta", "123456789", Perfil.USER);
                session.persist(usuarioComun);
                System.out.println("-> Utilizador comum (Esteban) criado.");
            } else {
                usuarioComun = session.createQuery("FROM Utilizador u WHERE u.email = 'esteban@upt.pt'", Utilizador.class).uniqueResult();
            }

            // US04 e relação 1:N: criar um objeto encontrado associado ao utilizador
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

            // Confirmar e guardar todas as alterações anteriores na base de dados
            tx.commit();


            // Exemplo de consulta JPA (US01)
            System.out.println("\n--- RESULTADOS DA PESQUISA PÚBLICA ---");

            // Pesquisar objetos filtrando pelo concelho do Porto
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


            // US08: Exemplo de regras de negócio e administração
            System.out.println("\n--- TESTE DE GESTÃO DE ADMINISTRADORES (US08) ---");

            // 1: Esteban tenta criar uma conta de administrador (deve falhar)
            System.out.println("Tentativa: 'Esteban' (USER) tenta criar um ADMIN...");
            tentarCriarAdmin(usuarioComun, "Hacker", "hacker@upt.pt", "99999", session);

            // 2: O administrador cria uma nova conta de administrador (deve funcionar)
            System.out.println("\nTentativa: 'Admin tenta criar um Admin...");
            tentarCriarAdmin(adminPrincipal, "Novo Admin", "admin2@upt.pt", "88888", session);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Encerrar as ligações
            session.close();
            sessionFactory.close();
            System.out.println("\nLigação à base de dados encerrada com sucesso.");
        }
    }

    /**
     * Cria uma conta de administrador se o utilizador indicado tiver perfil
     * {@link Perfil#ADMIN}. Se o perfil não tiver autorização, regista a recusa
     * e não insere a conta. Quando autorizado, guarda a nova conta numa
     * transação usando a sessão Hibernate fornecida.
     *
     * @param criador utilizador que solicita a criação da conta
     * @param nome nome do novo administrador
     * @param email endereço de email do novo administrador
     * @param doc documento de identificação do novo administrador
     * @param session sessão Hibernate usada para persistir a conta
     */
    public static void tentarCriarAdmin(Utilizador criador, String nome, String email, String doc, Session session) {
        // Validar a regra de negócio
        if (criador.getPerfil() != Perfil.ADMIN) {
            System.out.println("[ERRO] Permissão negada: Apenas administradores podem criar outras contas ADMIN.");
            return; // Interromper a execução sem criar a conta
        }

        try {
            // Se a validação for bem-sucedida, criar e guardar a conta na base de dados
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
