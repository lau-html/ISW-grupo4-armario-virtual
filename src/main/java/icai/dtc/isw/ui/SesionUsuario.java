package icai.dtc.isw.ui;

import icai.dtc.isw.domain.Usuario;

public class SesionUsuario {
    private static Usuario usuarioLogueado = null;

    public static void setUsuarioLogueado(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void cerrarSesion() {
        usuarioLogueado = null;
    }
}