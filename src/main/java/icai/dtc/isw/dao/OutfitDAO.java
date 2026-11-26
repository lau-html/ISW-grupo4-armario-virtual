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

    // 1. CREAR OUTFIT (con usuario_id y favorito)
    public boolean registrarOutfit(Outfit outfit) {
        String sqlOutfit = "INSERT INTO outfit (nombre, usuario_id, favorito) VALUES (?, ?, ?)";
        String sqlRelacion = "INSERT INTO outfit_prenda (outfit_id, prenda_id) VALUES (?, ?)";

        try (Connection conn = ConnectionDAO.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtOutfit = conn.prepareStatement(sqlOutfit, Statement.RETURN_GENERATED_KEYS)) {
                pstmtOutfit.setString(1, outfit.getNombre());
                pstmtOutfit.setInt(2, outfit.getUsuarioId());
                pstmtOutfit.setBoolean(3, outfit.isFavorito());
                pstmtOutfit.executeUpdate();

                ResultSet rs = pstmtOutfit.getGeneratedKeys();
                if (rs.next()) {
                    int idOutfitGenerado = rs.getInt(1);
                    outfit.setId(idOutfitGenerado);

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
                ex.printStackTrace();
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

    // 2. OBTENER SOLO LOS OUTFITS DEL USUARIO LOGUEADO
    public List<Outfit> obtenerOutfitsPorUsuario(int usuarioId, List<Prenda> catalogoPrendas) {
        List<Outfit> lista = new ArrayList<>();
        String sqlOutfits = "SELECT id, nombre, usuario_id, favorito FROM outfit WHERE usuario_id = ?";
        String sqlPrendas = "SELECT prenda_id FROM outfit_prenda WHERE outfit_id = ?";

        try (Connection conn = ConnectionDAO.getInstance().getConnection();
             PreparedStatement pstmtOutfits = conn.prepareStatement(sqlOutfits)) {

            pstmtOutfits.setInt(1, usuarioId);

            try (ResultSet rsOutfits = pstmtOutfits.executeQuery()) {
                while (rsOutfits.next()) {
                    int outfitId = rsOutfits.getInt("id");
                    String nombre = rsOutfits.getString("nombre");
                    boolean favorito = rsOutfits.getBoolean("favorito");

                    List<Prenda> prendasDelOutfit = new ArrayList<>();
                    try (PreparedStatement pstmtPrendas = conn.prepareStatement(sqlPrendas)) {
                        pstmtPrendas.setInt(1, outfitId);
                        try (ResultSet rsPrendas = pstmtPrendas.executeQuery()) {
                            while (rsPrendas.next()) {
                                int prendaId = rsPrendas.getInt("prenda_id");
                                for (Prenda p : catalogoPrendas) {
                                    if (p.getId() == prendaId) {
                                        prendasDelOutfit.add(p);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    lista.add(new Outfit(outfitId, nombre, prendasDelOutfit, usuarioId, favorito));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 3. EDITAR / ACTUALIZAR UN OUTFIT EXISTENTE
    public boolean actualizarOutfit(Outfit outfit) {
        String sqlUpdateOutfit = "UPDATE outfit SET nombre = ?, favorito = ? WHERE id = ? AND usuario_id = ?";
        String sqlBorrarPrendas = "DELETE FROM outfit_prenda WHERE outfit_id = ?";
        String sqlInsertarPrenda = "INSERT INTO outfit_prenda (outfit_id, prenda_id) VALUES (?, ?)";

        try (Connection conn = ConnectionDAO.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Paso A: Actualizar nombre y favorito en la tabla outfit
                try (PreparedStatement pstmtUpd = conn.prepareStatement(sqlUpdateOutfit)) {
                    pstmtUpd.setString(1, outfit.getNombre());
                    pstmtUpd.setBoolean(2, outfit.isFavorito());
                    pstmtUpd.setInt(3, outfit.getId());
                    pstmtUpd.setInt(4, outfit.getUsuarioId());
                    pstmtUpd.executeUpdate();
                }

                // Paso B: Borrar las prendas antiguas asociadas a este outfit
                try (PreparedStatement pstmtDel = conn.prepareStatement(sqlBorrarPrendas)) {
                    pstmtDel.setInt(1, outfit.getId());
                    pstmtDel.executeUpdate();
                }

                // Paso C: Insertar las nuevas prendas seleccionadas
                try (PreparedStatement pstmtIns = conn.prepareStatement(sqlInsertarPrenda)) {
                    for (Prenda p : outfit.getPrendas()) {
                        pstmtIns.setInt(1, outfit.getId());
                        pstmtIns.setInt(2, p.getId());
                        pstmtIns.addBatch();
                    }
                    pstmtIns.executeBatch();
                }

                conn.commit();
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
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

    public boolean eliminarOutfit(int outfitId) {
        String sql = "DELETE FROM outfit WHERE id = ?";
        try (Connection conn = ConnectionDAO.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, outfitId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // SPRINT 3 (Cristina): marca o desmarca un outfit como favorito sin volver a guardar sus prendas.
    // Solo lo cambia si el outfit es de ese usuario. Devuelve true si se ha actualizado.
    public boolean actualizarFavorito(int outfitId, int usuarioId, boolean favorito) {
        String sql = "UPDATE outfit SET favorito = ? WHERE id = ? AND usuario_id = ?";
        // La conexión no se cierra aquí: ConnectionDAO la comparte entre todas las consultas
        Connection conn = ConnectionDAO.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, favorito);
            pstmt.setInt(2, outfitId);
            pstmt.setInt(3, usuarioId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el favorito del outfit " + outfitId);
            e.printStackTrace();
            return false;
        }
    }
}