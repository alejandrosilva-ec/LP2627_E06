package org.example.modelo;

/**
 * Representa as categorias possíveis de um objeto encontrado.
 *
 * <p>Este enum classifica cada objeto num grupo, como eletrónica, vestuário
 * ou documentos. Cada valor identifica uma categoria específica e pode ser
 * utilizado para organizar ou pesquisar objetos de acordo com o seu tipo.
 *
 * @author Enzo Pinto
 */
public enum CategoriaObjeto {

    /**
     * Equipamentos eletrónicos como telemóveis, computadores, auscultadores, etc.
     */
    ELETRONICA,

    /**
     * Peças de roupa, casacos, sapatos e similares.
     */
    VESTUARIO,

    /**
     * Adereços de uso pessoal como óculos, relógios, joias e carteiras.
     */
    ACESSORIOS,

    /**
     * Identificações oficiais (cartão de cidadão, passaporte) e cartões bancários.
     */
    DOCUMENTOS,

    /**
     * Material de leitura e estudo, como livros, cadernos e apontamentos universitários.
     */
    LIVROS_CADERNOS,

    /**
     * Produtos de beleza e higiene pessoal, como maquilhagem e perfumes.
     */
    COSMETICOS,

    /**
     * Qualquer outro objeto que não se enquadre nas categorias anteriores.
     */
    OUTROS
}