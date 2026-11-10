package icai.dtc.isw.dao;

import icai.dtc.isw.Outfit;
import icai.dtc.isw.Prenda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

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

    public boolean anadirPrendaAOutfit(String nombreOutfit, int idPrenda) {
        // Busca el ID del outfit por su nombre y lo inserta en la tabla relacional
        String sql = "INSERT INTO outfit_prenda (outfit_id, prenda_id) VALUES ((SELECT id FROM outfit WHERE nombre = ? LIMIT 1), ?)";

        try (Connection conn = ConnectionDAO.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreOutfit);
            pstmt.setInt(2, idPrenda);
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Recupera todos los outfits y los asocia con las prendas del usuario
    public List<Outfit> obtenerTodosLosOutfits(List<Prenda> catalogoPrendas) {
        List<Outfit> lista = new ArrayList<>();
        String sqlOutfits = "SELECT id, nombre FROM outfit";
        String sqlPrendas = "SELECT prenda_id FROM outfit_prenda WHERE outfit_id = ?";

        try (Connection conn = ConnectionDAO.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rsOutfits = stmt.executeQuery(sqlOutfits)) {

            while (rsOutfits.next()) {
                int outfitId = rsOutfits.getInt("id");
                String nombre = rsOutfits.getString("nombre");

                List<Prenda> prendasDelOutfit = new ArrayList<>();
                try (PreparedStatement pstmt = conn.prepareStatement(sqlPrendas)) {
                    pstmt.setInt(1, outfitId);
                    try (ResultSet rsPrendas = pstmt.executeQuery()) {
                        while (rsPrendas.next()) {
                            int prendaId = rsPrendas.getInt("prenda_id");
                            // Buscar la prenda completa en el catálogo ya cargado
                            for (Prenda p : catalogoPrendas) {
                                if (p.getId() == prendaId) {
                                    prendasDelOutfit.add(p);
                                    break;
                                }
                            }
                        }
                    }
                }
                // Si el outfit tiene sus prendas, lo añadimos a la lista
                if (!prendasDelOutfit.isEmpty()) {
                    lista.add(new Outfit(nombre, prendasDelOutfit));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Borra un outfit
    // (las relaciones en outfit_prenda se borran solas por el ON DELETE CASCADE)
    public boolean eliminarOutfit(String nombreOutfit) {
        String sql = "DELETE FROM outfit WHERE nombre = ?";
        try (Connection conn = ConnectionDAO.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreOutfit);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}