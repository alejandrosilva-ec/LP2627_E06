package org.example.modelo;

/**
 * Define os perfis de acesso disponíveis para os utilizadores do sistema.
 *
 * Estes valores permitem distinguir os papéis de cada utilizador e
 * aplicar as regras de negócio e permissões adequadas a cada um.
 *
 * @author Esteban Silva
 */
public enum Perfil {

    /**
     * Utilizador comum do sistema. Pode publicar objetos, consultar
     * o catálogo e revelar contactos sujeitos a limite semanal.
     */
    USER,

    /**
     * Administrador do sistema. Tem permissões globais de moderação,
     * podendo arquivar publicações e criar outras contas de administração.
     */
    ADMIN
}