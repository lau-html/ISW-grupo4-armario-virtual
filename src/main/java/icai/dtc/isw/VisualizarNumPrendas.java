package icai.dtc.isw;

import javax.swing.*;
import java.util.List;

public class VisualizarNumPrendas {

    /**
     * Muestra la interfaz gráfica con el listado y conteo de prendas.
     *
     * @param armario Lista de objetos Prenda a mostrar en pantalla.
     */
    public static void mostrarVentana(List<Prenda> armario) {
        // Ventana visual
        JFrame ventana = new JFrame("Listado de Prendas");
        ventana.setSize(450, 520);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        ventana.add(new JScrollPane(areaTexto));

        areaTexto.append("    --- LISTADO DE PRENDAS ---\n\n");

        int totalPrendas = (armario != null) ? armario.size() : 0;

        if (totalPrendas == 0) {
            areaTexto.append(" No hay ninguna prenda, el catálogo está vacío.\n");
        } else {
            for (Prenda prenda : armario) {
                areaTexto.append(String.format(" • [%s] %s - Color: %s\n",
                        prenda.getTipo(),
                        prenda.getNombre(),
                        prenda.getColor()));
            }
        }

        areaTexto.append("\n----------------------------------------\n");
        areaTexto.append(" Hay " + totalPrendas + " prenda(s) en total.\n");

        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        // Se han ajustado los datos de prueba al constructor unificado de 8 parámetros
        // (id, nombre, tipo, color, descripcion, estilo, marca, temporada)
        List<Prenda> armario = List.of(
                new Prenda(1, "Camiseta Nike", "Camiseta", "Blanco", "Descripción genérica", "Casual", "Nike", "Verano"),
                new Prenda(2, "Vaqueros", "Pantalón", "Azul", "Descripción genérica", "Urbano", "Levi's", "Atemporal"),
                new Prenda(3, "Pantalones Deporte", "Pantalón", "Negro", "Descripción genérica", "Deportivo", "Adidas", "Atemporal")
        );

        SwingUtilities.invokeLater(() -> mostrarVentana(armario));
    }
}