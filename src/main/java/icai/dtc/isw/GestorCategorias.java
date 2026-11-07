package icai.dtc.isw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GestorCategorias { 

    // relacionar cada atributo de las prendas con su getter, para luego poder filtrar sin hacer un metodo o un bucle para cada uno
    private final Map<String, Function<Prenda, String>> atributosFijos = new HashMap<>();


    // IMPORTANTE!! SI AÑADIMOS NUEVOS ATRIBUTOS A LA CLASE PRENDA, HAY QUE AÑADIRLOS A ESTE MAPA A CONTINUACION PARA PODER FILTRAR POR ELLOS
    public GestorCategorias() {
        atributosFijos.put("tipo", Prenda::getTipo);
        atributosFijos.put("marca", Prenda::getMarca);
        atributosFijos.put("temporada", Prenda::getTemporada);
        atributosFijos.put("estilo", Prenda::getEstilo);
        atributosFijos.put("color", Prenda::getColor);
    }


    // Agrupa y categoriza prendas por tipo
    // Devuelve un mapa: clave = tipo de ropa ("Pantalones"), valor = lista de prendas de ese tipo
    public Map<String, List<Prenda>> categorizarPorTipo(List<Prenda> prendas) {
        Map<String, List<Prenda>> categorias = new HashMap<>();

        for (Prenda prenda : prendas) {
            String tipo = prenda.getTipo();
            categorias.putIfAbsent(tipo, new ArrayList<>()); // Si la categoría no existe, se crea una nueva lista
            categorias.get(tipo).add(prenda); // metes la prenda en la lista correspondiente a su tipo
        }

        return categorias;
    }

    // Filtra y devuelve SOLO las prendas de un tipo concreto (si esta en "Pantalones" te da la de pantalones)
    public List<Prenda> prendasPorTipo(List<Prenda> prendas, String tipoFiltrado) {
        return prendas.stream()
                .filter(prenda -> prenda.getTipo().equals(tipoFiltrado)) // deja pasar solo las que coinciden con el tipo
                .toList(); // volvemos a juntarlas en una lista
    }

    /* ESTA ES LA VERSIÓN ESCALABLE: puede filtrar por CUALQUIER atributo de la prenda,
        Explicacion del filtrado:

        Parametros:
        - atributo: el getter que le pasemos (Prenda::getMarca, Prenda::getTipo...)
        - valor: por lo que filtramos ("Zara", "Elegante"...)

        Ejemplo de uso: filtrarPor(armario, Prenda::getMarca, "Zara")

        EXPLICACION DE FUNCTION Y DE SU USO:
        Function es una variable que guarda un MÉTODO, coge una prenda y devuelve un String.
        Al pasarlo usas un getter del objeto Prenda con :: (Prenda::getTipo): los dos puntos dicen
        guardan que es el metodo de la clase pero no lo llama
        El metodo se llama al usar el apply(p), que ejecuta el getter sobre la prenda p */

    public List<Prenda> filtrarPor(List<Prenda> prendas, Function<Prenda, String> atributo, String valor) {
        return prendas.stream()
                .filter(p -> atributo.apply(p).equals(valor)) // atributo.apply(p) = llamar al getter sobre p
                .toList();
    }
// FILTRADO POR VARIOS ATRIBUTOS SIMULTANEOS - permitir seleccionar varios filtros a la vez:

    public List<Prenda> filtrarCombinado(List<Prenda> prendas, Map<String, String> filtrosActivos) {
        List<Prenda> resultado = prendas; // todas las prendas del armario
        for (Map.Entry<String, String> filtro : filtrosActivos.entrySet()) {
            String tipoFiltro = filtro.getKey(); // el tipo de filtro, por ej "marca"
            String valorFiltro = filtro.getValue(); // el valor que quiero filtrar,por ej "Zara"

            if (tipoFiltro.equals("categoriaPersonalizada")) { // filtrado por categorias personalizadas por el usuario, que estan en la lista de categoriasPersonalizadas correspondiente a cada prenda (GestorCategoriasPers)
                resultado = resultado.stream()
                        // distinto de null (busco que la lista exista para evitar errores) y que la prenda contenga como filtro el personalizado buscado
                        .filter(p -> p.getCategoriasPersonalizadas() != null && p.getCategoriasPersonalizadas().contains(valorFiltro))
                        .toList();
                    continue; // una vez he aplicado el filtro sigo filtrando con los demas del bucle 
            }

            Function<Prenda, String> getterFiltro = atributosFijos.get(tipoFiltro); // busco el getter correspondiente al filtro
                if (getterFiltro == null) {
                    continue; // si no reconozco el filtro, paso al siguiente
                }
                resultado = filtrarPor(resultado, getterFiltro, valorFiltro); // aplico el filtro sobre las prendas que quedaban

        }
        return resultado; // devuelvo la lista de prendas filtradas por todos los filtros activos
    }
}

