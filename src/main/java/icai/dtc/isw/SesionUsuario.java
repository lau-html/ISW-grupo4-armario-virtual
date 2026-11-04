package icai.dtc.isw;

import icai.dtc.isw.domain.Usuario;

/**
 * SPRINT 2: guarda qué usuario ha iniciado sesión.
 * En el login, cuando las credenciales sean correctas: SesionUsuario.iniciarSesion(usuario);
 */
public class SesionUsuario {

    private static Usuario usuarioActual;

    private SesionUsuario() { }

    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean haySesion() {
        return usuarioActual != null && usuarioActual.getEmail() != null;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}