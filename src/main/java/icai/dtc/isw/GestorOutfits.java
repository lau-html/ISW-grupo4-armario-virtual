package icai.dtc.isw;

import icai.dtc.isw.dao.OutfitDAO;
import java.util.ArrayList;
import java.util.List;

public class GestorOutfits {

    private final OutfitDAO dao = new OutfitDAO();

    // Carga solo los outfits del usuario logueado
    public List<Outfit> cargarOutfitsUsuario(int usuarioId, List<Prenda> todasLasPrendas) {
        return dao.obtenerOutfitsPorUsuario(usuarioId, todasLasPrendas);
    }

    // Elimina un outfit por su ID
    public boolean eliminarOutfit(int outfitId) {
        return dao.eliminarOutfit(outfitId);
    }

    public boolean anadirAccesorioAOutfit(String nombreOutfit, int idPrenda) {
        return dao.anadirPrendaAOutfit(nombreOutfit, idPrenda);
    }

    // Crea o edita un outfit de forma flexible y validando categorías
    public boolean validarYGuardarOutfit(Outfit outfit, Prenda superior, Prenda inferior, Prenda calzado, List<Prenda> accesorios) {
        // 1. Validar que haya al menos una prenda (sin obligar a superior o inferior)
        if (superior == null && inferior == null && calzado == null && (accesorios == null || accesorios.isEmpty())) {
            return false;
        }

        // 2. Validar categorías y construir la lista final de prendas
        List<Prenda> prendasFinales = new ArrayList<>();

        if (superior != null) {
            if (!esDeCategoria(superior, "SUPERIOR")) {
                throw new IllegalArgumentException("La prenda superior no pertenece a la categoría correcta.");
            }
            prendasFinales.add(superior);
        }

        if (inferior != null) {
            if (!esDeCategoria(inferior, "INFERIOR")) {
                throw new IllegalArgumentException("Una falda o pantalón debe ir en prenda inferior.");
            }
            prendasFinales.add(inferior);
        }

        if (calzado != null) {
            if (!esDeCategoria(calzado, "CALZADO")) {
                throw new IllegalArgumentException("Una camiseta no puede asignarse como calzado.");
            }
            prendasFinales.add(calzado);
        }

        if (accesorios != null) {
            for (Prenda acc : accesorios) {
                if (!esDeCategoria(acc, "ACCESORIO")) {
                    throw new IllegalArgumentException("Una prenda normal no puede asignarse como accesorio.");
                }
                prendasFinales.add(acc);
            }
        }

        outfit.setPrendas(prendasFinales);

        // 3. Si el outfit tiene ID > 0 es una edición; si es 0, es creación nueva
        if (outfit.getId() > 0) {
            return dao.actualizarOutfit(outfit);
        } else {
            return dao.registrarOutfit(outfit);
        }
    }

    // SPRINT 3 (Cristina): devuelve solo los outfits marcados como favoritos, en el mismo orden.
    // No usa la base de datos, trabaja con la lista que ya está cargada.
    public List<Outfit> filtrarFavoritos(List<Outfit> outfits) {
        List<Outfit> favoritos = new ArrayList<>();
        if (outfits == null) {
            return favoritos;
        }
        for (Outfit o : outfits) {
            if (o != null && o.isFavorito()) {
                favoritos.add(o);
            }
        }
        return favoritos;
    }


    public boolean cambiarFavorito(Outfit outfit) {
        if (outfit == null || outfit.getId() <= 0) {
            return false;
        }
        boolean nuevoValor = !outfit.isFavorito();
        if (dao.actualizarFavorito(outfit.getId(), outfit.getUsuarioId(), nuevoValor)) {
            outfit.setFavorito(nuevoValor);
            return true;
        }
        return false;
    }

    // Método auxiliar para comprobar el tipo/categoría de la prenda
    private boolean esDeCategoria(Prenda prenda, String categoriaEsperada) {
        String tipo = prenda.getTipo();
        if (tipo == null) return false;

        String t = tipo.trim().toLowerCase();
        switch (categoriaEsperada.toUpperCase()) {
            case "SUPERIOR":
                return t.contains("arriba") || t.contains("superior") ||
                        t.equals("camiseta") || t.equals("camisa") || t.equals("jersey") ||
                        t.equals("sudadera") || t.equals("top") || t.equals("chaqueta") || t.equals("abrigo");
            case "INFERIOR":
                return t.contains("abajo") || t.contains("inferior") ||
                        t.equals("pantalón") || t.equals("pantalon") || t.equals("falda") ||
                        t.equals("vaqueros") || t.equals("short") || t.equals("bermudas");
            case "CALZADO":
                return t.contains("calzado") ||
                        t.equals("zapatos") || t.equals("zapatillas") || t.equals("botas") ||
                        t.equals("sandalias") || t.equals("tacones");
            case "ACCESORIO":
                return t.contains("accesorio") ||
                        t.equals("bolso") || t.equals("cinturón") || t.equals("cinturon") ||
                        t.equals("gorra") || t.equals("sombrero") || t.equals("bufanda") ||
                        t.equals("gafas") || t.equals("joyería") || t.equals("joyeria");
            default:
                return false;
        }

    }
}