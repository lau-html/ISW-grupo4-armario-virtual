package icai.dtc.isw;
import java.io.Serializable;

public class Prenda implements Serializable{

    private static final long serialVersionUID=1L;
    private String nombre;
    private String tipo; // tipo de ropa
    private String color;
    private int id;

    public Prenda (String nombre, String tipo, String color) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
    }

    public Prenda (int id, String nombre, String tipo, String color) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
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

    @Override
    public String toString() {
        return nombre + " (" + tipo + ", " + color + ")";
    }
    
}
