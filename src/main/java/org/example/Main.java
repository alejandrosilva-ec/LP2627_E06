package org.example;
import org.hibernate.Session; // Representa la sesión física con la base de datos
import org.hibernate.Transaction; // Insertar o modificar registros
import org.example.modelo.*; // Importa mi modelo de datos: Construye SessionFactory

public class Main {
    public static void main(String[] args) {

    /**
     * Metodo para Testar US08.
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
}}