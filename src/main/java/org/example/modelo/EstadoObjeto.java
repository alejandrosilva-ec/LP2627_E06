package org.example.modelo;

/**
 * Representa os estados possíveis de um objeto registado.
 *
 * <p>O estado indica em que ponto do processo de gestão o objeto se encontra:
 * {@link #ABERTO} quando o caso ainda está em curso, {@link #RESOLVIDO} quando
 * já foi solucionado e {@link #ARQUIVADO} quando o registo foi arquivado.
 */
public enum EstadoObjeto {
    ABERTO,
    RESOLVIDO,
    ARQUIVADO
}
