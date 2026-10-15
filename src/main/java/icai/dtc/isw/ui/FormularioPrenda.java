package icai.dtc.isw.ui;

import icai.dtc.isw.Prenda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioPrenda extends JFrame {

    // 1. Declarar los componentes visuales y la referencia a la ventana principal
    private JTextField txtNombre, txtDescripcion, txtColor, txtMarca;
    private JComboBox<String> cbTipo, cbEstilo, cbTemporada;
    private JButton btnGuardar;
    private JVentana ventanaPrincipal;

    // Constructor vacío por si se abre de forma independiente en pruebas
    public FormularioPrenda() {
        this(null);
    }

    // Constructor conectado con el armario principal (JVentana)
    public FormularioPrenda(JVentana ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        // Configuración básica de la ventana
        setTitle("Registro de Nueva Prenda");
        setSize(400, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 10, 10)); // 8 filas y 2 columnas

        // 2. Inicializar los campos de texto y desplegables
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtColor = new JTextField();
        txtMarca = new JTextField();

        String[] tipos = {"Camiseta", "Pantalón", "Falda", "Vestido", "Chaqueta", "Calzado", "Accesorio"};
        cbTipo = new JComboBox<>(tipos);

        String[] estilos = {"Casual", "Urbano", "Deportivo", "Formal", "Elegante"};
        cbEstilo = new JComboBox<>(estilos);

        String[] temporadas = {"Primavera", "Verano", "Otoño", "Invierno", "Atemporal"};
        cbTemporada = new JComboBox<>(temporadas);

        // 3. Añadir los componentes a la ventana (Etiqueta + Campo)
        add(new JLabel(" Nombre:"));
        add(txtNombre);
        add(new JLabel(" Tipo / Categoría:"));
        add(cbTipo);
        add(new JLabel(" Descripción:"));
        add(txtDescripcion);
        add(new JLabel(" Estilo:"));
        add(cbEstilo);
        add(new JLabel(" Color:"));
        add(txtColor);
        add(new JLabel(" Marca:"));
        add(txtMarca);
        add(new JLabel(" Temporada:"));
        add(cbTemporada);

        // 4. Configurar el botón de guardar
        btnGuardar = new JButton("Guardar Prenda");
        add(new JLabel(""));
        add(btnGuardar);

        // 5. Conexión de la acción del botón
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDatos();
            }
        });
    }

    private void guardarDatos() {
        // Recoger los datos que el usuario ha escrito
        String nombre = txtNombre.getText().trim();
        String tipo = (String) cbTipo.getSelectedItem();
        String descripcion = txtDescripcion.getText().trim();
        String estilo = (String) cbEstilo.getSelectedItem();
        String color = txtColor.getText().trim();
        String marca = txtMarca.getText().trim();
        String temporada = (String) cbTemporada.getSelectedItem();

        // Validación en el frontal
        if (nombre.isEmpty() || color.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y el color son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear la prenda y enviarla a la ventana del armario para mostrarla como texto
        Prenda nuevaPrenda = new Prenda(0, nombre, tipo, color, descripcion, estilo, marca, temporada);

        if (ventanaPrincipal != null) {
            ventanaPrincipal.agregarPrenda(nuevaPrenda);
        }

        JOptionPane.showMessageDialog(this, "¡Prenda añadida al armario!");
        dispose(); // Cerrar el formulario al terminar
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormularioPrenda ventana = new FormularioPrenda();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}