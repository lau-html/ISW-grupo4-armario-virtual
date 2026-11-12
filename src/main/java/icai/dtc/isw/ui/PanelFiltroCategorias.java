package icai.dtc.isw.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import icai.dtc.isw.GestorCategorias;
import icai.dtc.isw.GestorCategoriasPers;
import icai.dtc.isw.Prenda;


// Nota: esto es un panel porque al filtrar aparecera dentro de una ventana, no es una ventana en sí.
// Hago el panel aparte para poder reutilizarlo sin necesidad de cambair directamente el codigo de las ventanas que ya funcionan - LAU

// sprint 2 - he cambiado que sea un desplegable a que sea un boton para que cuadre con el resto de la barra superior
public class PanelFiltroCategorias extends JPanel {

    public interface EscuchadorFiltro { // es como un ActionListener, cuando cambie el filtro llamamos a este metodo con las prendas filtradas 
        void mostrarPrendas(List<Prenda> prendas);
    }

    // Colores corporativos directos para evitar el fallo de importación cruzada
    private static final Color CREMA = new Color(251, 246, 241);
    private static final Color BURDEOS = new Color(87, 6, 45);

    private final GestorCategorias gestorCategorias = new GestorCategorias(); // llamo al gestor de categorias para poder categorizar y filtrar las prendas
    private final JButton botonCategorias = new JButton(); // boton que abre el menu de categorias para filtrar
    private final GestorCategoriasPers gestorPers; // gestor COMPARTIDO de categorias personalizadas: de aqui salen los nombres para el filtro
    private final JPopupMenu menuCategorias = new JPopupMenu(); // menu con las categorias que se pueden elegir para filtrar, JPopupMenu es un menu que se abre al pulsar un boton, no es un menu de barra superior
    private final EscuchadorFiltro listener; // metodo de la ventana al que avisar cuando cambie el filtro seleccionado
    private List<Prenda> prendas; // prendas sobre las que se filtra (se deben actualizar cuando cambia el armario)
    // filtros activos: criterio de filtrado el valor elegido de filtro si esta vacio mostrar todas 
    private final Map<String, String> filtrosActivos = new HashMap<>();


