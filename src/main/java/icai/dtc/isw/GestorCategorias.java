package icai.dtc.isw;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class GestorCategorias { 

    // relacionar cada atributo de las prendas con su getter, para luego poder filtrar sin hacer un metodo o un bucle para cada uno
    // LinkedHashMap para que el menu del filtro salga en el mismo orden en que se dan de alta 
    private final Map<String, Function<Prenda, String>> atributosFijos = new LinkedHashMap<>();


    // IMPORTANTE!! SI AÑADIMOS NUEVOS ATRIBUTOS A LA CLASE PRENDA, HAY QUE AÑADIRLOS A ESTE MAPA A CONTINUACION PARA PODER FILTRAR POR ELLOS
    public GestorCategorias() {
        atributosFijos.put("tipo", Prenda::getTipo);
        atributosFijos.put("marca", Prenda::getMarca);
        atributosFijos.put("temporada", Prenda::getTemporada);
        atributosFijos.put("estilo", Prenda::getEstilo);
        atributosFijos.put("color", Prenda::getColor);
    }

    // quito el metodo de categorizarPorTipo y prendasPorTipo, ya que esa logica la gestiona filtrarPor de la misma manera y no es necesario iteraciones de mas

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
                .filter(p -> Objects.equals(atributo.apply(p), valor)) // atributo.apply(p) = llamar al getter sobre p;  // Objects.equals para que una prenda con ese atributo a null no rompa el filtro
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

    // guardo los nombres de los atributos que se pueden filtrar en el orden en que se dieron de alta en el constructor (para el menu del panel)
    public java.util.Set<String> nombresAtributos() {
        return atributosFijos.keySet(); // los atributos que estan fijos por un lado
    }

    // valores distintos que tiene un atributo en las prendas, sin repetir (para las opciones del menu del panel)
    // si yo tengo un atributo en el panel me saldran todas las opciones posibles por las que filtrar en cuanto a ese atributo
    public List<String> valoresDeAtributo(List<Prenda> prendas, String nombreAtributo) {
        Function<Prenda, String> getter = atributosFijos.get(nombreAtributo);
        if (getter == null) {
            return new ArrayList<>(); // atributo que no conozco
        }
        List<String> valores = new ArrayList<>();
        for (Prenda p : prendas) {
            String valor = getter.apply(p);
            if (valor != null && !valores.contains(valor)) {
                valores.add(valor);
            }
        }
        return valores;
    }
}

