package icai.dtc.isw.ui;

import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import icai.dtc.isw.GestorCategorias;
import icai.dtc.isw.Prenda;

// Fila extra de botones de filtros por tipo de prenda, cada uno con el numero de prendas de ese tipo que hay en el armario

public class PanelBotonesFiltrado extends JPanel {

    private static final String atributo = "tipo"; // el criterio por el que filtran estos botones, tipo es lo mas general y directo
    private final PanelFiltroCategorias panelFiltro;
    private final GestorCategorias gestorCategorias = new GestorCategorias(); // para sacar los tipos y contar prendas



    public PanelBotonesFiltrado(PanelFiltroCategorias panelFiltro) {
        this.panelFiltro = panelFiltro; // guardo esto para poder utilizar el filtrado directamente desde aqui
        setBackground(Tema.CREMA); 
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 6)); 
    }

    // repinta los botones con las prendas que hay (hay que llamarlo si cambian las prendas del armario o el filtro)
    public void actualizar(List<Prenda> prendas) {
        removeAll(); // botto los botones antiguos
        String valorActivo = panelFiltro.getFiltroActivo(atributo); // tipo filtrado ahora, o null si ninguno

        boolean todasActivo = (valorActivo == null); // "Todas" esta activo si no hay filtro de tipo puesto
        add(crearBoton("Todas", prendas.size(), todasActivo, null)); // primer boton = todas las prendas

        for (String valor : gestorCategorias.valoresDeAtributo(prendas, atributo)) { // un boton por cada tipo que existe en el armario
            Map<String, String> criterio = new HashMap<>();
            criterio.put(atributo, valor);
            int cantidad = gestorCategorias.filtrarCombinado(prendas, criterio).size(); // cuantas prendas tienen este tipo (para poder poner un contador)
            boolean activo = valor.equals(valorActivo); // true si este es el tipo filtrado ahora
            add(crearBoton(valor, cantidad, activo, valor));
        }
        revalidate(); // recalcular y recolocar 
        repaint(); // los vuelve a pintar
    }

    // crea un boton con nombre tipo y numero de prendas asociadas, valor null es quitar el filtro
    private JButton crearBoton(String texto, int cantidad, boolean activo, String valor) {
        JButton boton = Tema.getInstancia().crearBotonFiltrado(texto + " (" + cantidad + ")", activo); 

        boton.addActionListener(e -> {
            if (valor == null) {
                panelFiltro.quitarFiltro(atributo); // "Todas" -> quita filtro tipo
            } else {
                panelFiltro.aplicarFiltro(atributo, valor); // filtra por este tipo
            }
        });
        return boton;
    }
}