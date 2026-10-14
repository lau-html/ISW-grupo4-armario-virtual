package icai.dtc.isw.dao;

import icai.dtc.isw.domain.Prenda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PrendaDAO {

    public boolean registrarPrenda(Prenda prenda) {
        String sql = "INSERT INTO prenda (nombre, descripcion, estilo, color, marca, temporada) VALUES (?, ?, ?, ?, ?, ?)";
        
        // Obtenemos la conexión usando tu ConnectionDAO
        try (Connection conn = ConnectionDAO.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, prenda.getNombre());
            pstmt.setString(2, prenda.getDescripcion());
            pstmt.setString(3, prenda.getEstilo());
            pstmt.setString(4, prenda.getColor());
            pstmt.setString(5, prenda.getMarca());
            pstmt.setString(6, prenda.getTemporada());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}