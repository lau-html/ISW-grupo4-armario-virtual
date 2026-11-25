package icai.dtc.isw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Guarda en la BD las categorías personalizadas de cada usuario (tabla categoria_personalizada),
// para que no se pierdan al cerrar la aplicación aunque no tengan ninguna prenda
public class CategoriaDAO {

    // devuelve los nombres de las categorías del usuario
    public List<String> getCategoriasUsuario(String usuarioEmail) {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT nombre FROM categoria_personalizada WHERE usuario_email = ? ORDER BY nombre";
        Connection conn = ConnectionDAO.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuarioEmail);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    categorias.add(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar las categorías de " + usuarioEmail);
            e.printStackTrace();
        }
        return categorias;
    }

    // guarda una categoría nueva; si ya estaba guardada no hace nada
    public boolean guardarCategoria(String nombre, String usuarioEmail) {
        String sql = "INSERT INTO categoria_personalizada (usuario_email, nombre) VALUES (?, ?) ON CONFLICT DO NOTHING";
        Connection conn = ConnectionDAO.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuarioEmail);
            pstmt.setString(2, nombre);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar la categoría " + nombre);
            e.printStackTrace();
            return false;
        }
    }

    // borra una categoría del usuario
    public boolean borrarCategoria(String nombre, String usuarioEmail) {
        String sql = "DELETE FROM categoria_personalizada WHERE nombre = ? AND usuario_email = ?";
        Connection conn = ConnectionDAO.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, usuarioEmail);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar la categoría " + nombre);
            e.printStackTrace();
            return false;
        }
    }
}