    // recibe todas las prendas y el escuchador (listener) (metodo de antes al que llamar al cambiar el filtro)
    public PanelFiltroCategorias(List<Prenda> prendas, EscuchadorFiltro listener,  GestorCategoriasPers gestorPers) {
        this.prendas = prendas; // prendas para poder filtrarlas cuando cambia el filtro
        this.listener = listener; // escuchador para avisar a ventana cuando cambia el filtro
        this.gestorPers = gestorPers; // gestor de categorias personalizadas compartido con la ventana para que se actualice al cambiar el nombre de una categoria

        // ajusto la estetica del boton al estilo de  dressB, igual que los demas botones de la ventana
        botonCategorias.setBackground(BURDEOS); // fondo burdeos como los demas botones
        botonCategorias.setForeground(CREMA);
        botonCategorias.setFont(new Font("SansSerif", Font.BOLD, 13));
        botonCategorias.setFocusPainted(false); // quito el recuadro de foco
        botonCategorias.setBorderPainted(false);
        botonCategorias.setOpaque(true); // se pinta el fondo del boton
        botonCategorias.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        botonCategorias.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16)); // margen

        botonCategorias.addActionListener(e -> menuCategorias.show(botonCategorias, 0, botonCategorias.getHeight())); // al pulsar el boton, abre el menu de categorias justo debajo

        setBackground(CREMA);
        rellenarMenu(); // rellena el menu con las categorias disponibles, definido debajo
        actualizarTextoBoton(); // texto del boton con los filtros activos
        add(botonCategorias);
    }


    // Rellena el menu con un submenu por atributo (tipo, marca...), otro con las categorias personalizadas y, si hay filtros activos, opciones para quitarlos. Los valores salen de TODAS las prendas, no solo de las filtradas.
    private void rellenarMenu() {
        menuCategorias.removeAll(); // vacio el menu para rellenarlo con las categorias de ahora, si se han añadido prendas nuevas o quitado, se actualiza

        for (String atributo : gestorCategorias.nombresAtributos()) { // un submenu por atributo registrado en el gestor
            JMenu submenu = new JMenu(atributo); // JMenu -> fila de menu que se despliega a un lado
            for (String valor : gestorCategorias.valoresDeAtributo(prendas, atributo)) {
                JMenuItem opcion = new JMenuItem(valor);
                opcion.addActionListener(e -> aplicarFiltro(atributo, valor)); // al pulsar, guarda el valor para ese criterio
                submenu.add(opcion);
            }
            anadirOpcionQuitar(submenu, atributo); // si este criterio tiene filtro activo, opcion de quitarlo
            menuCategorias.add(submenu);
    }

        // submenu de categorias personalizadas, los nombres salen del gestor, aunque no esten en prendas todavia, aunque no funcionara el usuario puede filtrar por algo que una prenda aun no tiene
        JMenu submenuPers = new JMenu("Categorías personalizadas"); // la clave del mapa de filtros es "categoriaPersonalizada"
        for (String nombre : gestorPers.getCategorias()) {
            JMenuItem opcion = new JMenuItem(nombre);
            opcion.addActionListener(e -> aplicarFiltro("categoriaPersonalizada", nombre));
            submenuPers.add(opcion);
        }
        anadirOpcionQuitar(submenuPers, "categoriaPersonalizada");
        menuCategorias.add(submenuPers);

        if (!filtrosActivos.isEmpty()) { // opcion para limpiar todos los filtros de golpe
            menuCategorias.addSeparator();
            JMenuItem quitarTodos = new JMenuItem("Quitar todos los filtros");
            quitarTodos.addActionListener(e -> {
                filtrosActivos.clear();
                reaplicar();
            });
            menuCategorias.add(quitarTodos);
        }
    }

    // añade al submenu la opcion de quitar el filtro de ese criterio, unicamente si esta activo
    private void anadirOpcionQuitar(JMenu submenu, String criterio) {
        if (filtrosActivos.containsKey(criterio)) {
            JMenuItem quitar = new JMenuItem("Quitar filtro");
            quitar.addActionListener(e -> quitarFiltro(criterio));
            submenu.addSeparator();
            submenu.add(quitar);
        }
    }

    // quita el filtro que quiero y reaplico filtrado a los que queden
    private void quitarFiltro(String filtroABorrar) {
        filtrosActivos.remove(filtroABorrar);
        reaplicar();
    }

    // guarda el valor elegido para ese criterio de filtrado (tipoFiltrado, por ej "marca") (si ya habia uno, lo sustituye) y reaplica todos los filtros
   private void aplicarFiltro(String tipoFiltrado, String valor) {
        filtrosActivos.put(tipoFiltrado, valor);
        reaplicar();
    }
    

    // aplicar el filtro elegido y llamar a la ventana para que muestre unicamente las prendas filtradas, cada vez que hay un cambio en los filtros hay que llamarlo
    private void reaplicar() {
        List<Prenda> prendasFiltradas = gestorCategorias.filtrarCombinado(prendas, filtrosActivos); // si el mapa esta vacio te va a devolver todas las prendas
        actualizarTextoBoton();
        rellenarMenu(); // repinto el menu para que aparezcan las opciones de quitar segun lo que este activo
        listener.mostrarPrendas(prendasFiltradas); // la ventana solo pintara las prendas filtradas o todas en su caso
    }

    // se llama a este metodo cuando cambien las prendas del armario (añadir, eliminar...) para que el filtro vea las categorias nuevas
    public void actualizarPrendas(List<Prenda> nuevasPrendas) {
        this.prendas = nuevasPrendas;
        reaplicar(); // mantengo los filtros activos y los aplico a las prendas actualizadas
    }

    // se le llama al cambiar un nombre de categoria personalizada, se añade o se borra, para que se reactualice el menu de filtrado
    public void actualizarCategorias() {
        reaplicar();
    }

    // texto del boton corto para que quepa bien en la ventana, completo se ve al pasar el raton (tooltip)
    private void actualizarTextoBoton() {
        if (filtrosActivos.isEmpty()) {
            botonCategorias.setText("Categoría: Todas");
            botonCategorias.setToolTipText(null); // si no hay filtros estoy mostrando todo y no hay nada extra q mostrar
        } else {
            botonCategorias.setText("Filtros: " + filtrosActivos.size());
            String textoFiltrosActivos = ""; // texto con todos los filtros elegidos
            for (Map.Entry<String, String> filtro : filtrosActivos.entrySet()) {
                if (!textoFiltrosActivos.equals("")) {
                    textoFiltrosActivos += " + "; // voy a ir añadiendo el tipo de filtro y por lo que estoy filtrando y con el raton el user pueda verlo
                }
                String nombreFiltro; // por lo que estoy filtrando (ej: "tipo")
                // la clave interna "categoriaPersonalizada" se muestra como "categoria" sin mas
                if (filtro.getKey().equals("categoriaPersonalizada")) {
                    nombreFiltro = "categoria";
                } else {
                    nombreFiltro = filtro.getKey();
                }
                textoFiltrosActivos += nombreFiltro + " = " + filtro.getValue();
            }
            botonCategorias.setToolTipText(textoFiltrosActivos); // va a ir añadiendose texto de esta manera: "tipo = Camisetas + marca = Zara"
        }
    }


}
