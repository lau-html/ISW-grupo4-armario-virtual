package icai.dtc.isw.dao;

import icai.dtc.isw.Outfit;
import icai.dtc.isw.Prenda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class OutfitDAO {

    public boolean registrarOutfit(Outfit outfit) {
        String sqlOutfit = "INSERT INTO outfit (nombre) VALUES (?)";
        String sqlRelacion = "INSERT INTO outfit_prenda (outfit_id, prenda_id) VALUES (?, ?)";

        try (Connection conn = ConnectionDAO.getInstance().getConnection()) {
            conn.setAutoCommit(false); // Transacción para asegurar que ambas tablas se guardan
            
            try (PreparedStatement pstmtOutfit = conn.prepareStatement(sqlOutfit, Statement.RETURN_GENERATED_KEYS)) {
                pstmtOutfit.setString(1, outfit.getNombre());
                pstmtOutfit.executeUpdate();
                
                ResultSet rs = pstmtOutfit.getGeneratedKeys();
                if (rs.next()) {
                    int idOutfitGenerado = rs.getInt(1);
                    
                    try (PreparedStatement pstmtRelacion = conn.prepareStatement(sqlRelacion)) {
                        for (Prenda p : outfit.getPrendas()) {
                            pstmtRelacion.setInt(1, idOutfitGenerado);
                            pstmtRelacion.setInt(2, p.getId());
                            pstmtRelacion.addBatch();
                        }
                        pstmtRelacion.executeBatch();
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace(); // AÑADIDO: Para ver el error real en la consola
                conn.rollback();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}