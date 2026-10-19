package org.example.modelo;

import jakarta.persistence.*; // Herramientas de Hibernate

@Entity // Tabla en la db
@Table(name = "utilizadores")

public class Utilizador {

    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // NOT NULL
    private String nome;

    // US02: email y id únicos
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String palavraPasse;

    @Column(nullable = false, unique = true)
    private String documentoIdentificacao;

    @Enumerated(EnumType.STRING) // USER o ADMIN
    @Column(nullable = false)
    private Perfil perfil;

    public Utilizador() {
    }

    // Constructor
    public Utilizador(String nome, String email, String palavraPasse, String documentoIdentificacao, Perfil perfil) {
        this.nome = nome;
        this.email = email;
        this.palavraPasse = palavraPasse;
        this.documentoIdentificacao = documentoIdentificacao;
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPalavraPasse() {
        return palavraPasse;
    }

    public void setPalavraPasse(String palavraPasse) {
        this.palavraPasse = palavraPasse;
    }

    public String getDocumentoIdentificacao() {
        return documentoIdentificacao;
    }

    public void setDocumentoIdentificacao(String documentoIdentificacao) {
        this.documentoIdentificacao = documentoIdentificacao;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}


