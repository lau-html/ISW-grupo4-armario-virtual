package icai.dtc.isw.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import icai.dtc.isw.TipoPrenda;
import icai.dtc.isw.VisualizarNumPrendas;
import icai.dtc.isw.domain.Usuario;


// VENTANA PRINCIPAL DE dressB: se abre después de iniciar sesión.
public class JVentana extends JFrame {

    // colores de la app (ahora se leen de Tema)
    public static final Color CREMA = Tema.CREMA; // fondo
    public static final Color ROSA = Tema.ROSA; // "dress" del logo y selección
    public static final Color BURDEOS = Tema.BURDEOS; // "B" del logo, botones y títulos
    public static final Color LIMA = Tema.LIMA; // detalles
    public static final Color GRIS_TEXTO = Tema.GRIS_TEXTO; // textos secundarios

    private JPanel panelPrendas; // zona central de "Mi armario" donde se muestran las prendas
    private JList<Prenda> listaPrendas; // lista visible (para saber qué prenda está seleccionada)
    private List<Prenda> prendasActuales = new ArrayList<>(); // prendas que se están mostrando (pueden ser todas o solo filtradas)
    private List<Prenda> todasLasPrendas = new ArrayList<>(); // todas las prendas usuario
    private final GestorPrendas gestorPrendas = new GestorPrendas(); // lógica de editar y eliminar
    private PanelFiltroCategorias panelFiltro; // filtro de categorias, hay que actualizarlo cuando cambian las prendas para que vea las nuevas categorias
    private final GestorCategoriasPers gestorCategoriasPers = new GestorCategoriasPers(); // mismo gestor de categorias para el filtro y el detalle

    // SPRINT 2 (bonus): desplegable para ordenar por antigüedad - Lau: lo cambio a boton para que cuadre con la estetica
    private final JButton btnOrden = crearBoton("Más recientes"); // el texto es el orden elegido

    // Componentes de la pestaña Outfits (Creación flexible + Edición + Favoritos)
    private JTextField txtNombreOutfit = new JTextField();
    private JComboBox<Prenda> cbArriba = new JComboBox<>();
    private JComboBox<Prenda> cbAbajo = new JComboBox<>();
    private JComboBox<Prenda> cbCalzado = new JComboBox<>();
    private JComboBox<Prenda> cbSelectorAccesorios = new JComboBox<>();

    // Lista visual para soportar VARIOS accesorios
    private DefaultListModel<Prenda> modeloAccesoriosSeleccionados = new DefaultListModel<>();
    private JList<Prenda> listaAccesoriosSeleccionados = new JList<>(modeloAccesoriosSeleccionados);

    private JCheckBox chkFavorito = new JCheckBox("Marcar como favorito");
    private JButton btnGuardarOutfit;

    // Controla si estamos creando (null) o editando un outfit existente
    private Outfit outfitEnEdicion = null;

    private DefaultListModel<Outfit> modeloOutfits = new DefaultListModel<>();
    private JList<Outfit> listaOutfits = new JList<>(modeloOutfits);

