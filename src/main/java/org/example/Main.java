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
 *
 * Este ficheiro atua como um ambiente de testes automatizado.
 * Configura o Hibernate, cria dados de exemplo na base de dados caso não existam,
 * executa consultas públicas de pesquisa e demonstra as regras de negócio
 * exigidas para o Componente 1 do projeto (como restrições de perfis).
 *
 * @author Joao Nascimento
 * @author Enzo Pinto
 * @author Esteban Silva
 */
public class Main {

    /**
     * Inicia a aplicação e executa sequencialmente os testes de persistência.
     *
     * Prepara os dados iniciais (utilizadores e objetos), efetua uma pesquisa
     * filtrada por concelho (US01) e testa os bloqueios de segurança na criação
     * de novas contas de administrador (US08). No final, encerra a sessão de forma segura.
     *
     * @param args argumentos da linha de comandos (não utilizados neste projeto)
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
     * Tenta criar uma nova conta de administrador validando primeiro as permissões do criador.
     *
     * De acordo com as regras de negócio, apenas utilizadores com o perfil ADMIN
     * podem promover ou criar outras contas ADMIN. Se a validação falhar, o método
     * bloqueia a operação e emite um aviso. Se tiver sucesso, guarda o novo administrador.
     *
     * @param criador o utilizador que está a tentar executar a operação
     * @param nome o nome do novo administrador
     * @param email o endereço de email do novo administrador
     * @param doc o documento de identificação do novo administrador
     * @param session a sessão ativa do Hibernate usada para persistir a conta
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