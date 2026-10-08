package icai.dtc.isw;
import java.util.List;

public class GestorPrendas { //SPRINT 1: EDITAR Y ELIMINAR PRENDAS
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
        prenda.setTipo(tipo);
        prenda.setColor(color);
        return true;
    }

    // Elimina la prenda con ese id.
    // Devuelve true si la ha encontrado y borrado, false si no existe.
    public boolean eliminarPrenda(List<Prenda> prendas, int id) {
        return prendas.removeIf(prenda -> prenda.getId() == id);
    }

}
