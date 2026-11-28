package icai.dtc.isw.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Este es el TEMA de dressB:
// Incluye sus colores, fuentes y estilos de botones y tarjetas (centralización de los métodos de creación de los componentes de la UI)
// Incorporo el patron tipo Singleton: una sola instancia para toda la app
// La app necesita una única fuente de colores y fuentes, accesible desde todas las ventanas, no aislado

public class Tema {

    private static Tema instanciaTema; // la unica instancia
    private static final Font FUENTE_NORMAL_DMSANS = cargarFuente("/DMSans_18pt-Medium.ttf");
    private static final Font FUENTE_BOLD_DMSANS = cargarFuente("/DMSans_18pt-Bold.ttf");
    private static final Font FUENTE_GASOEK = cargarFuente("/GasoekOne-Regular.ttf"); // la del logo

    // colores de la app 
    public static final Color CREMA = new Color(251, 246, 241); // fondo
    public static final Color ROSA = new Color(220, 135, 202); // "dress" del logo y selección
    public static final Color BURDEOS = new Color(87, 6, 45); // "B" del logo, botones y títulos
    public static final Color LIMA = new Color(225, 236, 159); // detalles
    public static final Color GRIS_TEXTO = new Color(120, 110, 115); // textos secundarios
    public static final Color GRIS_BOTON = new Color(238, 238, 238); // fondo botones de filtrado
    public static final Color TEXTO_OSCURO = new Color(40, 40, 40); // texto botones de filtrado

    // fuentes de la app (letras normales), son publicas para poder acceder desde fuera
    public final Font fuenteNormal = crearFuente(Font.PLAIN, 14);
    public final Font fuenteNegrita = crearFuente(Font.BOLD, 13);
    public final Font fuenteTitulo = crearFuenteTitulo(15); // Gasoek One a tamaño 15 para titulos
    public final Font fuenteTituloSec = crearFuenteTituloSec(15);

    private Tema() {
        // constructor privado para que nadie cree otra instancia - Singleton
    }

    // devuelve siempre la misma instancia (la crea la primera vez y nadie más puede crear otra) - Singleton
    public static Tema getInstancia() {
        if (instanciaTema == null) {
            instanciaTema = new Tema();
        }
        return instanciaTema;
    }

    public Font crearFuente(int estilo, float tamano) {
        Font fuentePrincipal; // la fuente basica de la que parto para guardar estilo/tamaño
        if (estilo == Font.BOLD) {
            fuentePrincipal = FUENTE_BOLD_DMSANS; // negrita
        } else {
            fuentePrincipal = FUENTE_NORMAL_DMSANS; // normal
        }
        return fuentePrincipal.deriveFont(Font.PLAIN, tamano); // el grosor ya lo da el archivo
    }

    public Font crearFuenteTitulo(float tamano) {
        return FUENTE_GASOEK.deriveFont(Font.PLAIN, tamano);
    }

    public Font crearFuenteTituloSec(float tamano) {
        Map<TextAttribute, Object> atributos = new HashMap<>(); // guardo ajustes extra a la fuente (le quiero meter separacion)
        atributos.put(TextAttribute.TRACKING, 0.04); // separación entre letras (un 4% del tamaño)
        return FUENTE_GASOEK.deriveFont(Font.PLAIN, tamano).deriveFont(atributos); // primero tamaño y luego separación

    }

    private static Font cargarFuente(String rutaFuente) {
        try (InputStream fuente = Tema.class.getResourceAsStream(rutaFuente)) {
            if (fuente == null) {
                System.out.println("No se encontró la fuente " + rutaFuente + ", se usará SansSerif");
                return new Font("SansSerif", Font.PLAIN, 12);
            }
            return Font.createFont(Font.TRUETYPE_FONT, fuente);
        }

        catch (IOException | FontFormatException e) {
            System.out.println("No se pudo cargar la fuente " + rutaFuente + ", se usará SansSerif");
            return new Font("SansSerif", Font.PLAIN, 12); // fuente de reserva (si falla te muestra sansSerif y ya)
        }
    }

    // boton con el estilo de dressB (fondo burdeos y letra crema)
    public JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(BURDEOS);
        boton.setForeground(CREMA);
        boton.setFont(fuenteNegrita);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return boton;
    }

    // boton de filtrado: gris claro, o rosa con letra blanca si es el que esta activo
    public JButton crearBotonFiltrado(String texto, boolean activo) {
        JButton boton = new JButton(texto);
        boton.setFont(fuenteNegrita);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        if (activo) { // el filtro seleccionado es rosa
            boton.setBackground(ROSA);
            boton.setForeground(CREMA);
        } else { // los demas (no seleccionados)
            boton.setBackground(LIMA);
            boton.setForeground(BURDEOS);
        }
        return boton;
    }

    // tarjeta basica: panel con borde rosa y un titulo (la foto y el resto se anaden luego)
    public JPanel crearTarjeta(String titulo) {
        JPanel tarjeta = new JPanel(new java.awt.BorderLayout());
        tarjeta.setBackground(CREMA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ROSA, 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(fuenteNegrita);
        lblTitulo.setForeground(BURDEOS);
        tarjeta.add(lblTitulo, java.awt.BorderLayout.SOUTH);
        return tarjeta;
    }
}