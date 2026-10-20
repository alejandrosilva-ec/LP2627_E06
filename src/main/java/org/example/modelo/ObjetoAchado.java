package org.example.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Representa um objeto encontrado e registado no sistema.
 *
 * Guarda as informações principais do objeto (como título, categoria,
 * localização e data) e associa esta publicação ao utilizador que o encontrou.
 *
 * @author Tomas Bernardo
 */
@Entity
@Table(name = "objetos_achados")
public class ObjetoAchado {

    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaObjeto categoria;

    @Column(nullable = false)
    private String cor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConcelhoPorto concelho;

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

    // Relacionamento 1:N (Um utilizador publica vários objetos)
    @ManyToOne
    @JoinColumn(name = "utilizador_id", nullable = false)
    private Utilizador publicador;

    /**
     * Construtor vazio obrigatório para o funcionamento do Hibernate/JPA.
     */
    public ObjetoAchado() {
    }

    /**
     * Cria um novo registo de um objeto encontrado com todos os dados necessários.
     *
     * @param titulo título do anúncio
     * @param descricao descrição detalhada do objeto
     * @param categoria categoria a que o objeto pertence
     * @param cor cor principal do objeto
     * @param concelho concelho do distrito do Porto onde foi encontrado
     * @param local local específico ou aproximado
     * @param dataEncontrado data em que o objeto foi recolhido
     * @param contactoDireto contacto telefónico ou email para devolução
     * @param imagemUrl endereço (URL) da fotografia do objeto
     * @param estado estado atual do processo (ex: ABERTO)
     * @param publicador o utilizador que registou o objeto no sistema
     */
    public ObjetoAchado(String titulo, String descricao, CategoriaObjeto categoria, String cor, ConcelhoPorto concelho, String local, LocalDate dataEncontrado, String contactoDireto, String imagemUrl, EstadoObjeto estado, Utilizador publicador) {
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

    // --- GETTERS E SETTERS ---

    /** @return o identificador único do objeto */
    public Long getId() {
        return id;
    }

    /** @param id o identificador único a atribuir */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return o título do anúncio */
    public String getTitulo() {
        return titulo;
    }

    /** @param titulo o título a atribuir */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /** @return a descrição detalhada do objeto */
    public String getDescricao() {
        return descricao;
    }

    /** @param descricao a descrição a atribuir */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /** @return a categoria do objeto */
    public CategoriaObjeto getCategoria() {
        return categoria;
    }

    /** @param categoria a categoria a atribuir */
    public void setCategoria(CategoriaObjeto categoria) {
        this.categoria = categoria;
    }

    /** @return a cor do objeto */
    public String getCor() {
        return cor;
    }

    /** @param cor a cor a atribuir */
    public void setCor(String cor) {
        this.cor = cor;
    }

    /** @return o concelho onde foi encontrado */
    public ConcelhoPorto getConcelho() {
        return concelho;
    }

    /** @param concelho o concelho a atribuir */
    public void setConcelho(ConcelhoPorto concelho) {
        this.concelho = concelho;
    }

    /** @return o local específico da recolha */
    public String getLocal() {
        return local;
    }

    /** @param local o local a atribuir */
    public void setLocal(String local) {
        this.local = local;
    }

    /** @return a data em que foi encontrado */
    public LocalDate getDataEncontrado() {
        return dataEncontrado;
    }

    /** @param dataEncontrado a data a atribuir */
    public void setDataEncontrado(LocalDate dataEncontrado) {
        this.dataEncontrado = dataEncontrado;
    }

    /** @return o contacto para devolução */
    public String getContactoDireto() {
        return contactoDireto;
    }

    /** @param contactoDireto o contacto a atribuir */
    public void setContactoDireto(String contactoDireto) {
        this.contactoDireto = contactoDireto;
    }

    /** @return o link da fotografia do objeto */
    public String getImagemUrl() {
        return imagemUrl;
    }

    /** @param imagemUrl o link da imagem a atribuir */
    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    /** @return o estado atual do objeto */
    public EstadoObjeto getEstado() {
        return estado;
    }

    /** @param estado o estado a atribuir */
    public void setEstado(EstadoObjeto estado) {
        this.estado = estado;
    }

    /** @return o utilizador que publicou o anúncio */
    public Utilizador getPublicador() {
        return publicador;
    }

    /** @param publicador o utilizador a associar à publicação */
    public void setPublicador(Utilizador publicador) {
        this.publicador = publicador;
    }
}