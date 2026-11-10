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

    // SPRINT 2: mismo usuario, con el nombre que pide el enunciado (getUsuarioActual)
    public static Usuario getUsuarioActual() {
        return usuarioLogueado;
    }

    // SPRINT 2: true si hay alguien con sesión iniciada
    public static boolean haySesion() {
        return usuarioLogueado != null && usuarioLogueado.getEmail() != null;
    }

    // Limpia los datos del usuario en memoria cuando cierra sesion
    public static void cerrarSesion() {
        usuarioLogueado = null;
    }
}