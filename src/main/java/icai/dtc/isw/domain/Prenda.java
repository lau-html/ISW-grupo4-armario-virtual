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
    private String tipo;
    private String color;
    private String marca;
    private String temporada;

    // Constructor vacío por defecto
    public Prenda() {}

    // Constructor para registrar una nueva prenda (sin ID, ya que la base de datos lo genera automáticamente)
    public Prenda(String nombre, String descripcion, String tipo, String color, String marca, String temporada) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.color = color;
        this.marca = marca;
        this.temporada = temporada;
    }

    // Constructor para recuperar los datos de la base de datos (incluye el ID)
    public Prenda(int id, String nombre, String descripcion, String tipo, String color, String marca, String temporada) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
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

    public String getTipo() { return tipo; }
    public void setEstilo(String tipo) { this.tipo = tipo; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getTemporada() { return temporada; }
    public void setTemporada(String temporada) { this.temporada = temporada; }

  @Override
  public String toString() {
      String infoExtra = "";
      if (tipo != null && !tipo.isEmpty()) infoExtra += " | Estilo: " + tipo;
      if (marca != null && !marca.isEmpty()) infoExtra += " | Marca: " + marca;
      if (temporada != null && !temporada.isEmpty()) infoExtra += " | Temp: " + temporada;
      if (descripcion != null && !descripcion.isEmpty()) infoExtra += " (" + descripcion + ")";

      return nombre + "  —  [" + tipo + "]  |  Color: " + color + infoExtra;
  }
}