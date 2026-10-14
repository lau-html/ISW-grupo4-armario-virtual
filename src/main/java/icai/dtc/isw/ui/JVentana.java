package icai.dtc.isw.ui;

import icai.dtc.isw.GestorPrendas;
import icai.dtc.isw.Prenda;
import icai.dtc.isw.domain.Usuario;
import icai.dtc.isw.VisualizarNumPrendas;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// VENTANA PRINCIPAL DE dressB: se abre después de iniciar sesión.
public class JVentana extends JFrame {


    //  colores de la app
    public static final Color CREMA = new Color(251, 246, 241);   // fondo
    public static final Color ROSA = new Color(220, 135, 202);    // "dress" del logo y selección
    public static final Color BURDEOS = new Color(87, 6, 45);     // "B" del logo, botones y títulos
    public static final Color LIMA = new Color(225, 236, 159);    // detalles
    public static final Color GRIS_TEXTO = new Color(120, 110, 115); // textos secundarios

    private JPanel panelPrendas; // zona central de "Mi armario" donde se muestran las prendas
    private JList<Prenda> listaPrendas; // lista visible (para saber qué prenda está seleccionada)
    private List<Prenda> prendasActuales = new ArrayList<>(); // prendas que se están mostrando
    private final GestorPrendas gestorPrendas = new GestorPrendas(); // lógica de editar y eliminar

