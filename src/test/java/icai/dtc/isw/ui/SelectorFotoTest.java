package icai.dtc.isw.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

// Pruebas de la validación del formato de las fotos (solo JPG, JPEG y PNG)
public class SelectorFotoTest {

    @Test
    void aceptaFotosJpgJpegYPng() {
        assertTrue(SelectorFoto.esFormatoValido("camiseta.jpg"));
        assertTrue(SelectorFoto.esFormatoValido("vaqueros.jpeg"));
        assertTrue(SelectorFoto.esFormatoValido("abrigo.png"));
    }

    @Test
    void aceptaExtensionesEnMayusculas() {
        assertTrue(SelectorFoto.esFormatoValido("FOTO.JPG"));
        assertTrue(SelectorFoto.esFormatoValido("Foto.Png"));
    }

    @Test
    void rechazaOtrosFormatos() {
        assertFalse(SelectorFoto.esFormatoValido("foto.gif"));
        assertFalse(SelectorFoto.esFormatoValido("documento.pdf"));
        assertFalse(SelectorFoto.esFormatoValido("foto.jpg.exe"));
    }

    @Test
    void rechazaNombresVaciosOSinExtension() {
        assertFalse(SelectorFoto.esFormatoValido(null));
        assertFalse(SelectorFoto.esFormatoValido(""));
        assertFalse(SelectorFoto.esFormatoValido("jpg"));
        assertFalse(SelectorFoto.esFormatoValido(".png"));
    }

    @Test
    void muestraSoloElNombreDelArchivo() {
        assertEquals("Sin foto", SelectorFoto.nombreArchivo(null));
        assertEquals("camiseta.jpg", SelectorFoto.nombreArchivo("carpeta/fotos/camiseta.jpg"));
    }
}