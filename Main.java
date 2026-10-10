import javax.swing.*; 
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        // ventana visual
        JFrame ventana = new JFrame("Listado de Prendas");
        ventana.setSize(400, 500); 
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false); 
        ventana.add(new JScrollPane(areaTexto));

        int contador = 0;
        areaTexto.append("    --- LISTADO DE PRENDAS ---\n\n");

        // "base de datos" (el archivo .txt)
        try {
            File archivo = new File("prendas.txt"); 
            Scanner lector = new Scanner(archivo); 

            // Mientras el archivo tenga prendas
            while (lector.hasNextLine()) {
                String prenda = lector.nextLine(); 
                areaTexto.append(" - " + prenda + "\n"); 
                contador++; 
            }
            


            lector.close(); 

        } catch (Exception e) {
            areaTexto.append("Error: No se ha encontrado el archivo prendas.txt.\n");
        }

        areaTexto.append("\n------------------------\n");

        // compruebo numero de prendas
        if (contador == 0) {
            areaTexto.append(" No hay ninguna prenda, el catálogo está vacío.\n");
        } else {
            areaTexto.append(" Hay " + contador + " prendas en total.\n");
        }

        // muestro la ventana
        ventana.setVisible(true);
    }
}