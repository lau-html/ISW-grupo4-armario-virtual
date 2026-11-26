package icai.dtc.isw;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class EdicionPrendaTest {

    private Prenda prendaOriginal() {
        return new Prenda(1, "Camiseta", "Camiseta basica", "Camiseta", "Blanco", "Casual", "Nike", "Verano");
    }

    @Test
    void aplicarCambiosModificaTodosLosAtributos() {
        Prenda original = prendaOriginal();
        GestorPrendas gestor = new GestorPrendas();

        Prenda cambios = new Prenda();
        cambios.setId(1);
        cambios.setNombre("Jersey de lana");
        cambios.setDescripcion("Jersey grueso");
        cambios.setTipoPrenda(TipoPrenda.JERSEY);
        cambios.setEstiloPrenda(EstiloPrenda.CLASICO);
        cambios.setColor("Verde");
        cambios.setMarca("Zara");
        cambios.setTemporadaPrenda(Temporada.INVIERNO);

        assertTrue(gestor.aplicarCambios(List.of(original), cambios));
        assertEquals("Jersey de lana", original.getNombre());
        assertEquals("Jersey grueso", original.getDescripcion());
        assertEquals(TipoPrenda.JERSEY, original.getTipoPrenda());
        assertEquals(EstiloPrenda.CLASICO, original.getEstiloPrenda());
        assertEquals("Verde", original.getColor());
        assertEquals("Zara", original.getMarca());
        assertEquals(Temporada.INVIERNO, original.getTemporadaPrenda());
    }

    @Test
    void aplicarCambiosConIdInexistenteNoModificaNada() {
        Prenda original = prendaOriginal();
        GestorPrendas gestor = new GestorPrendas();

        Prenda cambios = new Prenda();
        cambios.setId(99);
        cambios.setNombre("Otro");

        assertFalse(gestor.aplicarCambios(List.of(original), cambios));
        assertEquals("Camiseta", original.getNombre());
    }

    @Test
    void aplicarCambiosNoTocaLasCategoriasPersonalizadas() {
        Prenda original = prendaOriginal();
        original.setCategoriasPersonalizadas(new java.util.ArrayList<>(List.of("Para salir")));
        GestorPrendas gestor = new GestorPrendas();

        Prenda cambios = new Prenda();
        cambios.setId(1);
        cambios.setNombre("Camiseta nueva");
        cambios.setColor("Rojo");

        gestor.aplicarCambios(List.of(original), cambios);
        assertEquals(List.of("Para salir"), original.getCategoriasPersonalizadas());
    }
}
