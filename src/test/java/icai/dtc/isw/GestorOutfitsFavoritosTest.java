package icai.dtc.isw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GestorOutfitsFavoritosTest {

    private GestorOutfits gestor;
    private Outfit oficina;
    private Outfit fiesta;
    private Outfit deporte;

    @BeforeEach
    public void setUp() {
        gestor = new GestorOutfits();
        oficina = new Outfit(1, "Oficina", new ArrayList<>(), 7, true);
        fiesta = new Outfit(2, "Fiesta", new ArrayList<>(), 7, false);
        deporte = new Outfit(3, "Deporte", new ArrayList<>(), 7, true);
    }

    @Test
    public void filtrarFavoritosDevuelveSoloLosMarcados() {
        List<Outfit> favoritos = gestor.filtrarFavoritos(List.of(oficina, fiesta, deporte));

        assertEquals(2, favoritos.size(), "Solo hay dos outfits favoritos.");
        assertTrue(favoritos.contains(oficina));
        assertTrue(favoritos.contains(deporte));
        assertFalse(favoritos.contains(fiesta), "Un outfit no favorito no debe aparecer.");
    }

    @Test
    public void filtrarFavoritosMantieneElOrden() {
        List<Outfit> favoritos = gestor.filtrarFavoritos(List.of(deporte, fiesta, oficina));

        assertEquals(deporte, favoritos.get(0));
        assertEquals(oficina, favoritos.get(1));
    }

    @Test
    public void sinFavoritosDevuelveListaVacia() {
        oficina.setFavorito(false);
        deporte.setFavorito(false);

        assertTrue(gestor.filtrarFavoritos(List.of(oficina, fiesta, deporte)).isEmpty());
    }

    @Test
    public void listaVaciaONulaNoFalla() {
        assertTrue(gestor.filtrarFavoritos(new ArrayList<>()).isEmpty());
        assertTrue(gestor.filtrarFavoritos(null).isEmpty());
    }

    @Test
    public void cambiarFavoritoDeOutfitSinGuardarDevuelveFalse() {

        Outfit nuevo = new Outfit();

        assertFalse(gestor.cambiarFavorito(nuevo));
        assertFalse(nuevo.isFavorito(), "Si no se guarda, el outfit se queda como estaba.");
        assertFalse(gestor.cambiarFavorito(null));
    }
}