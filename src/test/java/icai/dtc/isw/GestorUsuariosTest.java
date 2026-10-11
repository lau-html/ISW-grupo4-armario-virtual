package icai.dtc.isw;

import icai.dtc.isw.controler.GestorUsuarios;
import icai.dtc.isw.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class GestorUsuariosTest {

    private GestorUsuarios gestorUsuarios;

    @BeforeEach
    public void setUp() {
        gestorUsuarios = new GestorUsuarios();
    }

    // 1. Prueba unitaria de modelo y hashing BCrypt
    @Test
    public void testCreacionUsuarioYGetters() {
        String email = "test@comillas.es";
        String passwordPlana = "ClaveSecreta123";
        String hash = BCrypt.hashpw(passwordPlana, BCrypt.gensalt());

        Usuario usuario = new Usuario(email, hash);

        assertEquals(email, usuario.getEmail());
        assertEquals(hash, usuario.getPassword());
        assertTrue(BCrypt.checkpw(passwordPlana, usuario.getPassword()));
        assertFalse(BCrypt.checkpw("ClaveErronea", usuario.getPassword()));
    }

    // 2. Prueba de Registro y Login exitoso
    @Test
    public void testRegistroYLoginExitoso() {
        String emailTemporal = "test_" + System.currentTimeMillis() + "@test.com";
        String password = "Password123!";

        assertDoesNotThrow(() -> {
            boolean registrado = gestorUsuarios.registrar(emailTemporal, password);
            assertTrue(registrado);
        });

        assertDoesNotThrow(() -> {
            Usuario logueado = gestorUsuarios.login(emailTemporal, password);
            assertNotNull(logueado);
            assertEquals(emailTemporal, logueado.getEmail());
        });
    }

    // 3. Prueba de restricción de duplicados
    @Test
    public void testRegistroDuplicadoLanzaExcepcion() {
        String emailDuplicado = "duplicado_" + System.currentTimeMillis() + "@test.com";
        String password = "Password123!";

        assertDoesNotThrow(() -> gestorUsuarios.registrar(emailDuplicado, password));

        assertThrows(IllegalArgumentException.class, () -> {
            gestorUsuarios.registrar(emailDuplicado, password);
        });
    }

    // 4. Prueba de contraseña errónea en login
    @Test
    public void testLoginPasswordErronea() {
        String email = "fail_" + System.currentTimeMillis() + "@test.com";
        assertDoesNotThrow(() -> gestorUsuarios.registrar(email, "Correcta123"));

        assertThrows(IllegalArgumentException.class, () -> {
            gestorUsuarios.login(email, "Incorrecta123");
        });
    }
}