package org.example.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registos_contacto")

public class RegistoContacto {

    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Automatic id generation
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataConsulta;

    // Relation to the user that's asking for the contact N:1
    @ManyToOne
    @JoinColumn(name = "utilizador_id", nullable = false)
    private Utilizador utilizadorQueConsulta;

    // Relation to the requested object  N:1
    @ManyToOne
    @JoinColumn(name = "objeto_id", nullable = false)
    private ObjetoAchado objetoConsultado;

    // Empty constructor
    public RegistoContacto() {
    }

    // Constructor with parameters
    public RegistoContacto(LocalDateTime dataConsulta, Utilizador utilizadorQueConsulta, ObjetoAchado objetoConsultado) {
        this.dataConsulta = dataConsulta;
        this.utilizadorQueConsulta = utilizadorQueConsulta;
        this.objetoConsultado = objetoConsultado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public Utilizador getUtilizadorQueConsulta() {
        return utilizadorQueConsulta;
    }

    public void setUtilizadorQueConsulta(Utilizador utilizadorQueConsulta) {
        this.utilizadorQueConsulta = utilizadorQueConsulta;
    }

    public ObjetoAchado getObjetoConsultado() {
        return objetoConsultado;
    }

    public void setObjetoConsultado(ObjetoAchado objetoConsultado) {
        this.objetoConsultado = objetoConsultado;
    }
}
