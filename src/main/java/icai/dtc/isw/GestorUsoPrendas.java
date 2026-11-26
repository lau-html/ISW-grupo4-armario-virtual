package icai.dtc.isw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// dice que prendas del armario no aparecen en ningun outfit guardado,
// para poder marcarlas en la interfaz como "sin usar"
public class GestorUsoPrendas {

    // devuelve las prendas del armario que no estan en ningun outfit
    public List<Prenda> prendasSinUsar(List<Prenda> armario, List<Outfit> outfits) {
        Set<Integer> idsUsados = idsEnOutfits(outfits);
        List<Prenda> sinUsar = new ArrayList<>();
        if (armario == null) {
            return sinUsar;
        }
        for (Prenda prenda : armario) {
            if (!idsUsados.contains(prenda.getId())) {
                sinUsar.add(prenda);
            }
        }
        return sinUsar;
    }

    // true si la prenda no esta en ningun outfit (para comprobar una sola prenda, por ejemplo al pintar su tarjeta)
    public boolean estaSinUsar(Prenda prenda, List<Outfit> outfits) {
        return prenda != null && !idsEnOutfits(outfits).contains(prenda.getId());
    }

    // junta los ids de todas las prendas que aparecen en algun outfit (se compara por id porque las prendas de un outfit y las del armario pueden ser objetos distintos)
    private Set<Integer> idsEnOutfits(List<Outfit> outfits) {
        Set<Integer> ids = new HashSet<>();
        if (outfits == null) {
            return ids;
        }
        for (Outfit outfit : outfits) {
            if (outfit.getPrendas() == null) {
                continue;
            }
            for (Prenda prenda : outfit.getPrendas()) {
                ids.add(prenda.getId());
            }
        }
        return ids;
    }
}