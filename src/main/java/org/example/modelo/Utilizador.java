package org.example.modelo;

import jakarta.persistence.*; // Ferramentas do Hibernate

/**
 * Representa um utilizador registado no sistema.
 *
 * Guarda os dados de identificação e acesso da conta, incluindo nome,
 * email, palavra-passe, documento de identificação e o perfil.
 * Para garantir a integridade dos dados (US02), o sistema assegura que
 * o email e o documento de identificação são únicos para cada utilizador.
 *
 * @author Esteban Silva
 * @author Enzo Pinto
 */
@Entity
@Table(name = "utilizadores")
public class Utilizador {

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Long usado para aproveitar o valor nulo (null) antes da persistência

    @Column(nullable = false)
    private String nome;

    // US02: email e documento de identificação devem ser únicos
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String palavraPasse;

    @Column(nullable = false, unique = true)
    private String docId;

    @Enumerated(EnumType.STRING) // Guarda como texto (USER ou ADMIN) na base de dados
    @Column(nullable = false)
    private Perfil perfil;

    /**
     * Construtor vazio obrigatório para o funcionamento do Hibernate/JPA.
     */
    public Utilizador() {
    }

    /**
     * Cria um novo utilizador com todos os dados de identificação e acesso.
     *
     * @param nome o nome completo do utilizador
     * @param email o endereço de correio eletrónico (deve ser único)
     * @param palavraPasse a palavra-passe de acesso à plataforma
     * @param docId o número do documento de identificação (NIF/Passaporte, único)
     * @param perfil o perfil de permissões atribuído (USER ou ADMIN)
     */
    public Utilizador(String nome, String email, String palavraPasse, String docId, Perfil perfil) {
        this.nome = nome;
        this.email = email;
        this.palavraPasse = palavraPasse;
        this.docId = docId;
        this.perfil = perfil;
    }

    // --- GETTERS E SETTERS ---

    /** @return o identificador único do utilizador */
    public Long getId() {
        return id;
    }

    /** @param id o identificador a atribuir ao utilizador */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return o nome do utilizador */
    public String getNome() {
        return nome;
    }

    /** @param nome o nome a atribuir ao utilizador */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** @return o endereço de correio eletrónico do utilizador */
    public String getEmail() {
        return email;
    }

    /** @param email o email a atribuir ao utilizador */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return a palavra-passe da conta do utilizador */
    public String getPalavraPasse() {
        return palavraPasse;
    }

    /** @param palavraPasse a palavra-passe a atribuir */
    public void setPalavraPasse(String palavraPasse) {
        this.palavraPasse = palavraPasse;
    }

    /** @return o documento de identificação do utilizador */
    public String getDocumentoIdentificacao() {
        return docId;
    }

    /** @param docId o documento de identificação a atribuir */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /** @return o perfil de acesso atribuído ao utilizador */
    public Perfil getPerfil() {
        return perfil;
    }

    /** @param perfil o perfil a atribuir (USER ou ADMIN) */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}