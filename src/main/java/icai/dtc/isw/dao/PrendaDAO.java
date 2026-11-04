package icai.dtc.isw.dao;

import icai.dtc.isw.SesionUsuario;
import icai.dtc.isw.domain.Prenda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class PrendaDAO {

    /**
     * Guarda la prenda asociada a su dueño.
     * SPRINT 2: el email sale de prenda.getUsuarioEmail(); si viene vacío,
     * se toma el usuario activo de SesionUsuario.getUsuarioActual().
     * (Misma firma que en el Sprint 1, así que las llamadas existentes siguen compilando.)
     */
    public boolean registrarPrenda(Prenda prenda) {
        String email = prenda.getUsuarioEmail();
        if ((email == null || email.isBlank()) && SesionUsuario.haySesion()) {
            email = SesionUsuario.getUsuarioActual().getEmail();
        }
        if (email == null || email.isBlank()) {
            System.out.println("No se puede guardar la prenda: no hay ningún usuario con sesión iniciada.");
            return false;
        }

        String sql = "INSERT INTO prenda (nombre, descripcion, tipo, color, marca, temporada, usuario_email) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // La conexión NO va dentro del try(...): ConnectionDAO es un singleton y, si se cierra
        // aquí, la siguiente consulta fallaría con "This connection has been closed".
        Connection conn = ConnectionDAO.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, prenda.getNombre());
            pstmt.setString(2, prenda.getDescripcion());
            pstmt.setString(3, prenda.getTipo());
            pstmt.setString(4, prenda.getColor());
            pstmt.setString(5, prenda.getMarca());
            pstmt.setString(6, prenda.getTemporada());
            pstmt.setString(7, email);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * SPRINT 2: devuelve solo las prendas de ese usuario (también las guardadas en sesiones anteriores).
     * BONUS: masRecientesPrimero = true -> más recientes primero; false -> más antiguas primero.
     */
    public ArrayList<Prenda> getPrendasUsuario(String usuarioEmail, boolean masRecientesPrimero) {
        ArrayList<Prenda> prendas = new ArrayList<>();
        if (usuarioEmail == null || usuarioEmail.isBlank()) {
            return prendas;
        }

        // El orden solo puede valer ASC o DESC, así que concatenarlo es seguro.
        // El email SIEMPRE va con "?" para evitar inyección SQL.
        String orden = masRecientesPrimero ? "DESC" : "ASC";
        String sql = "SELECT id, nombre, descripcion, tipo, color, marca, temporada, usuario_email, fecha_creacion "
                + "FROM prenda WHERE usuario_email = ? "
                + "ORDER BY fecha_creacion " + orden + ", id " + orden;

        Connection conn = ConnectionDAO.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuarioEmail);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Prenda p = new Prenda(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getString("tipo"),
                            rs.getString("color"),
                            rs.getString("marca"),
                            rs.getString("temporada"));
                    p.setUsuarioEmail(rs.getString("usuario_email"));
                    Timestamp ts = rs.getTimestamp("fecha_creacion");
                    if (ts != null) {
                        p.setFechaCreacion(ts.toLocalDateTime());
                    }
                    prendas.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar las prendas del usuario " + usuarioEmail);
            e.printStackTrace();
        }
        return prendas;
    }
}