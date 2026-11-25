package icai.dtc.isw;

public enum TipoPrenda {
    CAMISETA("Camiseta", CategoriaPrenda.SUPERIOR),
    TOP("Top", CategoriaPrenda.SUPERIOR),
    CAMISA("Camisa", CategoriaPrenda.SUPERIOR),
    JERSEY("Jersey", CategoriaPrenda.SUPERIOR),
    SUDADERA("Sudadera", CategoriaPrenda.SUPERIOR),
    PANTALON("Pantalón", CategoriaPrenda.INFERIOR),
    FALDA("Falda", CategoriaPrenda.INFERIOR),
    VESTIDO("Vestido", CategoriaPrenda.CUERPO_ENTERO),
    CHAQUETA("Chaqueta", CategoriaPrenda.ABRIGO),
    ABRIGO_CAZADORA("Abrigo/Cazadora", CategoriaPrenda.ABRIGO),
    CALZADO("Calzado", CategoriaPrenda.CALZADO),
    ACCESORIO("Accesorio", CategoriaPrenda.ACCESORIO),
    JOYERIA("Joyería", CategoriaPrenda.ACCESORIO),
    BOLSO("Bolso", CategoriaPrenda.ACCESORIO),
    OTRO("Otro", CategoriaPrenda.OTRO);

    private final String etiqueta;
    private final CategoriaPrenda categoria;

    TipoPrenda(String etiqueta, CategoriaPrenda categoria) {
        this.etiqueta = etiqueta;
        this.categoria = categoria;
    }

    public String getEtiqueta() { return etiqueta; }
    public CategoriaPrenda getCategoria() { return categoria; }

    public static TipoPrenda desdeEtiqueta(String texto) {
        if (texto == null) return OTRO;
        String t = normalizar(texto);
        for (TipoPrenda v : values()) {
            if (normalizar(v.etiqueta).equals(t)) return v;
        }
        return switch (t) {
            case "camisetas" -> CAMISETA;
            case "tops" -> TOP;
            case "camisas" -> CAMISA;
            case "jerseis", "jerseys" -> JERSEY;
            case "sudaderas" -> SUDADERA;
            case "pantalones" -> PANTALON;
            case "faldas" -> FALDA;
            case "vestidos" -> VESTIDO;
            case "chaquetas" -> CHAQUETA;
            case "abrigo", "abrigos", "cazadora", "cazadoras" -> ABRIGO_CAZADORA;
            case "zapatos", "zapatillas" -> CALZADO;
            case "accesorios" -> ACCESORIO;
            case "joyas" -> JOYERIA;
            case "bolsos" -> BOLSO;
            default -> OTRO;
        };
    }

    private static String normalizar(String s) {
        return java.text.Normalizer.normalize(s.trim(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
    }

    @Override
    public String toString() { return etiqueta; }
}