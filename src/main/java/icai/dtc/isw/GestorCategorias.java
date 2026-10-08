package icai.dtc.isw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class GestorCategorias { // SPRINT 1: FILTRAR POR TIPO DE ROPA, ESCALABLE A FILTRAR POR ATRIBUTOS Y A CREAR NUEVAS CATEGORIAS
    
    // Agrupa y categoriza prendas por tipo
    public Map<String, List<Prenda>> categorizarPorTipo(List<Prenda> prendas) {
        Map<String, List<Prenda>> categorias = new HashMap<>();

        for (Prenda prenda : prendas) {
            String tipo = prenda.getTipo();
            categorias.putIfAbsent(tipo, new ArrayList<>()); // Si la categoría no existe, se crea una nueva lista
            categorias.get(tipo).add(prenda); 
        }

        return categorias;
    }

    public List<Prenda> prendasPorTipo(List<Prenda> prendas, String tipoFiltrado) {
            return prendas.stream()
                    .filter(prenda -> prenda.getTipo().equals(tipoFiltrado))
                    .toList();
        }
}
