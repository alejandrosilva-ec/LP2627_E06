package org.example.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity //Table
@Table(name = "objetos_achados")

public class ObjetoAchado {
    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String cor;

    @Column(nullable = false)
    private String concelho; // Distrito do Porto

    @Column(nullable = false)
    private String local;

    @Column(nullable = false)
    private LocalDate dataEncontrado;

    @Column(nullable = false)
    private String contactoDireto;

    @Column(nullable = false)
    private String imagemUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoObjeto estado;

    // US04: 1:N
    @ManyToOne
    @JoinColumn(name = "utilizador_id", nullable = false) // Clave Extranjera
    private Utilizador publicador;

    // Constructor vacío
    public ObjetoAchado() {
    }

    // Constructor con parámetros
    public ObjetoAchado(String titulo, String descricao, String categoria, String cor, String concelho, String local, LocalDate dataEncontrado, String contactoDireto, String imagemUrl, EstadoObjeto estado, Utilizador publicador) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.cor = cor;
        this.concelho = concelho;
        this.local = local;
        this.dataEncontrado = dataEncontrado;
        this.contactoDireto = contactoDireto;
        this.imagemUrl = imagemUrl;
        this.estado = estado;
        this.publicador = publicador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getConcelho() {
        return concelho;
    }

    public void setConcelho(String concelho) {
        this.concelho = concelho;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDate getDataEncontrado() {
        return dataEncontrado;
    }

    public void setDataEncontrado(LocalDate dataEncontrado) {
        this.dataEncontrado = dataEncontrado;
    }

    public String getContactoDireto() {
        return contactoDireto;
    }

    public void setContactoDireto(String contactoDireto) {
        this.contactoDireto = contactoDireto;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public EstadoObjeto getEstado() {
        return estado;
    }

    public void setEstado(EstadoObjeto estado) {
        this.estado = estado;
    }

    public Utilizador getPublicador() {
        return publicador;
    }

    public void setPublicador(Utilizador publicador) {
        this.publicador = publicador;
    }
}
