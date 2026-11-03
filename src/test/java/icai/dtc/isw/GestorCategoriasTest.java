package icai.dtc.isw;

import java.util.List;
import java.util.Map;
import icai.dtc.isw.domain.Prenda;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


class GestorCategoriasTest {

    @org.junit.jupiter.api.Test
    void categorizarPorTipo() {

        // preparo un armario de prueba
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camiseta basica", "Camisetas", "Blanco", "Nike", "Verano"),
                new Prenda("Vaqueros", "Vaqueros rectos", "Pantalones", "Azul", "Levis", "Atemporal"),
                new Prenda("Pantalón", "Pantalon de vestir", "Pantalones", "Negro", "Zara", "Invierno")
        );

        // llamo al método
        Map<String, List<Prenda>> grupos = gestor.categorizarPorTipo(armario);

        // compruebo agrupacion OK
        assertEquals(2, grupos.size());
        assertEquals(2, grupos.get("Pantalones").size());
        assertEquals(1, grupos.get("Camisetas").size());

    }

    @org.junit.jupiter.api.Test
    void prendasPorTipo() {

        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camiseta basica", "Camisetas", "Blanco", "Nike", "Verano"),
                new Prenda("Vaqueros", "Vaqueros rectos", "Pantalones", "Azul", "Levis", "Atemporal"),
                new Prenda("Pantalón", "Pantalon de vestir", "Pantalones", "Negro", "Zara", "Invierno")
        );

        List<Prenda> pantalones = gestor.prendasPorTipo(armario, "Pantalones");

        assertEquals(2, pantalones.size());
        assertTrue(pantalones.stream().allMatch(p -> p.getTipo().equals("Pantalones")));

    }

    @Test
    void filtrarPorMarca() {
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda(1, "Camiseta Nike", "Camiseta de manga corta", "Camisetas", "Blanco", "Nike", "Verano"),
                new Prenda(2, "Abrigo", "Abrigo largo", "Abrigos", "Beige", "Zara", "Invierno")
        );


        List<Prenda> resultado = gestor.filtrarPor(armario, Prenda::getMarca, "Zara");

        assertEquals(1, resultado.size());
        assertEquals("Abrigo", resultado.get(0).getNombre());
    }

    @Test //testeo por si no existr una categoria te devuelva una lista vacia, no null
    void categoriaInexistenteDevuelveVacio() {
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camiseta basica", "Camisetas", "Blanco", "Nike", "Verano")
        );
        List<Prenda> bufandas = gestor.prendasPorTipo(armario, "Bufandas");
        assertNotNull(bufandas);
        assertTrue(bufandas.isEmpty());
    }

}