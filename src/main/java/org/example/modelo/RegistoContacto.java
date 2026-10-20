package org.example.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Representa o registo histórico de uma consulta aos contactos de um objeto.
 *
 * Serve para auditar e controlar quantas vezes um utilizador revelou
 * o contacto de outras pessoas, permitindo aplicar a regra do limite
 * semanal de segurança (mecanismo antifraude).
 *
 * @author Jorge Pereira
 */
@Entity
@Table(name = "registos_contacto")
public class RegistoContacto {

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataConsulta;

    // Relação 1:N (Um utilizador faz vários registos de consulta)
    @ManyToOne
    @JoinColumn(name = "utilizador_id", nullable = false)
    private Utilizador utilizadorQueConsulta;

    // Relação 1:N (Um objeto é consultado em vários registos)
    @ManyToOne
    @JoinColumn(name = "objeto_id", nullable = false)
    private ObjetoAchado objetoConsultado;

    /**
     * Construtor vazio obrigatório para o funcionamento do Hibernate/JPA.
     */
    public RegistoContacto() {
    }

    /**
     * Cria um novo registo histórico de consulta de contacto.
     *
     * @param dataConsulta data e hora exata em que o contacto foi revelado
     * @param utilizadorQueConsulta o utilizador que solicitou ver o contacto
     * @param objetoConsultado o objeto cujo contacto foi revelado
     */
    public RegistoContacto(LocalDateTime dataConsulta, Utilizador utilizadorQueConsulta, ObjetoAchado objetoConsultado) {
        this.dataConsulta = dataConsulta;
        this.utilizadorQueConsulta = utilizadorQueConsulta;
        this.objetoConsultado = objetoConsultado;
    }

    // --- GETTERS E SETTERS ---

    /** @return o identificador único deste registo */
    public Long getId() {
        return id;
    }

    /** @param id o identificador a atribuir a este registo */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return a data e hora em que a consulta ocorreu */
    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    /** @param dataConsulta a data e hora da consulta a registar */
    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    /** @return o utilizador que realizou a consulta */
    public Utilizador getUtilizadorQueConsulta() {
        return utilizadorQueConsulta;
    }

    /** @param utilizadorQueConsulta o utilizador a associar a esta consulta */
    public void setUtilizadorQueConsulta(Utilizador utilizadorQueConsulta) {
        this.utilizadorQueConsulta = utilizadorQueConsulta;
    }

    /** @return o objeto que foi consultado */
    public ObjetoAchado getObjetoConsultado() {
        return objetoConsultado;
    }

    /** @param objetoConsultado o objeto a associar a esta consulta */
    public void setObjetoConsultado(ObjetoAchado objetoConsultado) {
        this.objetoConsultado = objetoConsultado;
    }
}