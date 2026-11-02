package icai.dtc.isw;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Prenda implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos originales
    private int id;
    private String nombre;
    private String tipo;
    private String color;
    private String descripcion;
    private String estilo;
    private String marca;
    private String temporada;

    // NUEVOS ATRIBUTOS SPRINT 2
    private String rutaFoto;
    private String correoUsuario;
    private boolean esFavorito;
    private LocalDateTime fechaCreacion;
    private List<String> categoriasPersonalizadas;

    // Constructor vacío útil para crear mocks e instanciar usando setters
    public Prenda() {
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // Constructor con campos del formulario original
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

    // Constructor corto (pruebas)
    public Prenda(String nombre, String tipo, String color) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // Constructor para tests de GestorPrendas (Lau)
    public Prenda(int id, String nombre, String tipo, String color) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // --- GETTERS Y SETTERS ORIGINALES ---
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
    public void setEstilo(String estilo) { this.estilo = estilo; }

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