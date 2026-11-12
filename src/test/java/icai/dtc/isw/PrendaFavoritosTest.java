package icai.dtc.isw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrendaFavoritosTest {

    private Prenda prenda;

    // Se ejecuta antes de cada test para tener una prenda limpia
    @BeforeEach
    public void setUp() {
        prenda = new Prenda("Jersey de lana", "Parte de arriba", "Blanco");
    }

    @Test
    public void testMarcarComoFavorito() {
        // Simulamos la lógica que ocurre al pulsar el botón del corazón para marcar
        prenda.setEsFavorito(true);
        if (!prenda.getCategoriasPersonalizadas().contains("FAVORITOS")) {
            prenda.getCategoriasPersonalizadas().add("FAVORITOS");
        }

        // Verificamos que los cambios se han guardado correctamente
        assertTrue(prenda.isEsFavorito(), "La prenda debería estar marcada como favorita (true).");
        assertTrue(prenda.getCategoriasPersonalizadas().contains("FAVORITOS"), "La lista de categorías debe contener 'FAVORITOS'.");
    }

    @Test
    public void testDesmarcarComoFavorito() {
        // Preparamos una prenda que ya es favorita
        prenda.setEsFavorito(true);
        prenda.getCategoriasPersonalizadas().add("FAVORITOS");

        // Simulamos la lógica al volver a pulsar el botón para desmarcar
        prenda.setEsFavorito(false);
        prenda.getCategoriasPersonalizadas().remove("FAVORITOS");

        // Verificamos que se ha limpiado el estado
        assertFalse(prenda.isEsFavorito(), "La prenda ya no debería estar marcada como favorita (false).");
        assertFalse(prenda.getCategoriasPersonalizadas().contains("FAVORITOS"), "La lista de categorías ya no debe contener 'FAVORITOS'.");
    }
}