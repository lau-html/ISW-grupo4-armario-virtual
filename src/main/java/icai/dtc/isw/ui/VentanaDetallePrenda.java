package icai.dtc.isw.ui;
import icai.dtc.isw.Prenda;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


// Se crea una ventana emergente (JDialog) independiente para mostrar la prenda en detalle y evitar conflictos en Git con JVentana.java.
public class VentanaDetallePrenda extends JDialog {

    private Prenda prenda;
    private JButton btnFavorito;

    // El constructor recibe directamente un objeto Prenda para extraer y pintar toda su información actual en pantalla.
    public VentanaDetallePrenda(Prenda p) {
        this.prenda = p;

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
        btnFavorito.addActionListener(e -> {
            boolean nuevoEstado = !prenda.isEsFavorito();
            prenda.setEsFavorito(nuevoEstado);

            if (prenda.getCategoriasPersonalizadas() != null) {
                if (nuevoEstado && !prenda.getCategoriasPersonalizadas().contains("FAVORITOS")) {
                    prenda.getCategoriasPersonalizadas().add("FAVORITOS");
                    mostrarMensajeTemporal("¡Añadido a favoritos!"); // Lanza el mensaje aquí
                } else if (!nuevoEstado) {
                    prenda.getCategoriasPersonalizadas().remove("FAVORITOS");
                    mostrarMensajeTemporal("Eliminado de favoritos"); // avisar al quitarlo de favoritos
                }
            }
            actualizarAspectoCorazon();
        });

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

        // Temporizador ajustado a 2000 ms (2 segundos)
        Timer timer = new Timer(2000, evt -> dialogMensaje.dispose());
        timer.setRepeats(false);
        timer.start();
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

            VentanaDetallePrenda ventana = new VentanaDetallePrenda(prendaFicticia);
            ventana.setVisible(true);
        });
    }
}