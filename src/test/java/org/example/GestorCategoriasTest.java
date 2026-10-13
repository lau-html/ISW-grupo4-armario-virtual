package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;


class GestorCategoriasTest {

    @org.junit.jupiter.api.Test
    void categorizarPorTipo() {

        // preparo un armario de prueba
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camisetas", "Blanco"),
                new Prenda("Vaqueros", "Pantalones", "Azul"),
                new Prenda("Pantalón", "Pantalones", "Negro")
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
                new Prenda("Camiseta", "Camisetas", "Blanco"),
                new Prenda("Vaqueros", "Pantalones", "Azul"),
                new Prenda("Pantalón", "Pantalones", "Negro")
        );

        List<Prenda> pantalones = gestor.prendasPorTipo(armario, "Pantalones");

        assertEquals(2, pantalones.size());
        assertTrue(pantalones.stream().allMatch(p -> p.getTipo().equals("Pantalones")));

    }

    @Test //testeo por si no existr una categoria te devuelva una lista vacia, no null
    void categoriaInexistenteDevuelveVacio() {
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camisetas", "Blanco")
        );
        List<Prenda> bufandas = gestor.prendasPorTipo(armario, "Bufandas");
        assertNotNull(bufandas);
        assertTrue(bufandas.isEmpty());
    }

}