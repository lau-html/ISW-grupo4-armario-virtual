package icai.dtc.isw.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Outfit implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private List<Prenda> prendas;

    public Outfit() {}

    public Outfit(String nombre, List<Prenda> prendas) {
        this.nombre = nombre;
        this.prendas = prendas;
    }

    public Outfit(int id, String nombre, List<Prenda> prendas) {
        this.id = id;
        this.nombre = nombre;
        this.prendas = prendas;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Prenda> getPrendas() { return prendas; }
    public void setPrendas(List<Prenda> prendas) { this.prendas = prendas; }
}