    public JVentana() {
        super("dressB");
        setSize(700, 500);
        setLocationRelativeTo(null); // centrar en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CREMA);

        // Arriba: logo y saludo al usuario
        add(crearCabecera(), BorderLayout.NORTH);

        // Centro: una pestaña por cada parte de la aplicación
        UIManager.put("TabbedPane.selected", LIMA);  // Cambia el color de la pestaña que esta seleccionada
        UIManager.put("TabbedPane.contentAreaColor", CREMA);
        UIManager.put("TabbedPane.focus", CREMA);            // sin recuadro de foco
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setBackground(CREMA);
        pestanas.setForeground(BURDEOS);
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 13));
        pestanas.addTab("Mi armario", crearPanelArmario());
        pestanas.addTab("Perfil", crearPanelPerfil());
        add(pestanas, BorderLayout.CENTER);
    }

    //  CABECERA
    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(CREMA);
        cabecera.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, LIMA),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)));

        // Logo: "dress" en rosa y "B" en burdeos
        JLabel lblLogo = new JLabel("<html><span style='color:rgb(220,135,202)'>dress</span>"
                + "<span style='color:rgb(87,6,45)'>B</span></html>");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 30));

        JLabel lblLema = new JLabel("Tu armario, a tu manera.");
        lblLema.setForeground(BURDEOS);

        JPanel panelLogo = new JPanel();
        panelLogo.setLayout(new BoxLayout(panelLogo, BoxLayout.Y_AXIS));
        panelLogo.setBackground(CREMA);
        panelLogo.add(lblLogo);
        panelLogo.add(lblLema);

        JLabel lblSaludo = new JLabel("Hola, " + obtenerEmailUsuario());
        lblSaludo.setForeground(BURDEOS);

        cabecera.add(panelLogo, BorderLayout.WEST);
        cabecera.add(lblSaludo, BorderLayout.EAST);
        return cabecera;
    }

    // PESTAÑA "MI ARMARIO"
    private JPanel crearPanelArmario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CREMA);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Barra superior con el botón para añadir prendas (abre el formulario de Añadir Prenda)
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.setBackground(CREMA);
        JButton btnAnadir = crearBoton("+ Añadir prenda");
        btnAnadir.addActionListener(e -> {
            FormularioPrenda formulario = new FormularioPrenda();
            formulario.setLocationRelativeTo(this);
            formulario.setVisible(true);
        });
        barra.add(btnAnadir);

        // Botones de editar y eliminar la prenda seleccionada
        JButton btnEditar = crearBoton("Editar");
        btnEditar.addActionListener(e -> editarPrendaSeleccionada());
        barra.add(btnEditar);

        JButton btnEliminar = crearBoton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarPrendaSeleccionada());
        barra.add(btnEliminar);
        panel.add(barra, BorderLayout.NORTH);

        // Botón del listado de prendad (Ver listado)
        JButton btnListado = crearBoton("Ver listado");
        btnListado.addActionListener(e -> VisualizarNumPrendas.mostrarVentana(prendasActuales));
        barra.add(btnListado);
        panel.add(barra, BorderLayout.NORTH);

        // Zona donde se muestran las prendas
        panelPrendas = new JPanel(new BorderLayout());
        panelPrendas.setBackground(CREMA);
        panel.add(panelPrendas, BorderLayout.CENTER);

        // De momento el armario está vacío.
        // Cuando esté el listado con la base de datos (Ver listado), aquí se cargarán las prendas del usuario.
        mostrarPrendas(new ArrayList<>());

        return panel;
    }

    // Muestra en "Mi armario" la lista de prendas que se le pase.
    // Si no hay ninguna, muestra un mensaje (criterio de aceptación de VerListado).
    public void mostrarPrendas(List<Prenda> prendas) {
        prendasActuales = new ArrayList<>(prendas); // guardamos la lista para poder editar y eliminar
        listaPrendas = null;
        panelPrendas.removeAll();

        if (prendas.isEmpty()) {
            JLabel lblVacio = new JLabel("Todavía no tienes prendas en tu armario. ¡Añade la primera!", SwingConstants.CENTER);
            lblVacio.setFont(new Font("SansSerif", Font.ITALIC, 14));
            lblVacio.setForeground(GRIS_TEXTO);
            panelPrendas.add(lblVacio, BorderLayout.CENTER);
        } else {
            DefaultListModel<Prenda> modelo = new DefaultListModel<>();
            for (Prenda prenda : prendas) {
                modelo.addElement(prenda);
            }
            JList<Prenda> lista = new JList<>(modelo);
            lista.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lista.setFixedCellHeight(30);
            lista.setSelectionBackground(ROSA);
            lista.setSelectionForeground(BURDEOS);
            listaPrendas = lista;
            panelPrendas.add(new JScrollPane(lista), BorderLayout.CENTER);
        }

        panelPrendas.revalidate();
        panelPrendas.repaint();
    }

    //  EDITAR Y ELIMINAR PRENDAS

    // Devuelve la prenda seleccionada, o null (con un aviso) si no hay ninguna
    private Prenda obtenerPrendaSeleccionada() {
        if (listaPrendas == null || listaPrendas.getSelectedValue() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona primero una prenda de tu armario.");
            return null;
        }
        return listaPrendas.getSelectedValue();
    }

    // Elimina la prenda seleccionada, pidiendo confirmación antes
    private void eliminarPrendaSeleccionada() {
        Prenda prenda = obtenerPrendaSeleccionada();
        if (prenda == null) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar \"" + prenda.getNombre() + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            gestorPrendas.eliminarPrenda(prendasActuales, prenda.getId());
            mostrarPrendas(prendasActuales); // refrescar la lista
        }
    }

    // Permite cambiar el nombre, el tipo y el color de la prenda seleccionada
    private void editarPrendaSeleccionada() {
        Prenda prenda = obtenerPrendaSeleccionada();
        if (prenda == null) {
            return;
        }
        JTextField txtNombre = new JTextField(prenda.getNombre());
        JTextField txtTipo = new JTextField(prenda.getTipo());
        JTextField txtColor = new JTextField(prenda.getColor());

        JPanel formulario = new JPanel(new GridLayout(3, 2, 8, 8));
        formulario.add(new JLabel("Nombre:")); formulario.add(txtNombre);
        formulario.add(new JLabel("Tipo:"));   formulario.add(txtTipo);
        formulario.add(new JLabel("Color:"));  formulario.add(txtColor);

        int respuesta = JOptionPane.showConfirmDialog(this, formulario,
                "Editar prenda", JOptionPane.OK_CANCEL_OPTION);
        if (respuesta == JOptionPane.OK_OPTION) {
            gestorPrendas.editarPrenda(prendasActuales, prenda.getId(),
                    txtNombre.getText(), txtTipo.getText(), txtColor.getText());
            mostrarPrendas(prendasActuales); // refrescar la lista
        }
    }

    //  PESTAÑA "PERFIL"
    private JPanel crearPanelPerfil() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CREMA);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("Mi perfil");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(BURDEOS);

        JLabel lblEmail = new JLabel("Correo: " + obtenerEmailUsuario());

        JButton btnCerrarSesion = crearBoton("Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> {
            SesionUsuario.cerrarSesion();
            dispose();                           // cerrar la ventana principal
            new VentanaLogin().setVisible(true); // volver al inicio de sesión
        });

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblEmail);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnCerrarSesion);
        return panel;
    }

    // Crea un botón con el estilo de dressB (fondo burdeos y letra crema)
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(BURDEOS);
        boton.setForeground(CREMA);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return boton;
    }

    // Devuelve el correo del usuario que ha iniciado sesión
    private String obtenerEmailUsuario() {
        Usuario usuario = SesionUsuario.getUsuarioLogueado();
        if (usuario == null) {
            return "invitado";
        }
        return usuario.getEmail();
    }

    // La aplicación cliente empieza en la pantalla de inicio de sesión
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaLogin().setVisible(true));
    }
}