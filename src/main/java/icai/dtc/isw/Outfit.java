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
    private int usuarioId;
    private boolean favorito;

    // Constructores
    public Outfit() {
        this.prendas = new ArrayList<>();
        this.favorito = false;
    }

    public Outfit(String nombre, List<Prenda> prendas, int usuarioId, boolean favorito) {
        this.nombre = nombre;
        this.prendas = prendas != null ? prendas : new ArrayList<>();
        this.usuarioId = usuarioId;
        this.favorito = favorito;
    }

    public Outfit(int id, String nombre, List<Prenda> prendas, int usuarioId, boolean favorito) {
        this.id = id;
        this.nombre = nombre;
        this.prendas = prendas != null ? prendas : new ArrayList<>();
        this.usuarioId = usuarioId;
        this.favorito = favorito;
    }

    // Constructor antiguo mantenido por compatibilidad si algún test o clase lo usa
    public Outfit(int id, String nombre, List<Prenda> prendas) {
        this(id, nombre, prendas, 0, false);
    }

    // Getters y Setters
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

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    @Override
    public String toString() {
        return "👗 " + nombre + " (" + prendas.size() + " prendas)";
    }
}