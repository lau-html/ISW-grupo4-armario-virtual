package icai.dtc.isw;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Prenda implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // --- ATRIBUTOS ORIGINALES ---
    private int id;
    private String nombre;
    private String tipo;
    private String color;
    private String descripcion;
    private String estilo;
    private String marca;
    private String temporada;

    // --- NUEVOS ATRIBUTOS SPRINT 2 ---
    private String rutaFoto;
    private String correoUsuario;
    private boolean esFavorito;
    private LocalDateTime fechaCreacion;
    private List<String> categoriasPersonalizadas;

    // 1. Constructor vacío (útil para crear mocks e instanciar usando setters)
    public Prenda() {
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // 2. Constructor para recuperar los datos de la base de datos o de formularios antiguos (INCLUYE ID)
    public Prenda(int id, String nombre, String tipo, String color, String descripcion, String estilo, String marca, String temporada) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
        this.descripcion = descripcion;
        this.estilo = estilo;
        this.marca = marca;
        this.temporada = temporada;
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // 3. ¡RESCATADO Y ACTUALIZADO! Constructor para BBDD (SIN ID, lo genera SQL automáticamente)
    public Prenda(String nombre, String descripcion, String tipo, String color, String estilo, String marca, String temporada) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.color = color;
        this.estilo = estilo; // Se añade el estilo que faltaba en la clase vieja
        this.marca = marca;
        this.temporada = temporada;
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // 4. Constructor corto para pruebas antiguas
    public Prenda(String nombre, String tipo, String color) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // 5. Constructor para tests de GestorPrendas (Lau)
    public Prenda(int id, String nombre, String tipo, String color) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // --- GETTERS Y SETTERS ORIGINALES CORREGIDOS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstilo() { return estilo; }
    public void setEstilo(String estilo) { this.estilo = estilo; } // Corregido el bug de la clase vieja

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getTemporada() { return temporada; }
    public void setTemporada(String temporada) { this.temporada = temporada; }

    // --- GETTERS Y SETTERS NUEVOS (SPRINT 2) ---
    public String getRutaFoto() { return rutaFoto; }
    public void setRutaFoto(String rutaFoto) { this.rutaFoto = rutaFoto; }

    public String getCorreoUsuario() { return correoUsuario; }
    public void setCorreoUsuario(String correoUsuario) { this.correoUsuario = correoUsuario; }

    public boolean isEsFavorito() { return esFavorito; }
    public void setEsFavorito(boolean esFavorito) { this.esFavorito = esFavorito; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<String> getCategoriasPersonalizadas() { return categoriasPersonalizadas; }
    public void setCategoriasPersonalizadas(List<String> categoriasPersonalizadas) { this.categoriasPersonalizadas = categoriasPersonalizadas; }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ", " + color + " " + estilo + ", " + marca + ", " + temporada + ", " + descripcion + ")";
    }
}