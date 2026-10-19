package org.example.modelo;

import jakarta.persistence.*; // Hibernate Tools

/**
 * Representa um utilizador registado na aplicação.
 *
 * <p>Guarda os dados de identificação e acesso do utilizador, incluindo nome,
 * endereço de correio eletrónico, palavra-passe, documento de identificação e
 * perfil. O perfil distingue os diferentes papéis disponíveis, como utilizador
 * comum e administrador.
 *
 * <p>Esta classe é uma entidade JPA persistida na tabela
 * {@code utilizadores}. O endereço de correio eletrónico e o documento de
 * identificação devem ser únicos.
 */
@Entity // Tabela em database
@Table(name = "utilizadores")

public class Utilizador {

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Long usado para aproveitar o NULL

    @Column(nullable = false) // NOT NULL: nome, email, palavraPasse, id, perfil
    private String nome;

    // US02: email e ID únicos
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String palavraPasse;

    @Column(nullable = false, unique = true)
    private String docId;

    @Enumerated(EnumType.STRING) // USER o ADMIN
    @Column(nullable = false)
    private Perfil perfil;

    /**
     * Cria um utilizador sem inicializar os seus campos.
     *
     * <p>Este construtor vazio é utilizado pelo JPA ao carregar a entidade.
     */
    public Utilizador() {
    }

    /**
     * Cria um utilizador com os dados de identificação, acesso e perfil indicados.
     *
     * @param nome nome do utilizador
     * @param email endereço de correio eletrónico do utilizador
     * @param palavraPasse palavra-passe do utilizador
     * @param docId documento de identificação do utilizador
     * @param perfil perfil atribuído ao utilizador
     */
    public Utilizador(String nome, String email, String palavraPasse, String docId, Perfil perfil) {
        this.nome = nome;
        this.email = email;
        this.palavraPasse = palavraPasse;
        this.docId = docId;
        this.perfil = perfil;
    }

    /** @return identificador único do utilizador */
    public Long getId() {
        return id;
    }

    /** @param id identificador a atribuir ao utilizador */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return nome do utilizador */
    public String getNome() {
        return nome;
    }

    /** @param nome nome a atribuir ao utilizador */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** @return endereço de correio eletrónico do utilizador */
    public String getEmail() {
        return email;
    }

    /** @param email endereço de correio eletrónico a atribuir ao utilizador */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return palavra-passe do utilizador */
    public String getPalavraPasse() {
        return palavraPasse;
    }

    /** @param palavraPasse palavra-passe a atribuir ao utilizador */
    public void setPalavraPasse(String palavraPasse) {
        this.palavraPasse = palavraPasse;
    }

    /** @return documento de identificação do utilizador */
    public String getDocumentoIdentificacao() {
        return docId;
    }

    /** @param docId documento de identificação a atribuir ao utilizador */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /** @return perfil atribuído ao utilizador */
    public Perfil getPerfil() {
        return perfil;
    }

    /** @param perfil perfil a atribuir ao utilizador */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
