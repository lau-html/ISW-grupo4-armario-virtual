package icai.dtc.isw.controler;

import icai.dtc.isw.dao.ConnectionDAO;
import icai.dtc.isw.dao.UsuarioDAO;
import icai.dtc.isw.domain.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;

public class GestorUsuarios {

    // Registro de usuario
    public boolean registrar(String email, String passwordPlana) throws Exception {
        try (Connection con = ConnectionDAO.getInstance().getConnection()) {
            if (UsuarioDAO.existeEmail(con, email)) {
                throw new IllegalArgumentException("El correo ya está registrado.");
            }
            // Contraseña nunca en texto plano
            String hash = BCrypt.hashpw(passwordPlana, BCrypt.gensalt());
            Usuario usuario = new Usuario(email, hash);
            return UsuarioDAO.registrarUsuario(con, usuario);
        }
    }

    // Login de usuario
    public Usuario login(String email, String passwordPlana) throws Exception {
        try (Connection con = ConnectionDAO.getInstance().getConnection()) {
            Usuario usuario = UsuarioDAO.buscarPorEmail(con, email);
            if (usuario != null && BCrypt.checkpw(passwordPlana, usuario.getPassword())) {
                return usuario;
            }
            throw new IllegalArgumentException("Credenciales erróneas. Inténtalo de nuevo.");
        }
    }
}