package icai.dtc.isw;

import java.util.ArrayList;
import java.util.List;
import icai.dtc.isw.dao.CategoriaDAO;
import icai.dtc.isw.ui.SesionUsuario;

public class GestorCategoriasPers {
    private List<String> categorias;
    private final String CATEGORIA_POR_DEFECTO = "FAVORITOS";
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public GestorCategoriasPers() {
        categorias = new ArrayList<>();
        // 1. Inicializa siempre la lista con "FAVORITOS" por defecto
        categorias.add(CATEGORIA_POR_DEFECTO);
        cargarDesdeBD();
    }

    //Añade a la lista las categorias que el usuario tiene guardadas en la BD
    public void cargarDesdeBD() {
        if (!SesionUsuario.haySesion()) {
            return; // sin sesion (por ejemplo en los tests) solo se trabaja en memoria
        }
        String email = SesionUsuario.getUsuarioActual().getEmail();
        for (String nombre : categoriaDAO.getCategoriasUsuario(email)) {
            if (buscarCategoria(nombre) == null) {
                categorias.add(nombre);
            }
        }
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
        if (SesionUsuario.haySesion()) {
            categoriaDAO.guardarCategoria(categoriaLimpia, SesionUsuario.getUsuarioActual().getEmail());
        }
        return true;
    }

    // LAURA: lo mismo, devuelve true si la categoria se ha borrado; false si era FAVORITOS o no estaba
    public boolean borrarCategoria(String categoriaABorrar) {
        // Bloquea que "FAVORITOS" se pueda borrar
        if (categoriaABorrar != null && categoriaABorrar.trim().equalsIgnoreCase(CATEGORIA_POR_DEFECTO)) {
            return false; // antes aqui lanzaba joptionpane
        }
        boolean borrada = categorias.remove(categoriaABorrar); // devuelve true si se ha borrado, false si no estaba
        // la borro tambien de la BD
        if (borrada && SesionUsuario.haySesion()) {
            categoriaDAO.borrarCategoria(categoriaABorrar, SesionUsuario.getUsuarioActual().getEmail());
        }
        return borrada;
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
    // borra la categoria y se la quita a todas las prendas que la tenian (y lo guarda en la BD).
    // Devuelve el numero de prendas a las que se les ha quitado, o -1 si no se ha podido borrar
    public int borrarCategoria(String categoriaABorrar, List<Prenda> prendas) {
        String encontrada = buscarCategoria(categoriaABorrar);
        if (encontrada == null || !borrarCategoria(encontrada)) {
            return -1; // no existe o es FAVORITOS
        }
        int prendasCambiadas = 0;
        GestorPrendas gestorPrendas = new GestorPrendas();
        if (prendas != null) {
            for (Prenda prenda : prendas) {
                if (prenda.getCategoriasPersonalizadas() != null
                        && prenda.getCategoriasPersonalizadas().contains(encontrada)) {
                    quitarCategoria(prenda, encontrada);
                    prendasCambiadas++;
                    if (SesionUsuario.haySesion()) {
                        gestorPrendas.guardarCategoriasYFavorito(prenda);
                    }
                }
            }
        }
        return prendasCambiadas;
    }

    // devuelve las categorias que se pueden borrar (todas menos FAVORITOS)
    public List<String> getCategoriasBorrables() {
        List<String> borrables = new ArrayList<>();
        for (String cat : categorias) {
            if (!cat.equalsIgnoreCase(CATEGORIA_POR_DEFECTO)) {
                borrables.add(cat);
            }
        }
        return borrables;
    }

    // busca una categoria sin distinguir mayusculas y devuelve el nombre tal y como esta guardado (o null si no esta)
    private String buscarCategoria(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (String cat : categorias) {
            if (cat.trim().equalsIgnoreCase(nombre.trim())) {
                return cat;
            }
        }
        return null;
    }
}