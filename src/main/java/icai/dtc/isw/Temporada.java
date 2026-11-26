package icai.dtc.isw;

public enum Temporada {
    PRIMAVERA("Primavera"), VERANO("Verano"), OTONO("Otoño"),
    INVIERNO("Invierno"), ATEMPORAL("Atemporal");

    private final String etiqueta;
    Temporada(String etiqueta) { this.etiqueta = etiqueta; }
    public String getEtiqueta() { return etiqueta; }

    public static Temporada desdeEtiqueta(String texto) {
        if (texto == null) return ATEMPORAL;
        for (Temporada v : values()) {
            if (v.etiqueta.equalsIgnoreCase(texto.trim())) return v;
        }
        return ATEMPORAL;
    }

    @Override
    public String toString() { return etiqueta; }
}