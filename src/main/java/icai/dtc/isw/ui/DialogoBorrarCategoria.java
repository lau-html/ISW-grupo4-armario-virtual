package icai.dtc.isw.ui;

import icai.dtc.isw.GestorCategoriasPers;
import icai.dtc.isw.Prenda;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.util.List;

// dialogo para borrar una categoria personalizada desde la interfaz.
// Pregunta que categoria borrar, pide confirmacion y se la quita tambien a las prendas que la tenian
public class DialogoBorrarCategoria {

    // devuelve true si se ha borrado alguna categoria (para que la ventana se actualice)
    public static boolean mostrar(Component padre, GestorCategoriasPers gestor, List<Prenda> prendas) {
        List<String> borrables = gestor.getCategoriasBorrables();
        if (borrables.isEmpty()) {
            JOptionPane.showMessageDialog(padre,
                    "No tienes categorías personalizadas para borrar.\nLa categoría FAVORITOS no se puede borrar.",
                    "Borrar categoría", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        // desplegable con las categorias que se pueden borrar
        String elegida = (String) JOptionPane.showInputDialog(padre,
                "¿Qué categoría quieres borrar?",
                "Borrar categoría",
                JOptionPane.QUESTION_MESSAGE, null,
                borrables.toArray(), borrables.get(0));
        if (elegida == null) {
            return false; // ha cancelado
        }

        int numPrendas = contarPrendasCon(elegida, prendas);
        String aviso = "¿Seguro que quieres borrar la categoría '" + elegida + "'?";
        if (numPrendas > 0) {
            aviso += "\nSe quitará de " + numPrendas + (numPrendas == 1 ? " prenda." : " prendas.");
        }
        int confirmacion = JOptionPane.showConfirmDialog(padre, aviso, "Confirmar borrado",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return false;
        }

        if (gestor.borrarCategoria(elegida, prendas) < 0) {
            JOptionPane.showMessageDialog(padre, "No se ha podido borrar la categoría.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(padre, "Categoría '" + elegida + "' borrada.");
        return true;
    }

    private static int contarPrendasCon(String categoria, List<Prenda> prendas) {
        int total = 0;
        if (prendas == null) {
            return 0;
        }
        for (Prenda p : prendas) {
            if (p.getCategoriasPersonalizadas() != null && p.getCategoriasPersonalizadas().contains(categoria)) {
                total++;
            }
        }
        return total;
    }
}
