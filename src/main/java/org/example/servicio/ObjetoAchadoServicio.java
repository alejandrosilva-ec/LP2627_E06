package org.example.servicio;

import org.example.dao.ObjetoAchadoDAO;
import org.example.dao.UtilizadorDAO;
import org.example.excepcion.ErrorAplicacion;
import org.example.modelo.CategoriaObjeto;
import org.example.modelo.ConcelhoPorto;
import org.example.modelo.EstadoObjeto;
import org.example.modelo.ObjetoAchado;
import org.example.modelo.Utilizador;

import java.time.LocalDate;
import java.util.List;

/** Reglas de negocio para anuncios de objetos encontrados. */
public class ObjetoAchadoServicio {
    private final ObjetoAchadoDAO dao;
    private final UtilizadorDAO utilizadores;
    public ObjetoAchadoServicio() { this(new ObjetoAchadoDAO(), new UtilizadorDAO()); }
    public ObjetoAchadoServicio(ObjetoAchadoDAO dao, UtilizadorDAO utilizadores) { this.dao = dao; this.utilizadores = utilizadores; }

    public ObjetoAchado publicar(String titulo, String descricao, CategoriaObjeto categoria, String cor,
                                  String concelhoTexto, String local, LocalDate data, String contacto,
                                  String imagemUrl, Long publicadorId) {
        Validacion.obligatorio(titulo, "titulo"); Validacion.obligatorio(descricao, "descricao");
        Validacion.obligatorio(cor, "cor"); Validacion.obligatorio(local, "local");
        Validacion.obligatorio(contacto, "contactoDireto"); Validacion.urlImagen(imagemUrl); Validacion.concelho(concelhoTexto);
        if (categoria == null) throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "La categoría es obligatoria.");
        if (data == null || data.isAfter(LocalDate.now())) throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "La fecha del hallazgo no puede ser futura ni estar vacía.");
        Utilizador autor = utilizadores.porId(publicadorId).orElseThrow(() -> new ErrorAplicacion(ErrorAplicacion.Tipo.NO_ENCONTRADO, "El usuario publicador no existe."));
        ObjetoAchado o = new ObjetoAchado(titulo.trim(), descricao.trim(), categoria, cor.trim(),
                ConcelhoPorto.valueOf(concelhoTexto.trim().toUpperCase().replace(' ', '_')), local.trim(), data,
                contacto.trim(), imagemUrl.trim(), EstadoObjeto.ABERTO, autor);
        return dao.guardar(o);
    }

    public ObjetoAchado obtener(Long id) { return dao.porId(id).orElseThrow(() -> noEncontrado(id)); }
    public List<ObjetoAchado> catalogo(ConcelhoPorto concelho, CategoriaObjeto categoria, String cor, LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "La fecha inicial debe ser anterior a la final.");
        return dao.catalogoAbierto(concelho, categoria, cor, desde, hasta);
    }
    public List<ObjetoAchado> todos() { return dao.todos(); }
    public List<ObjetoAchado> porAutor(Long autorId) {
        if (utilizadores.porId(autorId).isEmpty()) throw new ErrorAplicacion(ErrorAplicacion.Tipo.NO_ENCONTRADO, "El usuario " + autorId + " no existe.");
        return dao.porAutor(autorId);
    }

    public ObjetoAchado actualizar(Long id, String titulo, String descripcion, CategoriaObjeto categoria, String cor,
                                    String concelhoTexto, String local, LocalDate data, String contacto, String imagenUrl) {
        ObjetoAchado o = obtener(id); Validacion.obligatorio(titulo, "titulo"); Validacion.obligatorio(descripcion, "descricao");
        Validacion.obligatorio(cor, "cor"); Validacion.obligatorio(local, "local"); Validacion.obligatorio(contacto, "contactoDireto");
        Validacion.urlImagen(imagenUrl); Validacion.concelho(concelhoTexto);
        if (categoria == null || data == null || data.isAfter(LocalDate.now())) throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "Categoría y fecha válida son obligatorias.");
        o.setTitulo(titulo.trim()); o.setDescricao(descripcion.trim()); o.setCategoria(categoria); o.setCor(cor.trim());
        o.setConcelho(ConcelhoPorto.valueOf(concelhoTexto.trim().toUpperCase().replace(' ', '_')));
        o.setLocal(local.trim()); o.setDataEncontrado(data); o.setContactoDireto(contacto.trim()); o.setImagemUrl(imagenUrl.trim());
        return dao.actualizar(o);
    }

    public ObjetoAchado cambiarEstado(Long id, EstadoObjeto estado) {
        if (estado == null) throw new ErrorAplicacion(ErrorAplicacion.Tipo.VALIDACION, "El estado es obligatorio.");
        ObjetoAchado o = obtener(id); o.setEstado(estado); return dao.actualizar(o);
    }
    public void eliminar(Long id) { obtener(id); dao.eliminarSiSinConsultas(id); }
    private ErrorAplicacion noEncontrado(Long id) { return new ErrorAplicacion(ErrorAplicacion.Tipo.NO_ENCONTRADO, "El objeto " + id + " no existe."); }
}
