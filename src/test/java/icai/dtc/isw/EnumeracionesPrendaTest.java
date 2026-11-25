package icai.dtc.isw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumeracionesPrendaTest {

    @Test
    void etiquetaConocidaIgnoraMayusculasYTildes() {
        assertEquals(TipoPrenda.PANTALON, TipoPrenda.desdeEtiqueta("pantalón"));
        assertEquals(TipoPrenda.PANTALON, TipoPrenda.desdeEtiqueta("PANTALON"));
    }

    @Test
    void pluralesAntiguosSeReconocen() {
        assertEquals(TipoPrenda.CAMISETA, TipoPrenda.desdeEtiqueta("Camisetas"));
        assertEquals(TipoPrenda.PANTALON, TipoPrenda.desdeEtiqueta("Pantalones"));
    }

    @Test
    void etiquetaDesconocidaONulaDaValorPorDefecto() {
        assertEquals(TipoPrenda.OTRO, TipoPrenda.desdeEtiqueta("zapatillas raras"));
        assertEquals(TipoPrenda.OTRO, TipoPrenda.desdeEtiqueta(null));
        assertEquals(EstiloPrenda.OTRO, EstiloPrenda.desdeEtiqueta(null));
        assertEquals(Temporada.ATEMPORAL, Temporada.desdeEtiqueta("nunca"));
    }

    @Test
    void categoriaDelTipo() {
        assertEquals(CategoriaPrenda.INFERIOR, TipoPrenda.FALDA.getCategoria());
        assertEquals(CategoriaPrenda.ACCESORIO, TipoPrenda.BOLSO.getCategoria());
        assertEquals(CategoriaPrenda.CALZADO, TipoPrenda.CALZADO.getCategoria());
    }

    @Test
    void prendaGuardaEnumeracionesPeroDevuelveTexto() {
        Prenda p = new Prenda("Falda", "Falda larga", "Falda", "Negro", "Elegante", "Zara", "Otoño");
        assertEquals(TipoPrenda.FALDA, p.getTipoPrenda());
        assertEquals("Falda", p.getTipo());
        assertEquals(EstiloPrenda.ELEGANTE, p.getEstiloPrenda());
        assertEquals("Otoño", p.getTemporada());
        assertEquals(CategoriaPrenda.INFERIOR, p.getCategoria());
    }

    @Test
    void prendaSinDatosTieneValoresPorDefecto() {
        Prenda p = new Prenda();
        assertEquals("Otro", p.getTipo());
        assertEquals("Otro", p.getEstilo());
        assertEquals("Atemporal", p.getTemporada());
    }
}
