package icai.dtc.isw.ui;

import icai.dtc.isw.domain.Usuario;

public class SesionUsuario {
    private static Usuario usuarioLogueado = null;

    // Guarda la referencia del usuario autenticado en la sesion activa
    public static void setUsuarioLogueado(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    // Permite consultar que usuario esta conectado
    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    // Limpia los datos del usuario en memoria cuando cierra sesion
    public static void cerrarSesion() {
        usuarioLogueado = null;
    }
}