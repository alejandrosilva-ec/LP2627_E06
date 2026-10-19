package org.example.modelo;

/**
 * Representa as categorias possíveis de um objeto encontrado.
 *
 * <p>Este enum classifica cada objeto em um grupo, como eletrônica, vestuário
 * ou documentos. Cada valor identifica uma categoria específica e pode ser
 * usado para organizar ou buscar objetos de acordo com o seu tipo.
 */
public enum CategoriaObjeto {
    ELETRONICA,       // Telemóveis, computadores, auscultadores
    VESTUARIO,        // Roupas, casacos, sapatos
    ACESSORIOS,       // Óculos, relógios, joias, carteiras
    DOCUMENTOS,       // Cartões de cidadão, passaportes, cartões bancários
    LIVROS_CADERNOS,  // Livros, cadernos, apontamentos universitários
    COSMETICOS,       // Maquiagem, perfumes
    OUTROS            // Qualquer outro objeto
}
