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
    private TipoPrenda tipo = TipoPrenda.OTRO;
    private String color;
    private String descripcion;
    private EstiloPrenda estilo = EstiloPrenda.OTRO;
    private String marca;
    private Temporada temporada = Temporada.ATEMPORAL;

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
        this.tipo = TipoPrenda.desdeEtiqueta(tipo);
        this.color = color;
        this.descripcion = descripcion;
        this.estilo = EstiloPrenda.desdeEtiqueta(estilo);
        this.marca = marca;
        this.temporada = Temporada.desdeEtiqueta(temporada);
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // 3. Constructor para BBDD (SIN ID, lo genera SQL automáticamente)
    public Prenda(String nombre, String descripcion, String tipo, String color, String estilo, String marca, String temporada) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = TipoPrenda.desdeEtiqueta(tipo);
        this.color = color;
        this.estilo = EstiloPrenda.desdeEtiqueta(estilo); // Se añade el estilo que faltaba en la clase vieja
        this.marca = marca;
        this.temporada = Temporada.desdeEtiqueta(temporada);
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // 4. Constructor corto para pruebas antiguas
    public Prenda(String nombre, String tipo, String color) {
        this.nombre = nombre;
        this.tipo = TipoPrenda.desdeEtiqueta(tipo);
        this.color = color;
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // 5. Constructor para tests de GestorPrendas (Lau)
    public Prenda(int id, String nombre, String tipo, String color) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = TipoPrenda.desdeEtiqueta(tipo);
        this.color = color;
        this.categoriasPersonalizadas = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    // --- GETTERS Y SETTERS ORIGINALES CORREGIDOS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo.getEtiqueta(); }
    public void setTipo(String tipo) { this.tipo = TipoPrenda.desdeEtiqueta(tipo); }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstilo() { return estilo.getEtiqueta(); }
    public void setEstilo(String estilo) { this.estilo = EstiloPrenda.desdeEtiqueta(estilo); }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getTemporada() { return temporada.getEtiqueta(); }
    public void setTemporada(String temporada) { this.temporada = Temporada.desdeEtiqueta(temporada); }

    public TipoPrenda getTipoPrenda() { return tipo; }
    public void setTipoPrenda(TipoPrenda tipo) { this.tipo = tipo != null ? tipo : TipoPrenda.OTRO; }

    public EstiloPrenda getEstiloPrenda() { return estilo; }
    public void setEstiloPrenda(EstiloPrenda estilo) { this.estilo = estilo != null ? estilo : EstiloPrenda.OTRO; }

    public Temporada getTemporadaPrenda() { return temporada; }
    public void setTemporadaPrenda(Temporada temporada) { this.temporada = temporada != null ? temporada : Temporada.ATEMPORAL; }

    public CategoriaPrenda getCategoria() { return tipo.getCategoria(); }

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