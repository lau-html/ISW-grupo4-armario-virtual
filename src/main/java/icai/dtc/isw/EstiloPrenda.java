package icai.dtc.isw;

public enum EstiloPrenda {
    CASUAL("Casual"), URBANO("Urbano"), DEPORTIVO("Deportivo"), FORMAL("Formal"),
    ELEGANTE("Elegante"), CLASICO("Clásico"), VINTAGE("Vintage"),
    MINIMALISTA("Minimalista"), OTRO("Otro");

    private final String etiqueta;
    EstiloPrenda(String etiqueta) { this.etiqueta = etiqueta; }
    public String getEtiqueta() { return etiqueta; }

    public static EstiloPrenda desdeEtiqueta(String texto) {
        if (texto == null) return OTRO;
        for (EstiloPrenda v : values()) {
            if (v.etiqueta.equalsIgnoreCase(texto.trim())) return v;
        }
        return OTRO;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}