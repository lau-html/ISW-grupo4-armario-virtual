package icai.dtc.isw.ui; // Ajusta el paquete según tu proyecto

import icai.dtc.isw.Outfit;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VentanaOutfits extends JFrame {
    private List<String> partesArriba;
    private List<String> partesAbajo;
    private JLabel lblResultado;

    public VentanaOutfits() {
        setTitle("Creador de Outfits");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ventana independiente
        setLayout(new BorderLayout());

        // Simulacion de listas filtradas del armario
        inicializarListasPrendas();

        lblResultado = new JLabel("Haz clic en 'Random' para crear un outfit", SwingConstants.CENTER);
        add(lblResultado, BorderLayout.CENTER);

        JButton btnRandom = new JButton("Crear Outfit Random");
        add(btnRandom, BorderLayout.SOUTH);

        btnRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearOutfitAleatorio();
            }
        });
    }

    private void inicializarListasPrendas() {
        // Filtra las prendas del armario en dos listas
        partesArriba = new ArrayList<>();
        partesArriba.add("Camiseta Blanca");
        partesArriba.add("Top Negro");
        partesArriba.add("Sudadera Gris");

        partesAbajo = new ArrayList<>();
        partesAbajo.add("Pantalon Vaquero");
        partesAbajo.add("Falda Larga");
        partesAbajo.add("Pantalon Chandal");
    }

    private void crearOutfitAleatorio() {
        if (partesArriba.isEmpty() || partesAbajo.isEmpty()) return;

        // 3. Usa java.util.Random para elegir un índice aleatorio de cada lista
        Random random = new Random();
        int indexArriba = random.nextInt(partesArriba.size());
        int indexAbajo = random.nextInt(partesAbajo.size());

        String prendaArriba = partesArriba.get(indexArriba);
        String prendaAbajo = partesAbajo.get(indexAbajo);

        // Mostrar combinaciones juntas
        Outfit outfitRandom = new Outfit("Outfit Generado", prendaArriba, prendaAbajo);
        lblResultado.setText("Outfit: " + outfitRandom.getParteArriba() + " + " + outfitRandom.getParteAbajo());
    }
}