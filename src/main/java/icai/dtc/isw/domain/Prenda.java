package icai.dtc.isw.domain;

import java.io.Serial;
import java.io.Serializable;

public class Prenda implements Serializable {
    
    // Serializable permite convertir una instancia de Prenda en un flujo de bytes
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String descripcion;
    private String estilo;
    private String color;
    private String marca;
    private String temporada;

    // Constructor vacío por defecto
    public Prenda() {}

    // Constructor para registrar una nueva prenda (sin ID, ya que la base de datos lo genera automáticamente)
    public Prenda(String nombre, String descripcion, String estilo, String color, String marca, String temporada) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estilo = estilo;
        this.color = color;
        this.marca = marca;
        this.temporada = temporada;
    }

    // Constructor para recuperar los datos de la base de datos (incluye el ID)
    public Prenda(int id, String nombre, String descripcion, String estilo, String color, String marca, String temporada) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estilo = estilo;
        this.color = color;
        this.marca = marca;
        this.temporada = temporada;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstilo() { return estilo; }
    public void setEstilo(String estilo) { this.estilo = estilo; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getTemporada() { return temporada; }
    public void setTemporada(String temporada) { this.temporada = temporada; }
}