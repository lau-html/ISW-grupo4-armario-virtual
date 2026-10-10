package icai.dtc.isw;

import java.io.Serializable;

public class Prenda implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos de la prenda: de momento id + los del formulario, se podrian añadir mas en el futuro
    private int id;
    private String nombre;
    private String tipo; // tipo de ropa (falda, pantalón, camiseta, etc.)
    private String color;
    private String descripcion;
    private String estilo;
    private String marca;
    private String temporada;

    // Constructor con campos que estan en el formulario
    public Prenda(int id, String nombre, String tipo, String color, String descripcion, String estilo, String marca, String temporada) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
        this.descripcion = descripcion;
        this.estilo = estilo;
        this.marca = marca;
        this.temporada = temporada;
    }

    // Constructor corto por si solo queremos crear una prenda con nombre, tipo y color (de momento para pruebas)
    public Prenda(String nombre, String tipo, String color) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ", " + color + " " + estilo + ", " + marca + ", " + temporada + ", " + descripcion + ")";
    }
}