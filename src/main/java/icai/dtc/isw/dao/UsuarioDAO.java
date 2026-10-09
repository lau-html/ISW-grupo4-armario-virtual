package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Verificar si el correo ya existe
    public static boolean existeEmail(Connection con, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Guardar nuevo usuario con la contraseña hasheada
    public static boolean registrarUsuario(Connection con, Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (email, password) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getEmail());
            ps.setString(2, usuario.getPassword());
            return ps.executeUpdate() > 0;
        }
    }

    // Buscar usuario por email para comprobar credenciales
    public static Usuario buscarPorEmail(Connection con, String email) throws SQLException {
        String sql = "SELECT id, email, password FROM usuarios WHERE email = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("email"), rs.getString("password"));
                }
            }
        }
        return null;
    }
}