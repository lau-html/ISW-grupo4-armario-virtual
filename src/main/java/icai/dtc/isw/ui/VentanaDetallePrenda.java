package icai.dtc.isw.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import icai.dtc.isw.GestorCategoriasPers;
import icai.dtc.isw.GestorPrendas;
import icai.dtc.isw.Prenda;



// Se crea una ventana emergente (JDialog) independiente para mostrar la prenda en detalle y evitar conflictos en Git con JVentana.java.
public class VentanaDetallePrenda extends JDialog {

    private Prenda prenda;
    private JButton btnFavorito;
    // Laura -> añado botones de asignar categorias personalizadas a la prenda y quitarlo, esta logica no la habia hecho nadie
    private JButton btnCategorias; // boton para asignar o quitar categorias personalizadas a la prenda
    private JPopupMenu menuCategorias = new JPopupMenu(); // submenu con las categorias disponibles, se abre al pulsar el boton
    private GestorCategoriasPers gestorPers; // gestor de categorias (compartido) es el mismo que usa el panel de filtros
    private final GestorPrendas gestorPrendas = new GestorPrendas(); // para guardar los cambios en la BD
    private Runnable alCambiarCategorias; // un aviso a la ventana principal que la avisara para repintar filtros y prendas cuando algo cambie aqui


    // El constructor recibe directamente un objeto Prenda para extraer y pintar toda su información actual en pantalla.
    public VentanaDetallePrenda(Prenda p, GestorCategoriasPers gestorPers, Runnable alCambiarCategorias) {
        this.prenda = p;
        this.gestorPers = gestorPers;
        this.alCambiarCategorias = alCambiarCategorias;

        // Configuración básica de la ventana con el nombre unificado dressB
        setTitle("dressB - Detalle de Prenda");
        setSize(420, 550);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(15, 15));

        // 1. ZONA SUPERIOR: Botón de Favoritos
        // Incluye el botón de favoritos con forma de corazón para marcar y desmarcar la prenda.
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFavorito = new JButton();
        btnFavorito.setFont(new Font("SansSerif", Font.BOLD, 24));
        btnFavorito.setFocusPainted(false);
        btnFavorito.setContentAreaFilled(false);
        btnFavorito.setBorderPainted(false);
        btnFavorito.setCursor(new Cursor(Cursor.HAND_CURSOR));

        actualizarAspectoCorazon();

        // Al pulsar el corazón, se cambia el estado booleano esFavorito y se añade o quita de la categoría FAVORITOS.
        // Laura: esta logica la hace el gestor (marcarFavorito), no el boton (UI) para poder probarla y testearla sin depender de la UI
        btnFavorito.addActionListener(e -> {
            boolean nuevoEstado = !prenda.isEsFavorito();
            gestorPers.marcarFavorito(prenda, nuevoEstado); // en vez de cambiar con un set, llamo al gestor que hace esa logica
            if (nuevoEstado) {
                mostrarMensajeTemporal("¡Añadido a favoritos!"); // Lanza el mensaje aquí
            } else {
                mostrarMensajeTemporal("Eliminado de favoritos"); // avisar al quitarlo de favoritos
            }
            actualizarAspectoCorazon();
            gestorPrendas.guardarCategoriasYFavorito(prenda); // lo guardo tambien en la BD
            alCambiarCategorias.run(); // aviso al filtro tanto al marcar como al desmarcar
        });

        // boton de categorias personalizadas, permite abrir un submenu para asignar/quitar las de esta prenda y para crear nuevas
        btnCategorias = new JButton("Categorías");
        btnCategorias.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCategorias.setFocusPainted(false);
        btnCategorias.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCategorias.addActionListener(e -> {
            rellenarMenuCategorias(); // lo relleno justo al abrir para que salgan las categorias y cambios de ahora
            menuCategorias.show(btnCategorias, 0, btnCategorias.getHeight());
        });

        panelSuperior.add(btnCategorias);
        panelSuperior.add(btnFavorito);
        add(panelSuperior, BorderLayout.NORTH);

