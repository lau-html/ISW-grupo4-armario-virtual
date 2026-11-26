package icai.dtc.isw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// pruebas de las prendas que no estan en ningun outfit
class GestorUsoPrendasTest {

    private final GestorUsoPrendas gestor = new GestorUsoPrendas();

    private Prenda prenda(int id, String tipo) {
        return new Prenda(id, "Prenda " + id, tipo, "Negro", "", "Casual", "Zara", "Verano");
    }

    @Test
    void sinOutfitsTodasEstanSinUsar() {
        List<Prenda> armario = List.of(prenda(1, "Camiseta"), prenda(2, "Pantalón"));
        assertEquals(2, gestor.prendasSinUsar(armario, new ArrayList<>()).size());
    }

    @Test
    void lasPrendasDeUnOutfitNoSalenComoSinUsar() {
        Prenda camiseta = prenda(1, "Camiseta");
        Prenda pantalon = prenda(2, "Pantalón");
        Prenda bolso = prenda(3, "Bolso");
        Outfit outfit = new Outfit(10, "Lunes", List.of(camiseta, pantalon));

        List<Prenda> sinUsar = gestor.prendasSinUsar(List.of(camiseta, pantalon, bolso), List.of(outfit));

        assertEquals(1, sinUsar.size());
        assertEquals(3, sinUsar.get(0).getId());
        assertTrue(gestor.estaSinUsar(bolso, List.of(outfit)));
        assertFalse(gestor.estaSinUsar(camiseta, List.of(outfit)));
    }

    @Test
    void comparaPorIdAunqueSeanObjetosDistintos() {
        // el outfit cargado de la BD puede tener otra copia de la misma prenda
        Outfit outfit = new Outfit(10, "Copia", List.of(prenda(1, "Camiseta")));
        assertFalse(gestor.estaSinUsar(prenda(1, "Camiseta"), List.of(outfit)));
    }

    @Test
    void listasNulasNoDanError() {
        assertTrue(gestor.prendasSinUsar(null, null).isEmpty());
        assertTrue(gestor.estaSinUsar(prenda(1, "Camiseta"), null));
    }
}
