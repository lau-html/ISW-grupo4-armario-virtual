package icai.dtc.isw;

import icai.dtc.isw.dao.OutfitDAO;
import java.util.List;

public class GestorOutfits {

    public boolean guardarNuevoOutfit(String nombre, List<Prenda> prendas) {
        if (prendas == null || prendas.isEmpty()) {
            return false;
        }
        Outfit nuevoOutfit = new Outfit(nombre, prendas);
        OutfitDAO dao = new OutfitDAO();
        return dao.registrarOutfit(nuevoOutfit);
    }

    public boolean anadirAccesorioAOutfit(String nombreOutfit, int idPrenda) {
        OutfitDAO dao = new OutfitDAO();
        return dao.anadirPrendaAOutfit(nombreOutfit, idPrenda);
    }

    public List<Outfit> cargarOutfits(List<Prenda> todasLasPrendas) {
        OutfitDAO dao = new OutfitDAO();
        return dao.obtenerTodosLosOutfits(todasLasPrendas);
    }

    public boolean eliminarOutfit(String nombreOutfit) {
        OutfitDAO dao = new OutfitDAO();
        return dao.eliminarOutfit(nombreOutfit);
    }
}