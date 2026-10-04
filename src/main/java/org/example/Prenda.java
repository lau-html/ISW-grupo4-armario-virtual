package org.example;

public class Prenda {

    private String nombre;
    private String tipo; // tipo de ropa
    private String color;

    public Prenda (String nombre, String tipo, String color) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
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

    @Override
    public String toString() {
        return nombre + " (" + tipo + ", " + color + ")";
    }
    
}
