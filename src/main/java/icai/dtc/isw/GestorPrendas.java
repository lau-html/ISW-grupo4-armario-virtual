package icai.dtc.isw;

import java.util.List;
import icai.dtc.isw.domain.Prenda; 
import icai.dtc.isw.dao.PrendaDAO; 

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
    // Conecta el formulario con la base de datos
    public boolean registrarNuevaPrenda(String nombre, String descripcion, String estilo, String color, String marca, String temporada) {
        // 1. Creamos el objeto con el constructor sin ID
        Prenda nuevaPrenda = new Prenda(nombre, descripcion, estilo, color, marca, temporada);
        
        // 2. Llamamos al DAO para que ejecute el INSERT
        PrendaDAO dao = new PrendaDAO();
        return dao.registrarPrenda(nuevaPrenda);
    }

    
    // Devuelve true si la ha encontrado y editado, false si no existe.
    public boolean editarPrenda(List<Prenda> prendas, int id, String nombre, String estilo, String color) {
        Prenda prenda = buscarPorId(prendas, id);
        if (prenda == null) {
            return false;
        }
        prenda.setNombre(nombre);
        prenda.setEstilo(estilo);
        prenda.setColor(color);
        return true;
    }

    // Elimina la prenda con ese id.
    // Devuelve true si la ha encontrado y borrado, false si no existe.
    public boolean eliminarPrenda(List<Prenda> prendas, int id) {
        return prendas.removeIf(prenda -> prenda.getId() == id);
    }

}



