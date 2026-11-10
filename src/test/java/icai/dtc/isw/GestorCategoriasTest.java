package icai.dtc.isw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class GestorCategoriasTest {


    // cambio el test de prendasPorTipo a filtrarPor, que es el metodo que se queda (porque lo filtra todo incluido tipo y no quiero metodos innecesarios)
    @Test
    void filtrarPorTipo() {
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camiseta basica", "Camisetas", "Blanco", "Casual", "Nike", "Verano"),
                new Prenda("Vaqueros", "Vaqueros rectos", "Pantalones", "Azul", "Casual", "Levis", "Atemporal"),
                new Prenda("Pantalón", "Pantalon de vestir", "Pantalones", "Negro", "Elegante", "Zara", "Invierno")
        );
        List<Prenda> pantalones = gestor.filtrarPor(armario, Prenda::getTipo, "Pantalones");
        assertEquals(2, pantalones.size());
        assertTrue(pantalones.stream().allMatch(p -> p.getTipo().equals("Pantalones")));
    }

    @Test
    void filtrarPorMarca() {
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda(1, "Camiseta Nike", "Camisetas", "Blanco", "Camiseta de manga corta", "Casual", "Nike", "Verano"),
                new Prenda(2, "Abrigo", "Abrigos", "Beige", "Abrigo largo", "Formal", "Zara", "Invierno")
        );
        List<Prenda> resultado = gestor.filtrarPor(armario, Prenda::getMarca, "Zara");
        assertEquals(1, resultado.size());
        assertEquals("Abrigo", resultado.get(0).getNombre());
    }
    // cambio el de categoria inexistente a filtrarPor, que es el metodo que se queda
    @Test //testeo por si no existr una categoria te devuelva una lista vacia, no null
    void categoriaInexistenteDevuelveVacio() {
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camiseta basica", "Camisetas", "Blanco", "Casual", "Nike", "Verano")
        );
        List<Prenda> bufandas = gestor.filtrarPor(armario, Prenda::getTipo, "Bufandas");
        assertNotNull(bufandas);
        assertTrue(bufandas.isEmpty());
    }

    @Test
    void filtrarCombinadoTipoYMarca() {
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camiseta basica", "Camisetas", "Blanco", "Casual", "Nike", "Verano"), // cumple tipo, no marca
                new Prenda("Pantalón", "Pantalon de vestir", "Pantalones", "Negro", "Elegante", "Zara", "Invierno"), // cumple marca, no tipo
                new Prenda("Top", "Top de tirantes", "Camisetas", "Rojo", "Casual", "Zara", "Verano") // cumple los dos
        );
        Map<String, String> filtros = Map.of("tipo", "Camisetas", "marca", "Zara");
        List<Prenda> resultado = gestor.filtrarCombinado(armario, filtros);
        assertEquals(1, resultado.size());
        assertEquals("Top", resultado.get(0).getNombre());
    }

    @Test
    void filtrarCombinadoCategoriaPersonalizadaYAtributo() {
        GestorCategorias gestor = new GestorCategorias();

        // preparo prendas que cumplen una parte, las dos o ninguna, a posta
        Prenda vestido = new Prenda("Vestido", "Vestido largo", "Vestidos", "Rojo", "Elegante", "Zara", "Verano");
        vestido.setCategoriasPersonalizadas(new ArrayList<>(List.of("Para salir"))); // categoria SI, marca SI

        Prenda camiseta = new Prenda("Camiseta", "Camiseta basica", "Camisetas", "Blanco", "Casual", "Nike", "Verano");
        camiseta.setCategoriasPersonalizadas(new ArrayList<>(List.of("Para salir"))); // categoria SI, marca NO

        Prenda blazer = new Prenda("Blazer", "Blazer entallado", "Abrigos", "Negro", "Elegante", "Zara", "Invierno"); // categoria NO, marca SI

        Prenda cinturon = new Prenda("Cinturón", "Cinturon de piel", "Accesorios", "Marron", "Casual", "Zara", "Atemporal");
        cinturon.setCategoriasPersonalizadas(null); // lista a null a posta: no debe romper el filtro, solo no pasa

        List<Prenda> armario = List.of(vestido, camiseta, blazer, cinturon);

        // llamo al metodo combinando categoria personalizada y atributo fijo
        Map<String, String> filtros = Map.of("categoriaPersonalizada", "Para salir", "marca", "Zara");
        List<Prenda> resultado = gestor.filtrarCombinado(armario, filtros);

        // compruebo que solo pasa la que tiene la categoria Y la marca
        assertEquals(1, resultado.size());
        assertEquals("Vestido", resultado.get(0).getNombre());
    }

    // una prenda con un atributo a null no debe romper el filtrado, solo no pasa ese criterio
    @Test
    void filtrarConAtributoNullNoFalla() {
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camiseta basica", "Camisetas", "Blanco", "Casual", null, "Verano"), // sin marca
                new Prenda("Abrigo", "Abrigo largo", "Abrigos", "Beige", "Formal", "Zara", "Invierno")
        );
        List<Prenda> resultado = gestor.filtrarCombinado(armario, Map.of("marca", "Zara"));
        assertEquals(1, resultado.size());
        assertEquals("Abrigo", resultado.get(0).getNombre());
    }

    // sin filtros activos tiene que devolver todas las prendas
    @Test
    void combinadoSinFiltrosDevuelveTodas() {
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camiseta basica", "Camisetas", "Blanco", "Casual", "Nike", "Verano"),
                new Prenda("Vaqueros", "Vaqueros rectos", "Pantalones", "Azul", "Casual", "Levis", "Atemporal")
        );
        List<Prenda> resultado = gestor.filtrarCombinado(armario, Map.of());
        assertEquals(2, resultado.size());
    }

    // si nada cumple todos los filtros, lista vacia (no null, no error)
    @Test
    void combinadoSinCoincidenciasDevuelveVacio() {
        GestorCategorias gestor = new GestorCategorias();
        List<Prenda> armario = List.of(
                new Prenda("Camiseta", "Camiseta basica", "Camisetas", "Blanco", "Casual", "Nike", "Verano")
        );
        Map<String, String> filtros = Map.of("tipo", "Camisetas", "marca", "Zara");
        List<Prenda> resultado = gestor.filtrarCombinado(armario, filtros);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
