package icai.dtc.isw;

import java.util.ArrayList;
import java.util.List;

public class GestorCategoriasPers {
    private List<String> categorias;
    private final String CATEGORIA_POR_DEFECTO = "FAVORITOS";

    public GestorCategoriasPers() {
        categorias = new ArrayList<>();
        // 1. Inicializa siempre la lista con "FAVORITOS" por defecto
        categorias.add(CATEGORIA_POR_DEFECTO);
    }


    // LAURA: saco los JOptionPane del gestor; los avisos los muevo a la ui
    // devuelve true si la categoria se ha añadido y false si estaba vacia o repetida (lo cambio a public boolean);
    public boolean anadirCategoria(String nuevaCategoria) {
        if (nuevaCategoria == null || nuevaCategoria.trim().isEmpty()) {
            return false; // antes aqui lanzaba joptionpane, si no hay un nombre valido, no creo la categoria

        }
        
        String categoriaLimpia = nuevaCategoria.trim();

        // 2. Validar duplicados ignorando mayúsculas/minúsculas
        for (String cat : categorias) {
            if (cat.trim().equalsIgnoreCase(categoriaLimpia)) {
                return false; // antes aqui lanzaba joptionpane
            }
        }
        categorias.add(categoriaLimpia);
        return true;
    }

    // LAURA: lo mismo, devuelve true si la categoria se ha borrado; false si era FAVORITOS o no estaba
    public boolean borrarCategoria(String categoriaABorrar) {
        // Bloquea que "FAVORITOS" se pueda borrar
        if (categoriaABorrar != null && categoriaABorrar.trim().equalsIgnoreCase(CATEGORIA_POR_DEFECTO)) {
            return false; // antes aqui lanzaba joptionpane
        }
        return categorias.remove(categoriaABorrar); // devuelve true si se ha borrado, false si no estaba
    }

    public List<String> getCategorias() {
        return categorias;
    }

    // asigno una categoria a una prenda, a su lista de categorias personalizadas; crea la lista si la prenda aun no tenia
    public void asignarCategoria(Prenda prenda, String nombre) {
        if (prenda.getCategoriasPersonalizadas() == null) {
            prenda.setCategoriasPersonalizadas(new ArrayList<>());
        }
        if (!prenda.getCategoriasPersonalizadas().contains(nombre)) {
            prenda.getCategoriasPersonalizadas().add(nombre);
        }
    }

    // quita una categoria de una prenda; si no la tenia (o no tenia lista directamente) no hace nada
    public void quitarCategoria(Prenda prenda, String nombre) {
        if (prenda.getCategoriasPersonalizadas() != null) {
            prenda.getCategoriasPersonalizadas().remove(nombre);
        }
    }

    // marcar o desmarcar favorito: sincronizo la categoria de FAVORITOS en la lista de categoriasPersonalizadas 
    public void marcarFavorito(Prenda prenda, boolean favorito) {
        prenda.setEsFavorito(favorito);
        if (favorito) {
            asignarCategoria(prenda, CATEGORIA_POR_DEFECTO);
        } else {
            quitarCategoria(prenda, CATEGORIA_POR_DEFECTO);
        }
    }
}