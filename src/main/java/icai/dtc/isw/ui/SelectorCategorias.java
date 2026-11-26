package icai.dtc.isw.ui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SelectorCategorias extends JScrollPane {

    private final List<JCheckBox> casillas = new ArrayList<>();

    public SelectorCategorias(List<String> disponibles, List<String> marcadas) {
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        if (disponibles == null || disponibles.isEmpty()) {
            contenido.add(new JLabel("Aún no has creado categorías"));
        } else {
            for (String nombre : disponibles) {
                JCheckBox casilla = new JCheckBox(nombre);
                casilla.setSelected(marcadas != null && marcadas.contains(nombre));
                casillas.add(casilla);
                contenido.add(casilla);
            }
        }
        setViewportView(contenido);
        setBorder(BorderFactory.createEtchedBorder());
        setPreferredSize(new Dimension(260, 80));
    }

    public List<String> getSeleccionadas() {
        List<String> seleccionadas = new ArrayList<>();
        for (JCheckBox casilla : casillas) {
            if (casilla.isSelected()) {
                seleccionadas.add(casilla.getText());
            }
        }
        return seleccionadas;
    }
}
