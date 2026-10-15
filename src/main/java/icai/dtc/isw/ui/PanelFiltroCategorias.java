package icai.dtc.isw.ui;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import icai.dtc.isw.GestorCategorias;
import icai.dtc.isw.Prenda;

// Nota: esto es un panel porque al filtrar aparecera dentro de una ventana, no es una ventana en sí.
// Hago el panel aparte para poder reutilizarlo sin necesidad de cambair directamente el codigo de las ventanas que ya funcionan - LAU

public class PanelFiltroCategorias extends JPanel {

    public interface EscuchadorFiltro { // es como un ActionListener, cuando cambie el filtro llamamos a este metodo con las prendas filtradas 
        void mostrarPrendas(List<Prenda> prendas);
    }

    // recibe todas las prendas y el escuchador (listener) (metodo de antes al que llamar al cambiar el filtro)
    public PanelFiltroCategorias(List<Prenda> prendas, EscuchadorFiltro listener) {
        GestorCategorias gestorCategorias = new GestorCategorias(); // llamo al gestor de categorias para poder categorizar y filtrar las prendas

        JComboBox<String> desplegableCategorias = new JComboBox<>(); // creo el desplegable para elegir la categoria que quieras filtrar    
        desplegableCategorias.addItem("Todas"); // opcion para quitar el filtro activo

        // relleno el desplegable solo con las categorias que existen en el armario, de momento solo añado los tipos disponibles
        // nota lau - mas adelante habra que hacer esto con todos los atributos de la prenda, no solo tipo
        gestorCategorias.categorizarPorTipo(prendas).keySet().forEach(desplegableCategorias::addItem);

        desplegableCategorias.addActionListener(e -> { // cada vez que eliges una categoria, filtra prendas y llama al listener para que la ventana solo muestre las filtradas
            String categoriaElegida = (String) desplegableCategorias.getSelectedItem(); // la categoria elegida en el desplegable como filtro
            List<Prenda> prendasFiltradas;
            if ("Todas".equals(categoriaElegida)) {
                prendasFiltradas = prendas; // mostrar todas las prendas si el filtro es todas (basicamente no filtrar)
            } else { // en cualquier otro caso habra un filtro normal, llamo al gestor y filtro
                prendasFiltradas = gestorCategorias.prendasPorTipo(prendas, categoriaElegida);
            }
            listener.mostrarPrendas(prendasFiltradas); // la ventana solo pintara las prendas filtradas o todas en su caso
        });

        add(new JLabel("Categoría: "));
        add(desplegableCategorias);
    }

}