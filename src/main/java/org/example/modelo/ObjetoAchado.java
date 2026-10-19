package org.example.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Representa um objeto encontrado publicado para que possa ser identificado
 * e eventualmente reclamado.
 *
 * <p>Guarda as informações do objeto, incluindo título, descrição, categoria,
 * cor, localização, data em que foi encontrado, contacto e imagem. O estado
 * indica a situação atual do registo, e o publicador identifica o utilizador
 * associado à publicação.
 *
 * <p>Esta classe é uma entidade JPA persistida na tabela
 * {@code objetos_achados}. Os campos de categoria, concelho e estado são
 * armazenados como texto, e cada objeto está associado a um utilizador
 * publicador.
 */
@Entity //Tabela
@Table(name = "objetos_achados")
public class ObjetoAchado {
    @Id //Chave primária
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
    private ConcelhoPorto concelho; //

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
    @JoinColumn(name = "utilizador_id", nullable = false) // Chave estrangeira para a tabela de utilizadores
    private Utilizador publicador;

    /**
     * Cria um objeto encontrado sem inicializar os seus campos.
     *
     * <p>Este construtor vazio é utilizado pelo JPA ao carregar a entidade.</p>
     */
    public ObjetoAchado() {
    }

    /**
     * Cria um objeto encontrado com os dados fornecidos.
     *
     * @param titulo título que identifica o objeto
     * @param descricao descrição do objeto
     * @param categoria categoria a que o objeto pertence
     * @param cor cor do objeto
     * @param concelho concelho onde o objeto foi encontrado
     * @param local local específico onde o objeto foi encontrado
     * @param dataEncontrado data em que o objeto foi encontrado
     * @param contactoDireto contacto para tratar da recuperação do objeto
     * @param imagemUrl endereço da imagem do objeto
     * @param estado estado atual do registo
     * @param publicador utilizador que publicou o objeto
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

    /** @return identificador único do objeto */
    public Long getId() {
        return id;
    }

    /** @param id identificador único a atribuir ao objeto */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return título que identifica o objeto */
    public String getTitulo() {
        return titulo;
    }

    /** @param titulo título a atribuir ao objeto */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /** @return descrição do objeto */
    public String getDescricao() {
        return descricao;
    }

    /** @param descricao descrição a atribuir ao objeto */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /** @return categoria a que o objeto pertence */
    public CategoriaObjeto getCategoria() {
        return categoria;
    }

    /** @param categoria categoria a atribuir ao objeto */
    public void setCategoria(CategoriaObjeto categoria) {
        this.categoria = categoria;
    }

    /** @return cor do objeto */
    public String getCor() {
        return cor;
    }

    /** @param cor cor a atribuir ao objeto */
    public void setCor(String cor) {
        this.cor = cor;
    }

    /** @return concelho onde o objeto foi encontrado */
    public ConcelhoPorto getConcelho() {
        return concelho;
    }

    /** @param concelho concelho a atribuir ao objeto */
    public void setConcelho(ConcelhoPorto concelho) {
        this.concelho = concelho;
    }

    /** @return local específico onde o objeto foi encontrado */
    public String getLocal() {
        return local;
    }

    /** @param local local específico a atribuir ao objeto */
    public void setLocal(String local) {
        this.local = local;
    }

    /** @return data em que o objeto foi encontrado */
    public LocalDate getDataEncontrado() {
        return dataEncontrado;
    }

    /** @param dataEncontrado data em que o objeto foi encontrado */
    public void setDataEncontrado(LocalDate dataEncontrado) {
        this.dataEncontrado = dataEncontrado;
    }

    /** @return contacto para tratar da recuperação do objeto */
    public String getContactoDireto() {
        return contactoDireto;
    }

    /** @param contactoDireto contacto a associar ao objeto */
    public void setContactoDireto(String contactoDireto) {
        this.contactoDireto = contactoDireto;
    }

    /** @return endereço da imagem do objeto */
    public String getImagemUrl() {
        return imagemUrl;
    }

    /** @param imagemUrl endereço da imagem a associar ao objeto */
    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    /** @return estado atual do registo do objeto */
    public EstadoObjeto getEstado() {
        return estado;
    }

    /** @param estado estado a atribuir ao registo do objeto */
    public void setEstado(EstadoObjeto estado) {
        this.estado = estado;
    }

    /** @return utilizador que publicou o objeto */
    public Utilizador getPublicador() {
        return publicador;
    }

    /** @param publicador utilizador a associar à publicação do objeto */
    public void setPublicador(Utilizador publicador) {
        this.publicador = publicador;
    }
}
