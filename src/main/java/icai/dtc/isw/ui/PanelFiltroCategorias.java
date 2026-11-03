package icai.dtc.isw.ui;

import java.awt.Cursor;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import icai.dtc.isw.GestorCategorias;
import icai.dtc.isw.domain.Prenda;


// Nota: esto es un panel porque al filtrar aparecera dentro de una ventana, no es una ventana en sí.
// Hago el panel aparte para poder reutilizarlo sin necesidad de cambair directamente el codigo de las ventanas que ya funcionan - LAU

// sprint 2 - he cambiado que sea un desplegable a que sea un boton para que cuadre con el resto de la barra superior
public class PanelFiltroCategorias extends JPanel {

    public interface EscuchadorFiltro { // es como un ActionListener, cuando cambie el filtro llamamos a este metodo con las prendas filtradas 
        void mostrarPrendas(List<Prenda> prendas);
    }

    private final GestorCategorias gestorCategorias = new GestorCategorias(); // llamo al gestor de categorias para poder categorizar y filtrar las prendas
    private final JButton botonCategorias = new JButton(); // boton que abre el menu de categorias para filtrar
    private final JPopupMenu menuCategorias = new JPopupMenu(); // menu con las categorias que se pueden elegir para filtrar, JPopupMenu es un menu que se abre al pulsar un boton, no es un menu de barra superior
    private final EscuchadorFiltro listener; // metodo de la ventana al que avisar cuando cambie el filtro seleccionado
    private List<Prenda> prendas; // prendas sobre las que se filtra (se deben actualizar cuando cambia el armario)
    private String categoriaActual = "Todas"; // categoria elegida por defecto, para enseñarla en el boton y no perderla al actualizar (muestra todas las prendas del armario, sin filtro)


    // recibe todas las prendas y el escuchador (listener) (metodo de antes al que llamar al cambiar el filtro)
    public PanelFiltroCategorias(List<Prenda> prendas, EscuchadorFiltro listener) {
        this.prendas = prendas; // prendas para poder filtrarlas cuando cambia el filtro
        this.listener = listener; // escuchador para avisar a ventana cuando cambia el filtro

        // ajusto la estetica del boton al estilo de  dressB, igual que los demas botones de la ventana
        botonCategorias.setBackground(JVentana.BURDEOS); // fondo burdeos como los demas botones
        botonCategorias.setForeground(JVentana.CREMA); 
        botonCategorias.setFont(new Font("SansSerif", Font.BOLD, 13));
        botonCategorias.setFocusPainted(false); // quito el recuadro de foco
        botonCategorias.setBorderPainted(false);
        botonCategorias.setOpaque(true); // se pinta el fondo del boton
        botonCategorias.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        botonCategorias.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16)); // margen

        botonCategorias.addActionListener(e -> menuCategorias.show(botonCategorias, 0, botonCategorias.getHeight())); // al pulsar el boton, abre el menu de categorias justo debajo

        setBackground(JVentana.CREMA); 
        rellenarMenu(); // rellena el menu con las categorias disponibles, definido debajo
        botonCategorias.setText("Categoría: " + categoriaActual); // texto del boton con el filtro activo
        add(botonCategorias);
    }


    // Rellena el menu con "Todas" (no filtra) + los tipos que existen en las prendas actuales
    private void rellenarMenu() {
        menuCategorias.removeAll(); // vacio el menu para rellenarlo con las categorias de ahora, si se han añadido prendas nuevas o quitado, se actualiza
        anadirOpcion("Todas"); // opcion para quitar el filtro y mostrar todas las prendas
        gestorCategorias.categorizarPorTipo(prendas).keySet().forEach(this::anadirOpcion); // una opcion por categoria existente
    }

    // Añade una opcion al menu desplegable, al pulsarla y esogerla se aplicara el filtro
    private void anadirOpcion(String categoria) { 
        JMenuItem opcion = new JMenuItem(categoria); // fila clickable del menu
        opcion.addActionListener(e -> aplicarFiltro(categoria)); // al pulsar la opcion, se aplica el filtro de esa categoria
        menuCategorias.add(opcion); // añado la opcion al menu visiblemente
    }

    // aplica el filtro elegido y llama a la ventana para que muestre las prendas filtradas
    private void aplicarFiltro(String categoriaElegida) {
        categoriaActual = categoriaElegida; 
        botonCategorias.setText("Categoría: " + categoriaElegida); // se enseña el filtro activo dentro del boton
        List<Prenda> prendasFiltradas;
        if ("Todas".equals(categoriaElegida)) {
            prendasFiltradas = prendas; // mostrar todas las prendas si el filtro es todas (basicamente no filtrar)
        } else { // en cualquier otro caso habra un filtro normal, llamo al gestor y filtro
            prendasFiltradas = gestorCategorias.prendasPorTipo(prendas, categoriaElegida);
        }
        listener.mostrarPrendas(prendasFiltradas); // la ventana solo pintara las prendas filtradas o todas en su caso
    }

    // se llama a este metodo cuando cambien las prendas del armario (añadir, eliminar...) para que el filtro vea las categorias nuevas
    public void actualizarPrendas(List<Prenda> nuevasPrendas) {
        this.prendas = nuevasPrendas;
        if (!"Todas".equals(categoriaActual) && !gestorCategorias.categorizarPorTipo(prendas).containsKey(categoriaActual)) {
            categoriaActual = "Todas"; // si la categoria del filtro ya no existe, quito el filtro y muestro todas las prendas
        }
        rellenarMenu();
        aplicarFiltro(categoriaActual); // repinto las prendas con el filtro activo que habia antes de actualizar
    }

}
