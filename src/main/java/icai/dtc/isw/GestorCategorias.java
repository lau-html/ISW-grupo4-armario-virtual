package icai.dtc.isw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function; // para el método filtradoPor, puede recibir un getter de Prenda como parametro directamente

public class GestorCategorias { // SPRINT 1: FILTRAR SOLO POR TIPO DE ROPA, ESCALABLE A FILTRAR POR ATRIBUTOS Y A CREAR NUEVAS CATEGORIAS

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

    // ESTO EN REALIDAD LO HAGO PARA EL SPRINT 1 QUE ES SOLO PARA TIPO, PERO DEBERIAMOS PODER ESCALARLO A CUALQUIER ATRIBUTO DIRECTAMENTE - LAU
    // Filtra y devuelve SOLO las prendas de un tipo concreto (si esta en "Pantalones" te da la de pantalones)
    public List<Prenda> prendasPorTipo(List<Prenda> prendas, String tipoFiltrado) {
        return prendas.stream()
                .filter(prenda -> prenda.getTipo().equals(tipoFiltrado)) // deja pasar solo las que coinciden con el tipo
                .toList(); // volvemos a juntarlas en una lista
    }

    /* ESTA ES LA VERSIÓN ESCALABLE: puede filtrar por CUALQUIER atributo de la prenda,
        no es para el sprint 1 pero lo dejo hecho - LAU

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
}