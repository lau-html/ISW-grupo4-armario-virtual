package icai.dtc.isw.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import icai.dtc.isw.Prenda;


// Una tarjeta del armario como panel - se puede ver la foto arriba y nombre y datos debajo
public class TarjetaPrenda extends JPanel {

    // tamaños maximos foto
    private static final int ANCHO_FOTO = 170; 
    private static final int ALTO_FOTO = 200;
    private static final Color CREMA_CLARO = new Color(255, 251, 248);
 // fondo de la tarjeta, crema mas claro que el de la ventana para que de un efecto de semiopacidad
    private static final int REDONDEO = 24; // cuanto voy a redondear las esquinas (cuadricula con bordes redondeados para mayor limpieza)

    private final Prenda prenda; // la prenda que dibuja esta tarjeta
    private final JVentana ventana; // para avisarle cuando se hace clic
    private boolean seleccionada = false; // true = dibuja el borde rosa

    public TarjetaPrenda(Prenda prenda, JVentana ventana) {
        this.prenda = prenda;
        this.ventana = ventana;

        setLayout(new BorderLayout(0, 6));
        setOpaque(false); // sin fondo cuadrado, voy a pintar otro fondo con esquinas redondeadas directamente
        setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8)); // margen por dentro
        setPreferredSize(new Dimension(190, 280)); // tamaño fijo cuadricula
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // manita al pasar por encima

        add(crearFoto(), BorderLayout.CENTER);
        add(crearTextos(), BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TarjetaPrenda.this.ventana.abrirDetallePrenda(TarjetaPrenda.this.prenda); // doble clic - abre la tarjeta con detalles
                } else {
                    TarjetaPrenda.this.ventana.seleccionarPrenda(TarjetaPrenda.this.prenda); // un clic - la selecciona
                }
            }
        });
    }

    // Foto de la prenda ESCALADA, SIN QUE SE DEFORME RESPECTO LA ORIGINAL (si no hay, pone no_pic.jpg)
    private JLabel crearFoto() {
        JLabel lblFoto = new JLabel("Sin foto", SwingConstants.CENTER); // por si no carga la foto de resources 
        lblFoto.setFont(Tema.getInstancia().fuenteNormal);
        lblFoto.setForeground(Tema.GRIS_TEXTO);

        ImageIcon icono = null; // la imagen que se va a pintar
        if (prenda.getRutaFoto() != null) {
            icono = new ImageIcon(prenda.getRutaFoto()); // cargar la foto de la prenda
        }
        if (icono == null || icono.getIconWidth() <= 0) { // no hay foto o no se ha podido cargar
            URL urlNoFoto = getClass().getResource("/no_pic.jpg"); // busca la foto por defecto si no existe imagen en resources
            if (urlNoFoto != null) { // si la foto existe la pone
                icono = new ImageIcon(urlNoFoto);
            }
        }

        if (icono != null && icono.getIconWidth() > 0) { // solo si hay una imagen valida (width > 0)
            double escala = Math.min((double) ANCHO_FOTO / icono.getIconWidth(), (double) ALTO_FOTO / icono.getIconHeight()); // calculo de cuanto hay que encoger, el ancho o alto de la foto entre los limites impuestos
            int anchoNuevo = (int) (icono.getIconWidth() * escala);
            int altoNuevo = (int) (icono.getIconHeight() * escala);
            Image imagen = icono.getImage().getScaledInstance(anchoNuevo, altoNuevo, Image.SCALE_SMOOTH);
            lblFoto.setText(""); // quito el texto de sin foto
            lblFoto.setIcon(new ImageIcon(imagen)); // pongo la nueva imagen
        }
        return lblFoto;
    }

    // Nombre en negrita y debajo tipo y color
    private JPanel crearTextos() {
        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setOpaque(false); // transparente, antes lo puse blanco pero ahora quiero que se vea el fondo de la tarjeta redondeada crema
        panelTextos.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JLabel lblNombre = new JLabel(prenda.getNombre());
        lblNombre.setFont(Tema.getInstancia().crearFuente(Font.BOLD, 14));
        lblNombre.setForeground(Tema.BURDEOS);

        JLabel lblDatos = new JLabel(prenda.getTipo() + " - " + prenda.getColor());
        lblDatos.setFont(Tema.getInstancia().fuenteNormal);
        lblDatos.setForeground(Tema.GRIS_TEXTO);

        panelTextos.add(lblNombre);
        panelTextos.add(lblDatos);
        return panelTextos;
    }

    // Dibujo un fondo para la tarjeta en color crema, esquinas redondeadas
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create(); // copia 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // bordes suaves
        g2.setColor(CREMA_CLARO);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, REDONDEO, REDONDEO); // cuadrado esquinas redondeadas
        if (seleccionada) {
            g2.setColor(Tema.ROSA); // si la tarjeta esta seleccionada
            g2.setStroke(new BasicStroke(3)); // ajustes anchura borde 
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, REDONDEO, REDONDEO); // borde rosa alrededor
        }
        g2.dispose();
    }

    // Marca la tarjeta con borde en rosita si está seleccionada
    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada; // me guardo si esta marcada (true o false) para dibujar o quitar el borde
        repaint(); // vuelvo a dibujar la tarjeta
    }

    public Prenda getPrenda() {
        return prenda;
    }
}