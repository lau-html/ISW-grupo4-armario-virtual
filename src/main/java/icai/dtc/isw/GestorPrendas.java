package icai.dtc.isw;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import icai.dtc.isw.client.Client;
import icai.dtc.isw.domain.Prenda; // Importación necesaria para que reconozca el objeto Prenda

public class GestorPrendas { // SPRINT 1: EDITAR Y ELIMINAR PRENDAS  ·  SPRINT 2: PRENDAS POR USUARIO

    // Busca una prenda por su id. Devuelve null si no existe.
    public Prenda buscarPorId(List<Prenda> prendas, int id) {
        for (Prenda prenda : prendas) {
            if (prenda.getId() == id) {
                return prenda;
            }
        }
        return null;
    }

    // Cambia los datos de la prenda con ese id.
    // Devuelve true si la ha encontrado y editado, false si no existe.
    public boolean editarPrenda(List<Prenda> prendas, int id, String nombre, String tipo, String color) {
        Prenda prenda = buscarPorId(prendas, id);
        if (prenda == null) {
            return false;
        }
        prenda.setNombre(nombre);
        prenda.setEstilo(tipo);
        prenda.setColor(color);
        return true;
    }

    // Elimina la prenda con ese id.
    // Devuelve true si la ha encontrado y borrado, false si no existe.
    // CORRECCIÓN: Se han quitado los parámetros "nombre", "tipo" y "color" que sobraban
    public boolean eliminarPrenda(List<Prenda> prendas, int id) {
        return prendas.removeIf(prenda -> prenda.getId() == id);
    }

    // ======================= SPRINT 2 =======================

    /**
     * Pide al servidor las prendas del usuario con sesión iniciada.
     * Llamar justo después de un login correcto (y cada vez que haya que refrescar la lista).
     * masRecientes = true -> más recientes primero; false -> más antiguas primero.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Prenda> cargarPrendasUsuario(boolean masRecientes) {
        if (!SesionUsuario.haySesion()) {
            System.out.println("No hay sesión iniciada: no se cargan prendas.");
            return new ArrayList<>();
        }
        HashMap<String, Object> session = new HashMap<>();
        session.put("usuarioEmail", SesionUsuario.getUsuarioActual().getEmail());
        session.put("masRecientes", masRecientes);

        HashMap<String, Object> respuesta = new Client().sentMessage("/getPrendasUsuario", session);
        ArrayList<Prenda> prendas = (ArrayList<Prenda>) respuesta.get("Prendas");
        return prendas != null ? prendas : new ArrayList<>();
    }

    /**
     * Envía una prenda al servidor para guardarla en la BD a nombre del usuario activo.
     * Devuelve true si se ha guardado.
     */
    public boolean guardarPrenda(Prenda prenda) {
        if (!SesionUsuario.haySesion()) {
            System.out.println("No hay sesión iniciada: no se puede guardar la prenda.");
            return false;
        }
        prenda.setUsuarioEmail(SesionUsuario.getUsuarioActual().getEmail());

        HashMap<String, Object> session = new HashMap<>();
        session.put("Prenda", prenda);

        HashMap<String, Object> respuesta = new Client().sentMessage("/guardarPrenda", session);
        return Boolean.TRUE.equals(respuesta.get("ok"));
    }

    // BONUS: ordenar una lista ya cargada sin volver a consultar la BD (Comparator).
    // Las prendas sin fecha (p. ej. las del mock) se quedan al final.
    public void ordenarMasAntiguas(List<Prenda> prendas) {
        prendas.sort(Comparator.comparing(Prenda::getFechaCreacion,
                        Comparator.nullsLast(Comparator.<LocalDateTime>naturalOrder()))
                .thenComparingInt(Prenda::getId));
    }

    public void ordenarMasRecientes(List<Prenda> prendas) {
        prendas.sort(Comparator.comparing(Prenda::getFechaCreacion,
                        Comparator.nullsLast(Comparator.<LocalDateTime>reverseOrder()))
                .thenComparing(Comparator.comparingInt(Prenda::getId).reversed()));
    }
}