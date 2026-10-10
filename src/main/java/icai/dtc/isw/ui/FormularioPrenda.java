package icai.dtc.isw.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioPrenda extends JFrame {

    // 1. Declarar los componentes visuales
    private JTextField txtNombre, txtDescripcion, txtColor, txtMarca;
    private JComboBox<String> cbEstilo, cbTemporada;
    private JButton btnGuardar;

    public FormularioPrenda() {
        // Configuración básica de la ventana
        setTitle("Registro de Nueva Prenda");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 10, 10)); // Cuadrícula de 7 filas y 2 columnas con márgenes

        // 2. Inicializar los campos de texto
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtColor = new JTextField();
        txtMarca = new JTextField();

        // Para el estilo y temporada, usamos listas desplegables (JComboBox) para evitar errores del usuario
        String[] estilos = {"Casual", "Urbano", "Deportivo", "Formal", "Elegante"};
        cbEstilo = new JComboBox<>(estilos);

        String[] temporadas = {"Primavera", "Verano", "Otoño", "Invierno", "Atemporal"};
        cbTemporada = new JComboBox<>(temporadas);

        // 3. Añadir los componentes a la ventana (Etiqueta + Campo)
        add(new JLabel(" Nombre:"));
        add(txtNombre);
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
        add(new JLabel("")); // Espacio vacío para alinear el botón a la derecha
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
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        String estilo = (String) cbEstilo.getSelectedItem();
        String color = txtColor.getText();
        String marca = txtMarca.getText();
        String temporada = (String) cbTemporada.getSelectedItem();

        // Pequeña validación en el frontal antes de enviar al servidor
        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // AQUÍ VA LA CONEXIÓN CON EL BACKEND
        // Llamar al método del controlador para guardar la prenda:
        // ControladorPrenda.guardar(nombre, descripcion, estilo, color, marca, temporada);

        JOptionPane.showMessageDialog(this, "Enviando datos al servidor...");
    }

    // Método principal para probar tu ventana directamente
    public static void main(String[] args) {
        // Ejecutar la interfaz gráfica de forma segura
        SwingUtilities.invokeLater(() -> {
            FormularioPrenda ventana = new FormularioPrenda();
            ventana.setLocationRelativeTo(null); // Centrar en la pantalla
            ventana.setVisible(true);
        });
    }
}
