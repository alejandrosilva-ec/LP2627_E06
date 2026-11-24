package org.example;

import org.example.configuracion.HibernateUtil;
import org.example.excepcion.ErrorAplicacion;
import org.example.modelo.CategoriaObjeto;
import org.example.modelo.ConcelhoPorto;
import org.example.modelo.EstadoObjeto;
import org.example.modelo.ObjetoAchado;
import org.example.modelo.Utilizador;
import org.example.servicio.ObjetoAchadoServicio;
import org.example.servicio.UtilizadorServicio;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Menú de consola para probar los servicios del Componente 1. */
public class Main {
    private final Scanner scanner;
    private final UtilizadorServicio usuarios = new UtilizadorServicio();
    private final ObjetoAchadoServicio objetos = new ObjetoAchadoServicio();
    private boolean continuar = true;

    private Main(Scanner scanner) { this.scanner = scanner; }

    /** Inicia el menú y cierra los recursos al salir. */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean hibernateIniciado = false;
        try {
            HibernateUtil.getSessionFactory();
            hibernateIniciado = true;
            new Main(scanner).iniciar();
        } catch (FinEntrada ex) {
            System.out.println("\nFin de la entrada. Cerrando la aplicación.");
        } catch (ExceptionInInitializerError ex) {
            Throwable causa = ex.getCause();
            System.out.println("[ERROR DE CONFIGURACIÓN] " + (causa == null ? ex.getMessage() : causa.getMessage()));
        } finally {
            scanner.close();
            if (hibernateIniciado) HibernateUtil.cerrar();
            System.out.println("Aplicación cerrada.");
        }
    }

    /** Mantiene el menú activo hasta que se elige salir. */
    private void iniciar() {
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Elige una opción: ");
            try {
                ejecutarOpcion(opcion);
            } catch (ErrorAplicacion ex) {
                System.out.println("[" + ex.getTipo() + "] " + ex.getMessage());
            } catch (FinEntrada ex) {
                throw ex;
            } catch (RuntimeException ex) {
                System.out.println("[ERROR] No se pudo completar la operación. " + mensajeSeguro(ex));
            }
            if (continuar) System.out.println();
        }
    }

    /** Presenta las opciones disponibles. */
    private void mostrarMenu() {
        System.out.println("============================================");
        System.out.println(" SISTEMA DE OBJETOS PERDIDOS Y ENCONTRADOS");
        System.out.println("============================================");
        System.out.println("USUARIOS");
        System.out.println(" 1. Registrar usuario común");
        System.out.println(" 2. Crear administrador (requiere ID de ADMIN)");
        System.out.println(" 3. Listar usuarios");
        System.out.println(" 4. Buscar usuario por ID o email");
        System.out.println("OBJETOS");
        System.out.println(" 5. Publicar objeto encontrado");
        System.out.println(" 6. Actualizar datos o estado de un objeto");
        System.out.println(" 7. Eliminar objeto por ID");
        System.out.println("CATÁLOGO Y CONSULTAS");
        System.out.println(" 8. Catálogo ABERTO con filtros opcionales");
        System.out.println(" 9. Objetos publicados por autor");
        System.out.println("10. Buscar objeto por ID");
        System.out.println("11. Listado global de objetos");
        System.out.println("DEMOSTRACIÓN");
        System.out.println("12. Cargar datos de prueba (solo bajo petición)");
        System.out.println(" 0. Salir");
    }

    /** Ejecuta la opción seleccionada y conserva el menú ante opciones incorrectas. */
    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> registrarUsuario();
            case 2 -> crearAdministrador();
            case 3 -> listarUsuarios();
            case 4 -> buscarUsuario();
            case 5 -> publicarObjeto();
            case 6 -> actualizarObjeto();
            case 7 -> eliminarObjeto();
            case 8 -> buscarCatalogo();
            case 9 -> objetosPorAutor();
            case 10 -> buscarObjetoPorId();
            case 11 -> listarTodosLosObjetos();
            case 12 -> cargarDatosDePrueba();
            case 0 -> { continuar = false; System.out.println("Hasta pronto."); }
            default -> System.out.println("Opción fuera de rango. Elige un número del 0 al 12.");
        }
    }

    /** Registra un usuario con perfil USER asignado por el servicio. */
    private void registrarUsuario() {
        System.out.println("\n--- Registro de usuario común ---");
        Utilizador u = usuarios.registrar(leerTexto("Nombre completo: "), leerTexto("Email: "),
                leerTexto("Contraseña: "), leerTexto("Documento de identificación: "));
        imprimirUsuario(u);
    }

    /** Solicita el ID del administrador que autoriza la creación de otro ADMIN. */
    private void crearAdministrador() {
        System.out.println("\n--- Crear administrador ---");
        Long creadorId = leerLong("ID del administrador que autoriza: ");
        Utilizador u = usuarios.crearAdmin(creadorId, leerTexto("Nombre completo: "), leerTexto("Email: "),
                leerTexto("Contraseña: "), leerTexto("Documento de identificación: "));
        imprimirUsuario(u);
    }

    /** Lista los usuarios persistidos. */
    private void listarUsuarios() {
        List<Utilizador> lista = usuarios.listar();
        if (lista.isEmpty()) System.out.println("No hay usuarios registrados.");
        else lista.forEach(this::imprimirUsuario);
    }

    /** Permite buscar una cuenta por su identificador o email. */
    private void buscarUsuario() {
        System.out.println("1. Buscar por ID");
        System.out.println("2. Buscar por email");
        int tipo = leerEntero("Tipo de búsqueda: ");
        if (tipo == 1) imprimirUsuario(usuarios.obtener(leerLong("ID del usuario: ")));
        else if (tipo == 2) imprimirUsuario(usuarios.porEmail(leerTexto("Email: ")));
        else System.out.println("Tipo de búsqueda inválido.");
    }

    /** Pide los datos del anuncio y deja que el servicio valide y asigne ABERTO. */
    private void publicarObjeto() {
        System.out.println("\n--- Publicar objeto encontrado ---");
        String titulo = leerTexto("Título: ");
        String descripcion = leerTexto("Descripción: ");
        CategoriaObjeto categoria = elegirEnum(CategoriaObjeto.class, "Categoría", false);
        String cor = leerTexto("Color: ");
        ConcelhoPorto concelho = elegirEnum(ConcelhoPorto.class, "Concejo del distrito de Porto", false);
        String local = leerTexto("Lugar aproximado: ");
        LocalDate fecha = leerFecha("Fecha del hallazgo (AAAA-MM-DD): ", false);
        String contacto = leerTexto("Contacto directo: ");
        String url = leerTexto("URL de imagen (HTTP/HTTPS): ");
        Long autorId = leerLong("ID del usuario publicador: ");
        imprimirObjeto(objetos.publicar(titulo, descripcion, categoria, cor, concelho.name(), local,
                fecha, contacto, url, autorId));
    }

    /** Actualiza datos o cambia el estado de un anuncio existente. */
    private void actualizarObjeto() {
        Long id = leerLong("ID del objeto: ");
        System.out.println("1. Actualizar datos");
        System.out.println("2. Cambiar estado");
        int tipo = leerEntero("Tipo de actualización: ");
        if (tipo == 1) {
            String titulo = leerTexto("Nuevo título: ");
            String descripcion = leerTexto("Nueva descripción: ");
            CategoriaObjeto categoria = elegirEnum(CategoriaObjeto.class, "Categoría", false);
            String cor = leerTexto("Nuevo color: ");
            ConcelhoPorto concelho = elegirEnum(ConcelhoPorto.class, "Concejo", false);
            String local = leerTexto("Nuevo lugar aproximado: ");
            LocalDate fecha = leerFecha("Fecha del hallazgo (AAAA-MM-DD): ", false);
            String contacto = leerTexto("Nuevo contacto: ");
            String url = leerTexto("URL de imagen (HTTP/HTTPS): ");
            imprimirObjeto(objetos.actualizar(id, titulo, descripcion, categoria, cor, concelho.name(), local, fecha, contacto, url));
        } else if (tipo == 2) {
            EstadoObjeto estado = elegirEnum(EstadoObjeto.class, "Estado", false);
            imprimirObjeto(objetos.cambiarEstado(id, estado));
        } else {
            System.out.println("Tipo de actualización inválido.");
        }
    }

    /** Elimina un anuncio y muestra los errores de integridad devueltos por el servicio. */
    private void eliminarObjeto() {
        Long id = leerLong("ID del objeto que se eliminará: ");
        if (!confirmar("¿Confirmas la eliminación? (s/n): ")) {
            System.out.println("Eliminación cancelada.");
            return;
        }
        objetos.eliminar(id);
        System.out.println("Objeto eliminado.");
    }

    /** Consulta el catálogo abierto con filtros individuales o combinados. */
    private void buscarCatalogo() {
        System.out.println("Deja un filtro vacío para omitirlo.");
        ConcelhoPorto concelho = elegirEnum(ConcelhoPorto.class, "Concejo", true);
        CategoriaObjeto categoria = elegirEnum(CategoriaObjeto.class, "Categoría", true);
        String cor = leerTexto("Color (vacío para omitir): ").trim();
        if (cor.isEmpty()) cor = null;
        LocalDate desde = leerFecha("Fecha desde (AAAA-MM-DD, vacío para omitir): ", true);
        LocalDate hasta = leerFecha("Fecha hasta (AAAA-MM-DD, vacío para omitir): ", true);
        imprimirObjetos(objetos.catalogo(concelho, categoria, cor, desde, hasta));
    }

    /** Consulta los anuncios de un usuario concreto. */
    private void objetosPorAutor() {
        Long autorId = leerLong("ID del autor: ");
        imprimirObjetos(objetos.porAutor(autorId));
    }

    /** Busca un anuncio mediante su ID. */
    private void buscarObjetoPorId() {
        imprimirObjeto(objetos.obtener(leerLong("ID del objeto: ")));
    }

    /** Lista los anuncios de todos los estados. */
    private void listarTodosLosObjetos() {
        imprimirObjetos(objetos.todos());
    }

    /** Crea registros de demostración únicamente cuando el usuario elige esta opción. */
    private void cargarDatosDePrueba() {
        System.out.println("Se crearán cuentas y un anuncio de demostración si aún no existen.");
        if (!confirmar("¿Continuar? (s/n): ")) return;

        Utilizador admin = obtenerOCrearAdminDemo();
        Utilizador usuario = obtenerOCrearUsuarioDemo();
        boolean existeAnuncio = objetos.todos().stream().anyMatch(o -> o.getTitulo().equals("Objeto de demostración LP"));
        if (!existeAnuncio) {
            objetos.publicar("Objeto de demostración LP", "Registro creado desde la opción de datos de prueba.",
                    CategoriaObjeto.OUTROS, "Azul", ConcelhoPorto.PORTO.name(), "Porto", LocalDate.now(),
                    "demo@example.invalid", "https://example.com/imagen-demo.jpg", usuario.getId());
        }
        System.out.println("Datos de demostración listos.");
        System.out.println("ADMIN de prueba, ID " + admin.getId() + "; USER de prueba, ID " + usuario.getId() + ".");
        System.out.println("Las contraseñas de prueba son solo para este entorno académico.");
    }

    private Utilizador obtenerOCrearAdminDemo() {
        try { return usuarios.porEmail("admin.demo@upt.pt"); }
        catch (ErrorAplicacion ex) {
            if (ex.getTipo() != ErrorAplicacion.Tipo.NO_ENCONTRADO) throw ex;
            return usuarios.crearAdmin("Admin de demostración", "admin.demo@upt.pt", "AdminDemo27!", "DEMO-ADMIN-01");
        }
    }

    private Utilizador obtenerOCrearUsuarioDemo() {
        try { return usuarios.porEmail("usuario.demo@upt.pt"); }
        catch (ErrorAplicacion ex) {
            if (ex.getTipo() != ErrorAplicacion.Tipo.NO_ENCONTRADO) throw ex;
            return usuarios.registrar("Usuario de demostración", "usuario.demo@upt.pt", "UserDemo27!", "DEMO-USER-01");
        }
    }

    /** Imprime una lista o indica que no hay resultados. */
    private void imprimirObjetos(List<ObjetoAchado> lista) {
        if (lista.isEmpty()) System.out.println("La consulta no devolvió objetos.");
        else lista.forEach(this::imprimirObjeto);
    }

    private void imprimirUsuario(Utilizador u) {
        System.out.printf("Usuario #%d | %s | %s | %s | documento: %s%n",
                u.getId(), u.getNome(), u.getEmail(), u.getPerfil(), u.getDocumentoIdentificacao());
    }

    private void imprimirObjeto(ObjetoAchado o) {
        String autor = o.getPublicador() == null ? "sin autor" : o.getPublicador().getNome() + " (#" + o.getPublicador().getId() + ")";
        System.out.printf("Objeto #%d | %s | %s | %s | %s | %s | fecha: %s | autor: %s%n",
                o.getId(), o.getTitulo(), o.getCategoria(), o.getConcelho(), o.getEstado(), o.getCor(), o.getDataEncontrado(), autor);
        System.out.println("  Lugar: " + o.getLocal() + " | Imagen: " + o.getImagemUrl());
    }

    /** Lee texto completo para permitir espacios y evitar problemas de Scanner con nextInt. */
    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) throw new FinEntrada();
        return scanner.nextLine();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje).trim();
            try { return Integer.parseInt(texto); }
            catch (NumberFormatException ex) { System.out.println("Introduce un número entero válido."); }
        }
    }

    private Long leerLong(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje).trim();
            try { return Long.parseLong(texto); }
            catch (NumberFormatException ex) { System.out.println("Introduce un ID numérico válido."); }
        }
    }

    private LocalDate leerFecha(String mensaje, boolean opcional) {
        while (true) {
            String texto = leerTexto(mensaje).trim();
            if (opcional && texto.isEmpty()) return null;
            try { return LocalDate.parse(texto); }
            catch (DateTimeParseException ex) { System.out.println("Fecha inválida. Usa el formato AAAA-MM-DD."); }
        }
    }

    /** Muestra valores enum numerados y vuelve a pedir una selección inválida. */
    private <E extends Enum<E>> E elegirEnum(Class<E> tipo, String titulo, boolean opcional) {
        E[] valores = tipo.getEnumConstants();
        while (true) {
            System.out.println(titulo + (opcional ? " (vacío para omitir):" : ":"));
            for (int i = 0; i < valores.length; i++) System.out.printf(" %d. %s%n", i + 1, valores[i]);
            String entrada = leerTexto("Selección: ").trim();
            if (opcional && entrada.isEmpty()) return null;
            try {
                int indice = Integer.parseInt(entrada) - 1;
                if (indice >= 0 && indice < valores.length) return valores[indice];
            } catch (NumberFormatException ignored) { }
            System.out.println("Selección inválida. Elige uno de los números mostrados.");
        }
    }

    private boolean confirmar(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje).trim();
            if (respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("si") || respuesta.equalsIgnoreCase("sí")) return true;
            if (respuesta.equalsIgnoreCase("n") || respuesta.equalsIgnoreCase("no")) return false;
            System.out.println("Responde s o n.");
        }
    }

    private String mensajeSeguro(RuntimeException ex) {
        if (ex instanceof NoSuchElementException) return "No hay más datos de entrada.";
        return ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
    }

    private static final class FinEntrada extends RuntimeException { }
}
