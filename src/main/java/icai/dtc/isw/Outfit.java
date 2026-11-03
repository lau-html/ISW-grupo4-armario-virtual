package icai.dtc.isw; // Ajusta el paquete según tu proyecto

public class Outfit {
    private String nombre;
    private String parteArriba;
    private String parteAbajo;
    private boolean esFavorito;

    public Outfit(String nombre, String parteArriba, String parteAbajo) {
        this.nombre = nombre;
        this.parteArriba = parteArriba;
        this.parteAbajo = parteAbajo;
        this.esFavorito = false; // Por defecto no es favorito
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getParteArriba() { return parteArriba; }
    public String getParteAbajo() { return parteAbajo; }

    public boolean isFavorito() { return esFavorito; }
    public void setFavorito(boolean favorito) { this.esFavorito = favorito; }

    @Override
    public String toString() {
        return nombre + " (" + parteArriba + " + " + parteAbajo + ")";
    }
}