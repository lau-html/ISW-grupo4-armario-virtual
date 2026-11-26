package icai.dtc.isw.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import icai.dtc.isw.EstiloPrenda;
import icai.dtc.isw.Prenda;
import icai.dtc.isw.Temporada;
import icai.dtc.isw.TipoPrenda;


public class FormularioPrenda extends JFrame {

    private JTextField txtNombre, txtDescripcion, txtColor, txtMarca;
    private JComboBox<TipoPrenda> cbTipo;
    private JComboBox<EstiloPrenda> cbEstilo;
    private JComboBox<Temporada> cbTemporada;
    private JButton btnGuardar;
    private JVentana ventanaPrincipal;
    private JButton btnFoto;
    private JLabel lblFoto;
    private String rutaFotoSeleccionada;
    private SelectorCategorias selectorCategorias;

    public FormularioPrenda() {
        this(null);
    }

    public FormularioPrenda(JVentana ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        setTitle("dressB - Nueva prenda");
        setSize(420, 540);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtColor = new JTextField();
        txtMarca = new JTextField();

        cbTipo = new JComboBox<>(TipoPrenda.values());
        cbEstilo = new JComboBox<>(EstiloPrenda.values());
        cbTemporada = new JComboBox<>(Temporada.values());

        JPanel campos = new JPanel(new GridLayout(8, 2, 10, 10));
        campos.add(new JLabel(" Nombre:"));
        campos.add(txtNombre);
        campos.add(new JLabel(" Tipo / Categoría:"));
        campos.add(cbTipo);
        campos.add(new JLabel(" Descripción:"));
        campos.add(txtDescripcion);
        campos.add(new JLabel(" Estilo:"));
        campos.add(cbEstilo);
        campos.add(new JLabel(" Color:"));
        campos.add(txtColor);
        campos.add(new JLabel(" Marca:"));
        campos.add(txtMarca);
        campos.add(new JLabel(" Temporada:"));
        campos.add(cbTemporada);

        btnFoto = new JButton("Seleccionar foto...");
        lblFoto = new JLabel("Sin foto");
        btnFoto.addActionListener(e -> {
            String ruta = SelectorFoto.elegirFoto(this);
            if (ruta != null) {
                rutaFotoSeleccionada = ruta;
                lblFoto.setText(SelectorFoto.nombreArchivo(ruta));
            }
        });
        JPanel panelFoto = new JPanel(new BorderLayout(5, 0));
        panelFoto.add(btnFoto, BorderLayout.WEST);
        panelFoto.add(lblFoto, BorderLayout.CENTER);
        campos.add(new JLabel(" Foto:"));
        campos.add(panelFoto);

        List<String> categorias = ventanaPrincipal != null
                ? ventanaPrincipal.getCategoriasPersonalizadas() : new ArrayList<>();
        selectorCategorias = new SelectorCategorias(categorias, null);
        JPanel panelCategorias = new JPanel(new BorderLayout(10, 0));
        panelCategorias.add(new JLabel(" Categorías:"), BorderLayout.WEST);
        panelCategorias.add(selectorCategorias, BorderLayout.CENTER);

        btnGuardar = new JButton("Guardar Prenda");
        btnGuardar.addActionListener(e -> guardarDatos());

        JPanel zonaInferior = new JPanel(new BorderLayout(0, 10));
        zonaInferior.add(panelCategorias, BorderLayout.CENTER);
        zonaInferior.add(btnGuardar, BorderLayout.SOUTH);

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(campos, BorderLayout.CENTER);
        contenido.add(zonaInferior, BorderLayout.SOUTH);
        add(contenido, BorderLayout.CENTER);
    }

    private void guardarDatos() {
        String nombre = txtNombre.getText().trim();
        String tipo = ((TipoPrenda) cbTipo.getSelectedItem()).getEtiqueta();
        String descripcion = txtDescripcion.getText().trim();
        String estilo = ((EstiloPrenda) cbEstilo.getSelectedItem()).getEtiqueta();
        String color = txtColor.getText().trim();
        String marca = txtMarca.getText().trim();
        String temporada = ((Temporada) cbTemporada.getSelectedItem()).getEtiqueta();

        if (nombre.isEmpty() || color.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y el color son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Prenda nuevaPrenda = new Prenda(nombre, descripcion, tipo, color, estilo, marca, temporada);
        nuevaPrenda.setRutaFoto(rutaFotoSeleccionada);
        nuevaPrenda.setCategoriasPersonalizadas(new ArrayList<>(selectorCategorias.getSeleccionadas()));
        if (ventanaPrincipal != null) {
            ventanaPrincipal.agregarPrenda(nuevaPrenda);
        }

        JOptionPane.showMessageDialog(this, "¡Prenda añadida al armario!");
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormularioPrenda ventana = new FormularioPrenda();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}