    // SPRINT 3 (Cristina): sección de outfits favoritos
    private final List<Outfit> todosLosOutfits = new ArrayList<>(); // todos los outfits del usuario, sin filtrar
    private boolean soloFavoritos = false; // true = la lista solo enseña los favoritos
    private JButton btnFavoritosOutfits; // botón "Todos / ★ Favoritos" encima de la lista

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
        pestanas.setUI(new BasicTabbedPaneUI() { // creo una ui para poder ir cambiando sus metodos - los pongo como protected para q solo accedan los del mismo paquete accedan - UI
            @Override
            protected void installDefaults() { 
                super.installDefaults(); //pongo defaults y luego cambio
                tabInsets = new Insets(8, 18, 10, 18); // espacio alrededor del texto
                selectedTabPadInsets = new Insets(0, 0, 0, 0); // quito que la pestaña activa se "levante" respecto al resto
                contentBorderInsets = new Insets(0, 0, 0, 0); // sin marco del contenido
            }

            // los metodos de pintar los dejo vacios
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {} // sin fondo

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {} // sin borde

            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {} // sin recuadro de foco

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {} // sin marco

            @Override // metodo para dibujar las diferentes pestanas
            protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g; // para poder suavizar los bordes, g2 da mas opciones
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // suavizado
                g2.setFont(font); // usando la font creada en tema
                if (isSelected) {
                    g2.setColor(BURDEOS); // la letra de la pestaña activa en burdeos 
                } else {
                    g2.setColor(GRIS_TEXTO); // las demas pestañas en gris (mostrando no seleccionada)
                }
                g2.drawString(title, textRect.x, textRect.y + metrics.getAscent()); // escribo el nombre de la pestaña dejandole un poco de espacio
                if (isSelected) {
                    g2.setColor(ROSA);
                    g2.fillRoundRect(textRect.x, textRect.y + textRect.height + 4, textRect.width, 3, 3, 3); // rayita rosa debajo para marcarlo
                }
            }
        });

        pestanas.setBackground(CREMA);
        pestanas.setForeground(BURDEOS);
        pestanas.setFont(Tema.getInstancia().fuenteTituloSec); // DM Sans normal, más fina que los botones // DM Sans en negrita desde Tema, tamaño 15, antes era la sansSerif
        pestanas.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // separo las pestañas de la linea verde
        pestanas.addTab("MI ARMARIO", crearPanelArmario());
        pestanas.addTab("OUTFITS", crearPanelOutfits());
        pestanas.addTab("PERFIL", crearPanelPerfil());
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

    private void recargarOutfitsDesdeBD() {
        modeloOutfits.clear();
        Usuario usuario = SesionUsuario.getUsuarioLogueado();
        if (usuario == null) {
            return;
        }

        GestorOutfits gestor = new GestorOutfits();
        List<Outfit> guardados = gestor.cargarOutfitsUsuario(usuario.getId(), todasLasPrendas);
        todosLosOutfits.clear();
        todosLosOutfits.addAll(guardados);
        mostrarOutfits();
    }

    // SPRINT 3 (Cristina): pinta en la lista todos los outfits o solo los favoritos, según el botón
    private void mostrarOutfits() {
        modeloOutfits.clear();
        List<Outfit> aMostrar = soloFavoritos
                ? new GestorOutfits().filtrarFavoritos(todosLosOutfits)
                : todosLosOutfits;
        for (Outfit o : aMostrar) {
            modeloOutfits.addElement(o);
        }
    }

    // PESTAÑA "CREADOR Y EDITOR DE OUTFITS"
    private JPanel crearPanelOutfits() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CREMA);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.setBackground(CREMA);

        JLabel lblTitulo = new JLabel("Diseña o edita tu outfit");
        lblTitulo.setFont(Tema.getInstancia().crearFuente(Font.BOLD, 16));
        lblTitulo.setForeground(BURDEOS);
        barra.add(lblTitulo);
        panel.add(barra, BorderLayout.NORTH);

        // Formulario izquierdo (Nombre, Superior, Inferior, Calzado, Accesorios múltiples y Favorito)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(CREMA);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel gridCampos = new JPanel(new GridLayout(5, 2, 10, 10));
        gridCampos.setBackground(CREMA);

        gridCampos.add(new JLabel("Nombre del Look:"));
        gridCampos.add(txtNombreOutfit);

        gridCampos.add(new JLabel("Prenda Superior (opcional):"));
        gridCampos.add(cbArriba);

        gridCampos.add(new JLabel("Prenda Inferior (opcional):"));
        gridCampos.add(cbAbajo);

        gridCampos.add(new JLabel("Calzado (opcional):"));
        gridCampos.add(cbCalzado);

        // Selector + botón para ir añadiendo varios accesorios
        JPanel panelAddAcc = new JPanel(new BorderLayout(5, 0));
        panelAddAcc.setBackground(CREMA);
        JButton btnAddAcc = crearBoton("+");
        btnAddAcc.setToolTipText("Añadir accesorio al outfit");
        btnAddAcc.addActionListener(e -> {
            Prenda acc = (Prenda) cbSelectorAccesorios.getSelectedItem();
            if (acc != null && acc.getId() != -1 && !modeloAccesoriosSeleccionados.contains(acc)) {
                modeloAccesoriosSeleccionados.addElement(acc);
            }
        });
        panelAddAcc.add(cbSelectorAccesorios, BorderLayout.CENTER);
        panelAddAcc.add(btnAddAcc, BorderLayout.EAST);

        gridCampos.add(new JLabel("Añadir Accesorios:"));
        gridCampos.add(panelAddAcc);

        panelFormulario.add(gridCampos);
        panelFormulario.add(Box.createVerticalStrut(8));

        // Lista visual de accesorios añadidos (con doble clic para quitar un accesorio)
        listaAccesoriosSeleccionados.setVisibleRowCount(3);
        listaAccesoriosSeleccionados.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int idx = listaAccesoriosSeleccionados.getSelectedIndex();
                    if (idx != -1) {
                        modeloAccesoriosSeleccionados.remove(idx);
                    }
                }
            }
        });
        JScrollPane scrollAcc = new JScrollPane(listaAccesoriosSeleccionados);
        scrollAcc.setBorder(BorderFactory.createTitledBorder("Accesorios seleccionados (doble clic para quitar)"));
        panelFormulario.add(scrollAcc);

        panelFormulario.add(Box.createVerticalStrut(8));
        chkFavorito.setBackground(CREMA);
        chkFavorito.setForeground(BURDEOS);
        panelFormulario.add(chkFavorito);

        actualizarDesplegables();

        // Lista derecha: Mis Outfits Guardados (clic en papelera borra, doble clic carga para EDITAR)
        listaOutfits.setBackground(CREMA);
        listaOutfits.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Outfit o = (Outfit) value;
                JPanel panelFila = new JPanel(new BorderLayout());
                panelFila.setBackground(isSelected ? ROSA : CREMA);
                panelFila.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 225, 218)));

                String estrella = o.isFavorito() ? "★ " : "☆ "; // SPRINT 3: clic en la estrella para marcar o desmarcar
                JLabel lblTexto = new JLabel(estrella + o.toString());
                lblTexto.setForeground(BURDEOS);
                lblTexto.setFont(Tema.getInstancia().fuenteNormal);
                lblTexto.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

                JLabel lblPapelera = new JLabel("🗑️");
                lblPapelera.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                lblPapelera.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 15));
                lblPapelera.setCursor(new Cursor(Cursor.HAND_CURSOR));

                panelFila.add(lblTexto, BorderLayout.CENTER);
                panelFila.add(lblPapelera, BorderLayout.EAST);
                return panelFila;
            }
        });

        listaOutfits.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int index = listaOutfits.locationToIndex(e.getPoint());
                if (index != -1) {
                    Outfit outfitSeleccionado = modeloOutfits.getElementAt(index);

                    // Borrar si pulsa la papelera
                    if (e.getX() > listaOutfits.getWidth() - 40) {
                        int confirmar = JOptionPane.showConfirmDialog(JVentana.this,
                                "¿Borrar el look '" + outfitSeleccionado.getNombre() + "'?",
                                "Confirmar borrado", JOptionPane.YES_NO_OPTION);
                        if (confirmar == JOptionPane.YES_OPTION) {
                            GestorOutfits gestor = new GestorOutfits();
                            if (gestor.eliminarOutfit(outfitSeleccionado.getId())) {
                                modeloOutfits.remove(index);
                                if (outfitEnEdicion != null && outfitEnEdicion.getId() == outfitSeleccionado.getId()) {
                                    limpiarFormularioOutfit();
                                }
                            }
                        }
                        return;
                    }

                    // SPRINT 3 (Cristina): clic en la estrella (a la izquierda) para marcar o desmarcar como favorito
                    if (e.getX() < 30 && e.getClickCount() == 1) {
                        GestorOutfits gestor = new GestorOutfits();
                        if (gestor.cambiarFavorito(outfitSeleccionado)) {
                            mostrarOutfits(); // si estoy viendo solo favoritos y lo desmarco, desaparece de la lista
                        } else {
                            JOptionPane.showMessageDialog(JVentana.this,
                                    "No se ha podido guardar el favorito en la base de datos.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        return;
                    }

                    // Doble clic: cargar el outfit en el formulario para EDITARLO
                    if (e.getClickCount() == 2) {
                        cargarOutfitParaEditar(outfitSeleccionado);
                    }
                }
            }
        });

        JScrollPane scrollOutfits = new JScrollPane(listaOutfits);
        scrollOutfits.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ROSA), "Mis Outfits (☆ marcar favorito · doble clic editar)",
                javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,

        Tema.getInstancia().crearFuente(Font.BOLD, 14), BURDEOS));
        
        scrollOutfits.getViewport().setBackground(CREMA);
        scrollOutfits.setPreferredSize(new java.awt.Dimension(260, 0));

        JPanel panelCentralDividido = new JPanel(new BorderLayout(15, 0));
        panelCentralDividido.setBackground(CREMA);
        panelCentralDividido.add(panelFormulario, BorderLayout.CENTER);
        // SPRINT 3 (Cristina): botón para ver todos los outfits o solo los favoritos, encima de la lista
        btnFavoritosOutfits = crearBoton("Ver: Todos");
        btnFavoritosOutfits.setToolTipText("Mostrar todos los outfits o solo los favoritos");
        btnFavoritosOutfits.addActionListener(e -> {
            soloFavoritos = !soloFavoritos;
            btnFavoritosOutfits.setText(soloFavoritos ? "Ver: ★ Favoritos" : "Ver: Todos");
            mostrarOutfits();
        });
        JPanel panelListaOutfits = new JPanel(new BorderLayout(0, 6));
        panelListaOutfits.setBackground(CREMA);
        panelListaOutfits.add(btnFavoritosOutfits, BorderLayout.NORTH);
        panelListaOutfits.add(scrollOutfits, BorderLayout.CENTER);
        panelCentralDividido.add(panelListaOutfits, BorderLayout.EAST);
        panel.add(panelCentralDividido, BorderLayout.CENTER);

        // Barra inferior: Guardar / Guardar Cambios + Cancelar edición
        JPanel barraInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        barraInferior.setBackground(CREMA);
        btnGuardarOutfit = crearBoton("Guardar Outfit");
        JButton btnLimpiar = crearBoton("Nuevo / Limpiar");

        btnLimpiar.addActionListener(e -> limpiarFormularioOutfit());

        btnGuardarOutfit.addActionListener(e -> {
            Usuario usuario = SesionUsuario.getUsuarioLogueado();
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "Debes iniciar sesión para guardar outfits.");
                return;
            }

            String nombre = txtNombreOutfit.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, escribe un nombre para el look.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Prenda arriba = obtenerPrendaReal(cbArriba);
            Prenda abajo = obtenerPrendaReal(cbAbajo);
            Prenda calzado = obtenerPrendaReal(cbCalzado);

            List<Prenda> accesorios = new ArrayList<>();
            for (int i = 0; i < modeloAccesoriosSeleccionados.size(); i++) {
                accesorios.add(modeloAccesoriosSeleccionados.getElementAt(i));
            }

            // Reutilizamos outfitEnEdicion si existe, o creamos uno nuevo
            Outfit outfitAGuardar = (outfitEnEdicion != null) ? outfitEnEdicion : new Outfit();
            outfitAGuardar.setNombre(nombre);
            outfitAGuardar.setFavorito(chkFavorito.isSelected());
            outfitAGuardar.setUsuarioId(usuario.getId()); // Propietario actual

            try {
                GestorOutfits gestor = new GestorOutfits();
                System.out.println("DEBUG OUTFIT -> Arriba: " + arriba + " | Abajo: " + abajo + " | Calzado: " + calzado + " | Acc: " + accesorios.size());
                if (gestor.validarYGuardarOutfit(outfitAGuardar, arriba, abajo, calzado, accesorios)) {
                    JOptionPane.showMessageDialog(this, outfitEnEdicion == null ? "¡Outfit creado con éxito!" : "¡Outfit actualizado con éxito!");
                    limpiarFormularioOutfit();
                    recargarOutfitsDesdeBD();
                } else {
                    JOptionPane.showMessageDialog(this, "Selecciona al menos una prenda para guardar el outfit.", "Outfit vacío", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                // Muestra el error de validación de categoría (ej. falda en accesorio)
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Categoría incorrecta", JOptionPane.ERROR_MESSAGE);
            }
        });

        barraInferior.add(btnGuardarOutfit);
        barraInferior.add(btnLimpiar);
        panel.add(barraInferior, BorderLayout.SOUTH);

        recargarOutfitsDesdeBD();
        return panel;
    }

    // Carga los datos de un outfit existente en el formulario para editarlo
    public void cargarOutfitParaEditar(Outfit outfit) {
        limpiarFormularioOutfit();
        this.outfitEnEdicion = outfit;
        txtNombreOutfit.setText(outfit.getNombre());
        chkFavorito.setSelected(outfit.isFavorito());

        for (Prenda p : outfit.getPrendas()) {
            String tipo = p.getTipo() != null ? p.getTipo().toLowerCase() : "";
            if (tipo.matches(".*(arriba|superior|camiseta|camisa|jersey|sudadera|top|chaqueta|abrigo).*")) {
                cbArriba.setSelectedItem(p);
            } else if (tipo.matches(".*(abajo|inferior|pantalón|pantalon|falda|vaqueros|short|bermudas).*")) {
                cbAbajo.setSelectedItem(p);
            } else if (tipo.matches(".*(calzado|zapatos|zapatillas|botas|sandalias|tacones).*")) {
                cbCalzado.setSelectedItem(p);
            } else {
                modeloAccesoriosSeleccionados.addElement(p);
            }
        }
        btnGuardarOutfit.setText("Guardar Cambios");
    }

    private void limpiarFormularioOutfit() {
        this.outfitEnEdicion = null;
        txtNombreOutfit.setText("");
        chkFavorito.setSelected(false);
        if (cbArriba.getItemCount() > 0) cbArriba.setSelectedIndex(0);
        if (cbAbajo.getItemCount() > 0) cbAbajo.setSelectedIndex(0);
        if (cbCalzado.getItemCount() > 0) cbCalzado.setSelectedIndex(0);
        if (cbSelectorAccesorios.getItemCount() > 0) cbSelectorAccesorios.setSelectedIndex(0);
        modeloAccesoriosSeleccionados.clear();
        if (btnGuardarOutfit != null) btnGuardarOutfit.setText("Guardar Outfit");
    }

    private Prenda obtenerPrendaReal(JComboBox<Prenda> combo) {
        Prenda p = (Prenda) combo.getSelectedItem();
        if (p == null || p.getId() == -999 || "-- Ninguna --".equals(p.getNombre())) {
            return null;
        }
        return p;
    }

    // Actualiza los desplegables incluyendo la opción "-- Ninguna --" para que sean opcionales
    public void actualizarDesplegables() {
        cbArriba.removeAllItems();
        cbAbajo.removeAllItems();
        cbCalzado.removeAllItems();
        cbSelectorAccesorios.removeAllItems();

        Prenda ninguna = new Prenda();
        ninguna.setId(-999);
        ninguna.setNombre("-- Ninguna --");

        cbArriba.addItem(ninguna);
        cbAbajo.addItem(ninguna);
        cbCalzado.addItem(ninguna);
        cbSelectorAccesorios.addItem(ninguna);

        for (Prenda p : todasLasPrendas) {
            cbArriba.addItem(p);
            cbAbajo.addItem(p);
            cbCalzado.addItem(p);
            cbSelectorAccesorios.addItem(p);
        }
    }


    // se llama desde el detalle cuando cambian las categorias de una prenda o se crea una categoria nueva, le mando refrescar los filtrados y prendas visibles
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
            lblVacio.setFont(Tema.getInstancia().fuenteNormal);
            lblVacio.setForeground(GRIS_TEXTO);
            panelPrendas.add(lblVacio, BorderLayout.CENTER);
        } else {
            DefaultListModel<Prenda> modelo = new DefaultListModel<>();
            for (Prenda prenda : prendas) {
                modelo.addElement(prenda);
            }
            JList<Prenda> lista = new JList<>(modelo);
            lista.setFont(Tema.getInstancia().fuenteNormal);
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
        JComboBox<TipoPrenda> cbTipo = new JComboBox<>(TipoPrenda.values());
        cbTipo.setSelectedItem(prenda.getTipoPrenda());
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
        formulario.add(new JLabel("Tipo:"));   formulario.add(cbTipo);
        formulario.add(new JLabel("Color:"));  formulario.add(txtColor);
        formulario.add(new JLabel("Foto:"));   formulario.add(panelFoto);

        int respuesta = JOptionPane.showConfirmDialog(this, formulario,
                "Editar prenda", JOptionPane.OK_CANCEL_OPTION);
        if (respuesta == JOptionPane.OK_OPTION) {
            // SPRINT 2: el cambio se guarda también en la base de datos
            boolean editada = gestorPrendas.editarPrendaGuardada(todasLasPrendas, prenda.getId(),
                    txtNombre.getText().trim(), ((TipoPrenda) cbTipo.getSelectedItem()).getEtiqueta(), txtColor.getText().trim());
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
        lblTitulo.setFont(Tema.getInstancia().crearFuente(Font.BOLD, 20));;
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

    // Crea un botón con el estilo de dressB (lo hace Tema, para que todos los botones sean iguales)
    private JButton crearBoton(String texto) {
        return Tema.getInstancia().crearBoton(texto); // le pido el botón a Tema
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