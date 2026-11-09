package icai.dtc.isw.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import icai.dtc.isw.GestorCategoriasPers;
import icai.dtc.isw.GestorOutfits;
import icai.dtc.isw.GestorPrendas;
import icai.dtc.isw.Outfit;
import icai.dtc.isw.Prenda;
import icai.dtc.isw.VisualizarNumPrendas;
import icai.dtc.isw.domain.Usuario;

// VENTANA PRINCIPAL DE dressB: se abre después de iniciar sesión.
public class JVentana extends JFrame {

    // colores de la app
    public static final Color CREMA = new Color(251, 246, 241);   // fondo
    public static final Color ROSA = new Color(220, 135, 202);    // "dress" del logo y selección
    public static final Color BURDEOS = new Color(87, 6, 45);     // "B" del logo, botones y títulos
    public static final Color LIMA = new Color(225, 236, 159);    // detalles
    public static final Color GRIS_TEXTO = new Color(120, 110, 115); // textos secundarios

    private JPanel panelPrendas; // zona central de "Mi armario" donde se muestran las prendas
    private JList<Prenda> listaPrendas; // lista visible (para saber qué prenda está seleccionada)
    private List<Prenda> prendasActuales = new ArrayList<>(); // prendas que se están mostrando (pueden ser todas o solo filtradas)
    private List<Prenda> todasLasPrendas = new ArrayList<>(); // todas las prendas usuario
    private final GestorPrendas gestorPrendas = new GestorPrendas(); // lógica de editar y eliminar
    private PanelFiltroCategorias panelFiltro; // filtro de categorias, hay que actualizarlo cuando cambian las prendas para que vea las nuevas categorias
    private final GestorCategoriasPers gestorCategoriasPers = new GestorCategoriasPers(); // mismo gestor de categorias para el filtro y el detalle

    // SPRINT 2 (bonus): desplegable para ordenar por antigüedad - Lau: lo cambio a boton para que cuadre con la estetica
    private final JButton btnOrden = crearBoton("Más recientes"); // el texto es el orden elegido

    // Desplegables de la pestaña Outfits
    private JComboBox<Prenda> cbArriba = new JComboBox<>();
    private JComboBox<Prenda> cbAbajo = new JComboBox<>();
    private JComboBox<Prenda> cbAccesorios = new JComboBox<>();

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
        UIManager.put("TabbedPane.focus", CREMA);    // sin recuadro de foco

        JTabbedPane pestanas = new JTabbedPane();

        //PRUEBA, quitar el set ui si queremos volver a lo anterior
        pestanas.setUI(new BasicTabbedPaneUI() {
        @Override
        protected void paintContentBorder(Graphics g, int colocacion, int seleccionada) {
            // no dibujo el marco alrededor del contenido que no queda estetico
        }
        });