        // 2. ZONA CENTRAL: Foto de la prenda en grande
        // Carga y escala la imagen desde la rutaFoto acordada en Prenda.java para mostrarla en grande.
        JLabel lblImagen = new JLabel("", SwingConstants.CENTER);
        cargarImagenEnGrande(p.getRutaFoto(), lblImagen);
        add(lblImagen, BorderLayout.CENTER);

        // 3. ZONA INFERIOR: Toda la información que antes salía en el listado principal
        JPanel panelInfo = new JPanel(new GridLayout(0, 1, 5, 5));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));

        panelInfo.add(crearEtiquetaDato("Nombre: ", p.getNombre()));
        panelInfo.add(crearEtiquetaDato("Tipo: ", p.getTipo()));
        panelInfo.add(crearEtiquetaDato("Color: ", p.getColor()));
        panelInfo.add(crearEtiquetaDato("Estilo: ", p.getEstilo()));
        panelInfo.add(crearEtiquetaDato("Marca: ", p.getMarca()));
        panelInfo.add(crearEtiquetaDato("Temporada: ", p.getTemporada()));
        panelInfo.add(crearEtiquetaDato("Descripción: ", p.getDescripcion()));

        add(panelInfo, BorderLayout.SOUTH);
    }

    private JLabel crearEtiquetaDato(String titulo, String valor) {
        JLabel label = new JLabel("<html><b>" + titulo + "</b>" + (valor != null ? valor : "No especificado") + "</html>");
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return label;
    }

    private void actualizarAspectoCorazon() {
        if (prenda.isEsFavorito()) {
            btnFavorito.setText("♥");
            btnFavorito.setForeground(Color.RED);
            btnFavorito.setToolTipText("Quitar de Favoritos");
        } else {
            btnFavorito.setText("♡");
            btnFavorito.setForeground(Color.GRAY);
            btnFavorito.setToolTipText("Añadir a Favoritos");
        }
    }

    private void cargarImagenEnGrande(String ruta, JLabel etiquetaDestino) {
        if (ruta != null && !ruta.isEmpty() && new File(ruta).exists()) {
            ImageIcon iconoOriginal = new ImageIcon(ruta);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(260, 260, Image.SCALE_SMOOTH);
            etiquetaDestino.setIcon(new ImageIcon(imagenEscalada));
        } else {
            etiquetaDestino.setText("[ Sin foto disponible ]");
            etiquetaDestino.setPreferredSize(new Dimension(260, 260));
            etiquetaDestino.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
    }

    // Muestra un mensaje emergente temporal que desaparece solo a los 3 segundos
    // Muestra un mensaje emergente centrado, semitransparente, con bordes redondeados y de 2 segundos de duración
    private void mostrarMensajeTemporal(String mensaje) {
        JDialog dialogMensaje = new JDialog(this, false);
        dialogMensaje.setUndecorated(true);
        // Hacemos que el fondo base de la ventana sea totalmente transparente para que se vean las esquinas redondeadas
        dialogMensaje.setBackground(new Color(0, 0, 0, 0));

        // Panel personalizado que dibuja el fondo burdeos semitransparente y redondeado
        JPanel panelRedondeado = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Suavizado de bordes (Antialiasing) para que la curva se vea nítida
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Mismo tono BURDEOS (87, 6, 45) con canal alfa a 210 (sobre 255) para darle transparencia
                g2.setColor(new Color(87, 6, 45, 210));
                // Dibuja el rectángulo con un radio de curvatura de 25px en las esquinas
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };
    
        panelRedondeado.setOpaque(false);
        panelRedondeado.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setForeground(new Color(251, 246, 241)); // Color CREMA para el texto
        lblMensaje.setFont(new Font("SansSerif", Font.BOLD, 14));

        panelRedondeado.add(lblMensaje, BorderLayout.CENTER);
        dialogMensaje.setContentPane(panelRedondeado);
        dialogMensaje.pack();

        // Lo coloca exactamente en el centro de la ventana emergente de la prenda
        dialogMensaje.setLocationRelativeTo(this);
        dialogMensaje.setVisible(true);

        // Temporizador ajustado a 1500 ms (1,5 segundos)
        Timer timer = new Timer(1500, evt -> dialogMensaje.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    // rellena el menu con una opcion por cada categoria del gestor que comparten, las que ya tiene la prenda salen marcadas
    private void rellenarMenuCategorias() {
        menuCategorias.removeAll();
        List<String> asignadas = prenda.getCategoriasPersonalizadas(); // puede ser null si la prenda aun no tiene lista
        for (String nombre : gestorPers.getCategorias()) {
            if ("FAVORITOS".equals(nombre)) {
                continue; // FAVORITOS se gestiona solo con el corazon, no desde este menu, para que no se desincronicen
            }
            boolean asignada = asignadas != null && asignadas.contains(nombre);
            String textoOpcion;
            if (asignada) {
                textoOpcion = "✓ " + nombre; // tick delante si la prenda tiene esa categoria asignada
            } else {
                textoOpcion = nombre;
            }
            JMenuItem opcion = new JMenuItem(textoOpcion);

            opcion.addActionListener(e -> alternarCategoria(nombre));
            menuCategorias.add(opcion);
        }
        menuCategorias.addSeparator();
        JMenuItem nueva = new JMenuItem("Nueva categoría..."); // crea el nombre en el gestor para que luego se pueda asignar
        nueva.addActionListener(e -> crearCategoria()); // abre un dialogo para pedir el nombre y lo crea en el gestor, si ya existe, aviso de duplicado
        menuCategorias.add(nueva);
    }

    // asigna la categoria a la prenda si no la tenia, o se la quita si ya la tenia (alterna) , y aviso para refrescar; la operacion sobre la lista la hace el gestor, no la ventana
    private void alternarCategoria(String nombre) {
        List<String> asignadas = prenda.getCategoriasPersonalizadas();
        if (asignadas != null && asignadas.contains(nombre)) {
            gestorPers.quitarCategoria(prenda, nombre);
        } else {
            gestorPers.asignarCategoria(prenda, nombre);
        }
        gestorPrendas.guardarCategoriasYFavorito(prenda); // lo guardo tambien en la BD
        alCambiarCategorias.run(); // aviso a la ventana principal para que refresque el panel de filtros y las prendas
    }

    // pide el nombre y lo crea en el gestor, si esta repetido, el aviso de repeticion lo muestra la ventana (no el gestor donde estaba antes)
    private void crearCategoria() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la nueva categoría:");
        if (nombre == null) {
            return; // cancela
        }
        if (gestorPers.anadirCategoria(nombre)) {
            alCambiarCategorias.run(); // para que aparezca en los filtros aunque aun no tenga prendas
        } else if (!nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La categoría '" + nombre.trim() + "' ya existe.",
                "Advertencia de Duplicado",
                JOptionPane.WARNING_MESSAGE);
        }
    }



    // Metodo main independiente para probar la ventana pasándole una Prenda ficticia sin depender de la base de datos ni del resto del equipo.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Se instancia un objeto Prenda de prueba (mock) con datos inventados
            Prenda prendaFicticia = new Prenda();
            prendaFicticia.setDescripcion("Cazadora vaquera oversize");
            prendaFicticia.setMarca("Zara");
            prendaFicticia.setColor("Azul claro");
            prendaFicticia.setEstilo("Casual");
            prendaFicticia.setTemporada("Otoño");
            prendaFicticia.setRutaFoto("ruta/de/prueba.jpg"); // Pon la ruta de una foto real de tu PC para probar
            prendaFicticia.setEsFavorito(false);
            prendaFicticia.setCategoriasPersonalizadas(new ArrayList<>(List.of("Otoño")));

            GestorCategoriasPers gestorDemo = new GestorCategoriasPers(); // gestor solo para esta prueba de la ventana
            gestorDemo.anadirCategoria("Otoño"); // la prenda ficticia ya tiene esta categoria

            VentanaDetallePrenda ventana = new VentanaDetallePrenda(prendaFicticia, gestorDemo, () -> {
                // aqui no hay ventana principal que refrescar
            });
            ventana.setVisible(true);
        });
    }
}