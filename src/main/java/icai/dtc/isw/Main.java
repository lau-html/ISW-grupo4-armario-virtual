package icai.dtc.isw;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hola, Armario Virtual! :)");

        // PROBANDO GESTOR DE CATEGORIAS

        // Instancio gestor
        GestorCategorias gestor = new GestorCategorias();

        // Añado un armario temporal hasta conectar con BBDD
        List<Prenda> miArmario = new ArrayList<>();

        miArmario.add(new Prenda(1, "Camiseta Nike", "Camisetas", "Blanco", "Camiseta basica", "Deportivo", "Nike", "Atemporal"));
        miArmario.add(new Prenda(2, "Vaqueros Majada", "Pantalones", "Azul", "Vaqueros rectos", "Casual", "Levi's", "Atemporal"));
        miArmario.add(new Prenda(3, "Gabardina", "Abrigos", "Marron", "Cazadora de entretiempo", "Elegante", "Zara", "Primavera"));

        // Pruebo filtrar por tipo "Pantalones" - SPRINT 1
        List<Prenda> pantalones = gestor.prendasPorTipo(miArmario, "Pantalones");
        System.out.println("Pantalones encontrados: " + pantalones);

        // Pruebo el filtro generico por marca
        List<Prenda> zara = gestor.filtrarPor(miArmario, Prenda::getMarca, "Zara");
        System.out.println("De Zara: " + zara);

        // Pruebo el filtro generico por estilo
        List<Prenda> elegantes = gestor.filtrarPor(miArmario, Prenda::getEstilo, "Elegante");
        System.out.println("Elegantes: " + elegantes);
    }
}