        pestanas.setBackground(CREMA);
        pestanas.setForeground(BURDEOS);
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 13));
        pestanas.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // separo las pestañas de la linea verde
        pestanas.addTab("Mi armario", crearPanelArmario());
        pestanas.addTab("Outfits", crearPanelOutfits());
        pestanas.addTab("Perfil", crearPanelPerfil());
        add(pestanas, BorderLayout.CENTER);
    }

    // CABECERA
    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(CREMA);
        cabecera.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, LIMA),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)));

        // Logo: "dress" en rosa y "B" en burdeos
        Font fuenteLogo; // la fuente del logo - va a ser gasoek one, la original, si no se encuentra, se usa una por defecto
        try { // cargo la Gasoek One desde resources
            fuenteLogo = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/GasoekOne-Regular.ttf")).deriveFont(Font.PLAIN, 30f);
        } catch (Exception e) { // si falta el archivo, sigue con la fuente de siempre
            fuenteLogo = new Font("SansSerif", Font.BOLD, 30);
        }
        JLabel lblDress = new JLabel("dress"); // "dress" en rosa
        lblDress.setFont(fuenteLogo);
        lblDress.setForeground(ROSA);
        JLabel lblB = new JLabel("B"); // "B" en burdeos
        lblB.setFont(fuenteLogo);
        lblB.setForeground(BURDEOS);
        JPanel textoLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // junta las dos partes
        textoLogo.setBackground(CREMA);
        textoLogo.add(lblDress);
        textoLogo.add(lblB);

        JLabel lblLema = new JLabel("Tu armario, a tu manera.");
        lblLema.setForeground(BURDEOS);
        lblLema.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 0)); // lo alineo con el logo

        JPanel panelLogo = new JPanel();
        panelLogo.setLayout(new BoxLayout(panelLogo, BoxLayout.Y_AXIS));
        panelLogo.setBackground(CREMA);
        // intento poner la imagen del logo de la app:
        Image imagenLogo = new ImageIcon(getClass().getResource("/Logo_dressB_.png")).getImage()
        .getScaledInstance(-1, 56, Image.SCALE_SMOOTH); // la achico para la cabecera
        JLabel lblImagen = new JLabel(new ImageIcon(imagenLogo));
        JPanel filaLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); // imagen a la izquierda del texto
        filaLogo.setBackground(CREMA);
        filaLogo.add(lblImagen);
        filaLogo.add(textoLogo);
        filaLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblLema.setAlignmentX(Component.LEFT_ALIGNMENT); // el lema sigue a la izquierda
        panelLogo.add(filaLogo);
        panelLogo.add(lblLema);

        JLabel lblSaludo = new JLabel("Hola, " + obtenerEmailUsuario());
        lblSaludo.setForeground(BURDEOS);

        cabecera.add(panelLogo, BorderLayout.WEST);
        cabecera.add(lblSaludo, BorderLayout.EAST);
        return cabecera;
    }

    // PESTAÑA "MI ARMARIO"
    private JPanel crearPanelArmario() {
        // SPRINT 2: cargar de la BD las prendas del usuario que ha iniciado sesión
        // (antes de crear el filtro, para que el filtro ya vea sus categorías)
        recargarPrendasDesdeBD();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CREMA);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Barra superior con el botón para añadir prendas (abre el formulario de Añadir Prenda)
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.setBackground(CREMA);
        JButton btnAnadir = crearBoton("+ Añadir prenda");
        btnAnadir.addActionListener(e -> {
            FormularioPrenda formulario = new FormularioPrenda(this);
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

        // LAU: filtro por categoria, al elegir una, usa mostrarPrendas con las filtradas
        panelFiltro = new PanelFiltroCategorias(todasLasPrendas, this::mostrarPrendas, gestorCategoriasPers);
        barra.add(panelFiltro);

        // Botón del listado de prendas (Ver listado)
        JButton btnListado = crearBoton("Ver listado");
        btnListado.addActionListener(e -> VisualizarNumPrendas.mostrarVentana((List) prendasActuales));
        barra.add(btnListado);

        // SPRINT 2 (bonus): ordenar por "más recientes" / "más antiguas" (respeta los filtros activos)
        JPopupMenu menuOrden = new JPopupMenu(); // se abre al pulsar el boton
        JMenuItem opcionRecientes = new JMenuItem("Más recientes");
        JMenuItem opcionAntiguas = new JMenuItem("Más antiguas");
        opcionRecientes.addActionListener(e -> cambiarOrden("Más recientes"));
        opcionAntiguas.addActionListener(e -> cambiarOrden("Más antiguas"));
        menuOrden.add(opcionRecientes);
        menuOrden.add(opcionAntiguas);
        btnOrden.addActionListener(e -> menuOrden.show(btnOrden, 0, btnOrden.getHeight()));
        barra.add(btnOrden);

        panel.add(barra, BorderLayout.NORTH);

        // Zona donde se muestran las prendas
        panelPrendas = new JPanel(new BorderLayout());
        panelPrendas.setBackground(CREMA);
        panel.add(panelPrendas, BorderLayout.CENTER);

        // SPRINT 2: se muestran las prendas guardadas del usuario (si no tiene, sale el mensaje de armario vacío)
        mostrarPrendas(todasLasPrendas);

        return panel;
    }

    // SPRINT 2: vuelve a leer de la BD las prendas del usuario activo.
    // Se rellena la MISMA lista (clear + addAll) porque otras partes guardan una referencia a ella.
    private void recargarPrendasDesdeBD() {
        todasLasPrendas.clear();
        todasLasPrendas.addAll(gestorPrendas.cargarPrendasUsuario(true));
        // repuebla el gestor con las categorias que vienen de la BD para que salgan en el filtro y el menu del detalle
        for (Prenda p : todasLasPrendas) {
            if (p.getCategoriasPersonalizadas() != null) {
                for (String nombre : p.getCategoriasPersonalizadas()) {
                    gestorCategoriasPers.anadirCategoria(nombre); // ya ignora duplicados y vacios
                }
            }
        }
        aplicarOrden();
    }

    // SPRINT 2 (bonus): ordena todasLasPrendas según lo elegido en el desplegable
    private void aplicarOrden() {
    if ("Más antiguas".equals(btnOrden.getText())) {
            gestorPrendas.ordenarMasAntiguas(todasLasPrendas);
        } else {
            gestorPrendas.ordenarMasRecientes(todasLasPrendas);
        }
    }
        // aplica el orden elegido en el menu y refresca la lista
    private void cambiarOrden(String orden) {
        btnOrden.setText(orden);
        aplicarOrden();
        panelFiltro.actualizarPrendas(todasLasPrendas);
    }

    // PESTAÑA "CREADOR DE OUTFITS"
    private JPanel crearPanelOutfits() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CREMA);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.setBackground(CREMA);
        JLabel lblTitulo = new JLabel("Diseña tu outfit");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(BURDEOS);
        barra.add(lblTitulo);
        panel.add(barra, BorderLayout.NORTH);

        // Cuadrícula central para elegir el nombre y las prendas
        JPanel panelLienzo = new JPanel(new GridLayout(4, 2, 10, 20));
        panelLienzo.setBackground(CREMA);
        panelLienzo.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        panelLienzo.add(new JLabel("Nombre del Look:"));
        JTextField txtNombre = new JTextField();
        panelLienzo.add(txtNombre);

        panelLienzo.add(new JLabel("Prenda Superior:"));
        panelLienzo.add(cbArriba);

        panelLienzo.add(new JLabel("Prenda Inferior:"));
        panelLienzo.add(cbAbajo);

        panelLienzo.add(new JLabel("Accesorios:")); // Criterio actualizado
        panelLienzo.add(cbAccesorios);

        actualizarDesplegables(); // Carga las opciones iniciales si las hubiera

        panel.add(panelLienzo, BorderLayout.CENTER);

        // Barra inferior con botón de guardado
        JPanel barraInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        barraInferior.setBackground(CREMA);
        JButton btnGuardar = crearBoton("Guardar Outfit");

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            Prenda arriba = (Prenda) cbArriba.getSelectedItem();
            Prenda abajo = (Prenda) cbAbajo.getSelectedItem();
            Prenda accesorios = (Prenda) cbAccesorios.getSelectedItem();

            if (nombre.isEmpty() || arriba == null || abajo == null || accesorios == null) {
                JOptionPane.showMessageDialog(this, "Por favor, asigna un nombre y selecciona todas las prendas.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Prenda> prendasSeleccionadas = new ArrayList<>();
            prendasSeleccionadas.add(arriba);
            prendasSeleccionadas.add(abajo);
            prendasSeleccionadas.add(accesorios);

            // Creamos el objeto Outfit unificado pasándole el nombre y la lista de prendas
            Outfit nuevoOutfit = new Outfit(nombre, prendasSeleccionadas);

            GestorOutfits gestor = new GestorOutfits();
            if (gestor.guardarNuevoOutfit(nombre, prendasSeleccionadas)) { // O gestor.guardarNuevoOutfit(nuevoOutfit) según prefiera su gestor
                JOptionPane.showMessageDialog(this, "¡Outfit guardado con éxito!");
                txtNombre.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el outfit en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        barraInferior.add(btnGuardar);
        panel.add(barraInferior, BorderLayout.SOUTH);

        return panel;
    }

    // Actualiza los desplegables de outfits con los datos actuales
    public void actualizarDesplegables() {
        cbArriba.removeAllItems();
        cbAbajo.removeAllItems();
        cbAccesorios.removeAllItems();

        for (Prenda p : todasLasPrendas) {
            cbArriba.addItem(p);
            cbAbajo.addItem(p);
            cbAccesorios.addItem(p);
        }
    }

    // se llama desde el detalle cuando cambian las categorias de una prenda o se crea una categoria nueva, le manndo refrescar los filtrados y prendas visibles
    private void refrescarVista() {
        panelFiltro.actualizarPrendas(todasLasPrendas); // reaplico filtros y actualizo el menu con los datos de ahora
    }


    // Muestra en "Mi armario" la lista de prendas que se le pase.
    public void mostrarPrendas(List<Prenda> prendas) {
        prendasActuales = new ArrayList<>(prendas); // guardamos la lista para poder editar y eliminar
        listaPrendas = null;
        panelPrendas.removeAll();

        if (prendas.isEmpty()) {
            String textoVacio;
            if (todasLasPrendas.isEmpty()) {
                textoVacio = "Todavía no tienes prendas en tu armario. ¡Añade la primera!";
            } else {
                textoVacio = "Ninguna prenda cumple los filtros activos. Prueba a quitar alguno.";
            }
            JLabel lblVacio = new JLabel(textoVacio, SwingConstants.CENTER);
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
            lista.setFixedCellHeight(36); // filas un poco mas altas
            lista.setBackground(CREMA); // fondo beige
            lista.setForeground(BURDEOS); // texto burdeos
            lista.setSelectionBackground(ROSA);
            lista.setSelectionForeground(BURDEOS);
            lista.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> lista, Object valor, int posicion, boolean seleccionada, boolean conFoco) {
                    JLabel fila = (JLabel) super.getListCellRendererComponent(lista, valor, posicion, seleccionada, conFoco);
                    fila.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 225, 218)), // separador fino
                            BorderFactory.createEmptyBorder(0, 12, 0, 12))); // margen lateral
                    return fila;
                }
            });

            listaPrendas = lista;
            // Se añade un MouseListener a la lista del catálogo para abrir la ventana emergente al hacer doble clic sobre una prenda.
            listaPrendas.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        Prenda prendaSeleccionada = listaPrendas.getSelectedValue();
                        if (prendaSeleccionada != null) {
                            new VentanaDetallePrenda(prendaSeleccionada, gestorCategoriasPers, JVentana.this::refrescarVista).setVisible(true); // paso el gestor compartido y el aviso para refrescar
                        }
                    }
                }
            });
            JScrollPane scrollLista = new JScrollPane(lista);
            scrollLista.getViewport().setBackground(CREMA); // fondo de la zona vacia igual que el resto
            scrollLista.setBorder(BorderFactory.createEmptyBorder()); // quito el marco gris para que quede más estetico
            panelPrendas.add(scrollLista, BorderLayout.CENTER);
        }

        panelPrendas.revalidate();
        panelPrendas.repaint();
    }

    // Añade una nueva prenda al armario y refresca la vista en texto
    // SPRINT 2: la prenda se guarda en la BD a nombre del usuario activo y se recarga la lista
    // (así cada prenda tiene su id y fecha reales de la BD).
    public void agregarPrenda(Prenda nuevaPrenda) {
        if (!gestorPrendas.guardarPrenda(nuevaPrenda)) {
            JOptionPane.showMessageDialog(this,
                    "No se ha podido guardar la prenda en la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        recargarPrendasDesdeBD();
        mostrarPrendas(todasLasPrendas);
        actualizarDesplegables(); // Se actualizan los desplegables
        panelFiltro.actualizarPrendas(todasLasPrendas); // actualizar el filtro con la nueva prenda
    }

    // EDITAR Y ELIMINAR PRENDAS

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
            // SPRINT 2: se borra también de la base de datos
            if (!gestorPrendas.eliminarPrendaGuardada(todasLasPrendas, prenda.getId())) {
                JOptionPane.showMessageDialog(this,
                        "No se ha podido eliminar la prenda de la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mostrarPrendas(todasLasPrendas); // refrescar la lista
            actualizarDesplegables();        // refrescar desplegables
            panelFiltro.actualizarPrendas(todasLasPrendas); // actualizar el filtro con la prenda eliminada
        }
    }

    // Permite cambiar el nombre, el tipo y el color y la foto de la prenda seleccionada
    private void editarPrendaSeleccionada() {
        Prenda prenda = obtenerPrendaSeleccionada();
        if (prenda == null) {
            return;
        }
        JTextField txtNombre = new JTextField(prenda.getNombre());
        JTextField txtTipo = new JTextField(prenda.getTipo());
        JTextField txtColor = new JTextField(prenda.getColor());
        // Foto: se guarda en un array para poder cambiarla desde el botón
        String[] rutaFoto = {prenda.getRutaFoto()};
        JLabel lblFoto = new JLabel(SelectorFoto.nombreArchivo(rutaFoto[0]));
        JButton btnFoto = new JButton("Cambiar foto...");
        btnFoto.addActionListener(e -> {
            String ruta = SelectorFoto.elegirFoto(this);
            if (ruta != null) {
                rutaFoto[0] = ruta;
                lblFoto.setText(SelectorFoto.nombreArchivo(ruta));
            }
        });
        JPanel panelFoto = new JPanel(new BorderLayout(5, 0));
        panelFoto.add(btnFoto, BorderLayout.WEST);
        panelFoto.add(lblFoto, BorderLayout.CENTER);

        JPanel formulario = new JPanel(new GridLayout(4, 2, 8, 8));
        formulario.add(new JLabel("Nombre:")); formulario.add(txtNombre);
        formulario.add(new JLabel("Tipo:"));   formulario.add(txtTipo);
        formulario.add(new JLabel("Color:"));  formulario.add(txtColor);
        formulario.add(new JLabel("Foto:"));   formulario.add(panelFoto);

        int respuesta = JOptionPane.showConfirmDialog(this, formulario,
                "Editar prenda", JOptionPane.OK_CANCEL_OPTION);
        if (respuesta == JOptionPane.OK_OPTION) {
            // SPRINT 2: el cambio se guarda también en la base de datos
            boolean editada = gestorPrendas.editarPrendaGuardada(todasLasPrendas, prenda.getId(),
                    txtNombre.getText().trim(), txtTipo.getText().trim(), txtColor.getText().trim());
            if (!editada) {
                JOptionPane.showMessageDialog(this,
                        "No se ha podido guardar el cambio en la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            prenda.setRutaFoto(rutaFoto[0]);
            gestorPrendas.guardarFoto(prenda);
            mostrarPrendas(todasLasPrendas); // refrescar la lista
            actualizarDesplegables();        // refrescar desplegables
            panelFiltro.actualizarPrendas(todasLasPrendas); // actualizar el filtro con la prenda editada
        }
    }

    // PESTAÑA "PERFIL"
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