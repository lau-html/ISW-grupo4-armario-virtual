package icai.dtc.isw;

import icai.dtc.isw.ui.SesionUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//pruebas de crear y borrar categorias personalizadas (sin sesion, solo en memoria)
class GestorCategoriasPersTest {

    private GestorCategoriasPers gestor;

    @BeforeEach
    void setUp() {
        SesionUsuario.cerrarSesion(); // sin sesion el gestor no llama a la BD
        gestor = new GestorCategoriasPers();
    }

    @Test
    void favoritosExisteYNoSePuedeBorrar() {
        assertTrue(gestor.getCategorias().contains("FAVORITOS"));
        assertFalse(gestor.borrarCategoria("FAVORITOS"));
        assertFalse(gestor.borrarCategoria("favoritos"));
        assertTrue(gestor.getCategorias().contains("FAVORITOS"));
        assertFalse(gestor.getCategoriasBorrables().contains("FAVORITOS"));
    }

    @Test
    void noSeCreanCategoriasVaciasNiRepetidas() {
        assertTrue(gestor.anadirCategoria("Verano"));
        assertFalse(gestor.anadirCategoria("verano"));
        assertFalse(gestor.anadirCategoria("   "));
        assertEquals(2, gestor.getCategorias().size()); // FAVORITOS y Verano
    }

    @Test
    void borrarUnaCategoriaQueNoExisteDevuelveFalse() {
        assertFalse(gestor.borrarCategoria("Trabajo"));
        assertEquals(-1, gestor.borrarCategoria("Trabajo", new ArrayList<>()));
    }

    @Test
    void borrarCategoriaLaQuitaDeLasPrendas() {
        gestor.anadirCategoria("Viajes");
        Prenda camiseta = new Prenda(1, "Camiseta blanca", "Camiseta", "Blanco", "", "Casual", "Zara", "Verano");
        Prenda falda = new Prenda(2, "Falda vaquera", "Falda", "Azul", "", "Casual", "Mango", "Verano");
        Prenda abrigo = new Prenda(3, "Abrigo negro", "Abrigo/Cazadora", "Negro", "", "Clásico", "H&M", "Invierno");
        gestor.asignarCategoria(camiseta, "Viajes");
        gestor.asignarCategoria(falda, "Viajes");
        List<Prenda> armario = List.of(camiseta, falda, abrigo);

        int cambiadas = gestor.borrarCategoria("viajes", armario); // da igual mayusculas

        assertEquals(2, cambiadas);
        assertFalse(gestor.getCategorias().contains("Viajes"));
        assertFalse(camiseta.getCategoriasPersonalizadas().contains("Viajes"));
        assertFalse(falda.getCategoriasPersonalizadas().contains("Viajes"));
    }

    @Test
    void borrarCategoriaNoTocaLasDemas() {
        gestor.anadirCategoria("Trabajo");
        gestor.anadirCategoria("Fiesta");
        Prenda camisa = new Prenda(1, "Camisa", "Camisa", "Blanco", "", "Formal", "Zara", "Atemporal");
        gestor.asignarCategoria(camisa, "Trabajo");
        gestor.asignarCategoria(camisa, "Fiesta");

        gestor.borrarCategoria("Fiesta", List.of(camisa));

        assertTrue(gestor.getCategorias().contains("Trabajo"));
        assertEquals(List.of("Trabajo"), camisa.getCategoriasPersonalizadas());
    }
}