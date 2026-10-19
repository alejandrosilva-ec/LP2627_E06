package org.example.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Regista uma consulta aos dados de contacto de um objeto encontrado.
 *
 * <p>Cada registo guarda a data e hora da consulta, o utilizador que consultou
 * e o objeto cujo contacto foi consultado. Esta informação permite manter um
 * histórico das consultas realizadas.
 *
 * <p>É uma entidade JPA persistida na tabela {@code registos_contacto} e está
 * associada a um utilizador e a um objeto encontrado.
 */
@Entity
@Table(name = "registos_contacto")

public class RegistoContacto {

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática do identificador
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataConsulta;

    // Relação muitos-para-um com o utilizador que consulta os dados de contacto
    @ManyToOne
    @JoinColumn(name = "utilizador_id", nullable = false)
    private Utilizador utilizadorQueConsulta;

    // Relação muitos-para-um com o objeto consultado
    @ManyToOne
    @JoinColumn(name = "objeto_id", nullable = false)
    private ObjetoAchado objetoConsultado;

    /**
     * Cria um registo de contacto sem inicializar os seus campos.
     *
     * <p>Este construtor vazio é utilizado pelo JPA ao carregar a entidade.</p>
     */
    public RegistoContacto() {
    }

    /**
     * Cria um registo de contacto com a data, o utilizador e o objeto indicados.
     *
     * @param dataConsulta data e hora em que os dados de contacto foram consultados
     * @param utilizadorQueConsulta utilizador que consultou os dados de contacto
     * @param objetoConsultado objeto cujos dados de contacto foram consultados
     */
    public RegistoContacto(LocalDateTime dataConsulta, Utilizador utilizadorQueConsulta, ObjetoAchado objetoConsultado) {
        this.dataConsulta = dataConsulta;
        this.utilizadorQueConsulta = utilizadorQueConsulta;
        this.objetoConsultado = objetoConsultado;
    }

    /** @return identificador único deste registo de contacto */
    public Long getId() {
        return id;
    }

    /** @param id identificador a atribuir a este registo */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return data e hora em que os dados de contacto foram consultados */
    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    /** @param dataConsulta data e hora da consulta a registar */
    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    /** @return utilizador que consultou os dados de contacto */
    public Utilizador getUtilizadorQueConsulta() {
        return utilizadorQueConsulta;
    }

    /** @param utilizadorQueConsulta utilizador que realizou a consulta */
    public void setUtilizadorQueConsulta(Utilizador utilizadorQueConsulta) {
        this.utilizadorQueConsulta = utilizadorQueConsulta;
    }

    /** @return objeto cujos dados de contacto foram consultados */
    public ObjetoAchado getObjetoConsultado() {
        return objetoConsultado;
    }

    /** @param objetoConsultado objeto associado a esta consulta */
    public void setObjetoConsultado(ObjetoAchado objetoConsultado) {
        this.objetoConsultado = objetoConsultado;
    }
}
