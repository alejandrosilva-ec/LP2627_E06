package org.example.modelo;

/**
 * Representa os estados possíveis de um objeto registado no sistema.
 *
 * <p>O estado indica em que fase do processo de gestão o objeto se encontra,
 * desde a sua publicação original até ao seu encerramento ou moderação.
 *
 * @author Esteban Silva
 */
public enum EstadoObjeto {

    /**
     * Estado inicial atribuído automaticamente quando o objeto é publicado.
     * Significa que o objeto ainda está à procura do seu dono.
     */
    ABERTO,

    /**
     * O objeto foi entregue com sucesso ao seu legítimo dono.
     * O anúncio é retirado das pesquisas públicas.
     */
    RESOLVIDO,

    /**
     * O anúncio foi ocultado ou removido por um administrador do sistema
     * por motivos de moderação ou suspeita de fraude.
     */
    ARQUIVADO
}