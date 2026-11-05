package icai.dtc.isw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestorPrendasTest {

    private GestorPrendas gestor;
    private List<Prenda> armario;

    // Se ejecuta antes de cada test: armario nuevo con 3 prendas
    @BeforeEach
    void setUp() {
        gestor = new GestorPrendas();
        armario = new ArrayList<>();
        armario.add(new Prenda(1, "Camiseta Nike", "Camisetas", "Blanco"));
        armario.add(new Prenda(2, "Vaqueros", "Pantalones", "Azul"));
        armario.add(new Prenda(3, "Pantalones Deporte", "Pantalones", "Negro"));
    }

    @Test
    void eliminarPrendaExistenteLaQuitaDeLaLista() {
        boolean resultado = gestor.eliminarPrenda(armario, 2);

        assertTrue(resultado);
        assertEquals(2, armario.size());
        assertNull(gestor.buscarPorId(armario, 2));
    }

    @Test
    void eliminarPrendaQueNoExisteNoCambiaNada() {
        boolean resultado = gestor.eliminarPrenda(armario, 99);

        assertFalse(resultado);
        assertEquals(3, armario.size());
    }

    @Test
    void editarPrendaCambiaSusDatos() {
        boolean resultado = gestor.editarPrenda(armario, 1, "Camiseta Adidas", "Camisetas", "Negro");

        assertTrue(resultado);
        Prenda editada = gestor.buscarPorId(armario, 1);
        assertEquals("Camiseta Adidas", editada.getNombre());
        assertEquals("Negro", editada.getColor());
    }

    @Test
    void editarPrendaNoTocaLasDemas() {
        gestor.editarPrenda(armario, 1, "Camiseta Adidas", "Camisetas", "Negro");

        Prenda otra = gestor.buscarPorId(armario, 2);
        assertEquals("Vaqueros", otra.getNombre());
        assertEquals("Azul", otra.getColor());
    }

    @Test
    void editarPrendaQueNoExisteDevuelveFalse() {
        boolean resultado = gestor.editarPrenda(armario, 99, "X", "Y", "Z");

        assertFalse(resultado);
    }
}