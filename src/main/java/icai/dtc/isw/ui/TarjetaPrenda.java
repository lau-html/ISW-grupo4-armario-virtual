package icai.dtc.isw.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
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

    private final Prenda prenda; // la prenda que dibuja esta tarjeta
    private final JVentana ventana; // para avisarle cuando se hace clic

    public TarjetaPrenda(Prenda prenda, JVentana ventana) {
        this.prenda = prenda;
        this.ventana = ventana;

        setLayout(new BorderLayout(0, 6));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(190, 280)); // tamaño fijo (cuadricula ordenada)
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // manita al pasar por encima
        setSeleccionada(false); 

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
        panelTextos.setBackground(Color.WHITE);
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

    // Marca la tarjeta con borde en rosita si está seleccionada
    public void setSeleccionada(boolean seleccionada) {
        if (seleccionada) {
            setBorder(BorderFactory.createLineBorder(Tema.ROSA, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(new Color(235, 225, 218), 1));
        }
    }

    public Prenda getPrenda() {
        return prenda;
    }
}