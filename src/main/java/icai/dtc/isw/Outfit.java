package icai.dtc.isw;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Outfit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private List<Prenda> prendas;

    // Atributos añadidos del Sprint 2
    private boolean esFavorito;

    // Constructores
    public Outfit() {
        this.prendas = new ArrayList<>();
        this.esFavorito = false;
    }

    public Outfit(String nombre, List<Prenda> prendas) {
        this.nombre = nombre;
        this.prendas = prendas != null ? prendas : new ArrayList<>();
        this.esFavorito = false;
    }

    public Outfit(int id, String nombre, List<Prenda> prendas) {
        this.id = id;
        this.nombre = nombre;
        this.prendas = prendas != null ? prendas : new ArrayList<>();
        this.esFavorito = false;
    }

    // Getters y Setters principales
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

    public List<Prenda> getPrendas() {
        return prendas;
    }

    public void setPrendas(List<Prenda> prendas) {
        this.prendas = prendas;
    }

    // Getters y Setters de favoritos
    public boolean isFavorito() {
        return esFavorito;
    }

    public void setFavorito(boolean favorito) {
        this.esFavorito = favorito;
    }

    @Override
    public String toString() {
        return "👗 " + nombre + " (" + prendas.size() + " prendas)";
    }
}