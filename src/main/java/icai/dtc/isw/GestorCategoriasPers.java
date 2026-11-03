package icai.dtc.isw; // Ajusta el paquete según tu proyecto

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class GestorCategoriasPers {
    private List<String> categorias;
    private final String CATEGORIA_POR_DEFECTO = "FAVORITOS";

    public GestorCategoriasPers() {
        categorias = new ArrayList<>();
        // 1. Inicializa siempre la lista con "FAVORITOS" por defecto
        categorias.add(CATEGORIA_POR_DEFECTO);
    }

    public void anadirCategoria(String nuevaCategoria) {
        if (nuevaCategoria == null || nuevaCategoria.trim().isEmpty()) {
            return;
        }
        
        String categoriaLimpia = nuevaCategoria.trim();

        // 2. Validar duplicados ignorando mayúsculas/minúsculas
        for (String cat : categorias) {
            if (cat.trim().equalsIgnoreCase(categoriaLimpia)) {
                // Lanza un JOptionPane de advertencia si ya existe
                JOptionPane.showMessageDialog(null, 
                    "La categoría '" + categoriaLimpia + "' ya existe.", 
                    "Advertencia de Duplicado", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        categorias.add(categoriaLimpia);
    }

    public void borrarCategoria(String categoriaABorrar) {
        // Bloquea que "FAVORITOS" se pueda borrar
        if (categoriaABorrar != null && categoriaABorrar.trim().equalsIgnoreCase(CATEGORIA_POR_DEFECTO)) {
            JOptionPane.showMessageDialog(null, 
                "La categoría FAVORITOS no se puede borrar.", 
                "Acción denegada", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        categorias.remove(categoriaABorrar);
    }

    public List<String> getCategorias() {
        return categorias;
    }
}