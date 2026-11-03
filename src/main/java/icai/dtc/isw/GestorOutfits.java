package icai.dtc.isw;

import icai.dtc.isw.dao.OutfitDAO;
import icai.dtc.isw.domain.Outfit;
import icai.dtc.isw.domain.Prenda;
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
}