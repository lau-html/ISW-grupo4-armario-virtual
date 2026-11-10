package icai.dtc.isw.ui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

// Selector de fotos de prendas. Lo usan el formulario de nueva prenda y la ventana de editar,
// así la validación del formato está en un solo sitio.
public class SelectorFoto {

    // Formatos de imagen que admite dressB
    private static final String[] EXTENSIONES_VALIDAS = {"jpg", "jpeg", "png"};

    // Abre el explorador de archivos para elegir una foto.
    // Devuelve la ruta de la foto elegida, o null si el usuario cancela o elige un formato no válido.
    public static String elegirFoto(Component ventanaPadre) {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("dressB - Seleccionar foto de la prenda");
        // Filtro para que solo se vean imágenes JPG, JPEG y PNG
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, JPEG, PNG)", EXTENSIONES_VALIDAS));
        selector.setAcceptAllFileFilterUsed(false); // quita la opción "Todos los archivos"

        int resultado = selector.showOpenDialog(ventanaPadre);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return null; // el usuario ha cancelado
        }

        File archivo = selector.getSelectedFile();
        // Segunda comprobación por si el usuario escribe a mano el nombre de otro tipo de archivo
        if (!esFormatoValido(archivo.getName())) {
            JOptionPane.showMessageDialog(ventanaPadre,
                    "Formato no válido. Solo se admiten fotos JPG, JPEG o PNG.",
                    "Foto no válida", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return archivo.getAbsolutePath();
    }

    // Comprueba que el nombre del archivo termine en .jpg, .jpeg o .png (sin importar mayúsculas)
    public static boolean esFormatoValido(String nombreArchivo) {
        if (nombreArchivo == null) {
            return false;
        }
        String nombre = nombreArchivo.trim().toLowerCase();
        for (String extension : EXTENSIONES_VALIDAS) {
            if (nombre.endsWith("." + extension) && nombre.length() > extension.length() + 1) {
                return true;
            }
        }
        return false;
    }

    // Devuelve solo el nombre del archivo a partir de la ruta completa, para enseñarlo en pantalla
    public static String nombreArchivo(String ruta) {
        if (ruta == null || ruta.isEmpty()) {
            return "Sin foto";
        }
        return new File(ruta).getName();
    }
}