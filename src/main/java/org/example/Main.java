package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hola, Armario Virtual!");

        // PROBANDO GESTOR DE CATEGORIAS

        // Instancio gestor
        GestorCategorias gestor = new GestorCategorias();

        // Añado un armario temporal hasta conectar con BBDD
        List<Prenda> miArmario = new ArrayList<>();
        
        miArmario.add(new Prenda("Camiseta Nike", "Camisetas", "Blanco"));
        miArmario.add(new Prenda("Vaqueros Majada", "Pantalones", "Azul"));
        miArmario.add(new Prenda("Pantalones Deporte", "Pantalones", "Negro"));
        
        // Pruebo categorización por tipo "Pantalones"
        List<Prenda> pantalones = gestor.prendasPorTipo(miArmario, "Pantalones");
        
        // Imprimo pantalones
        System.out.println("Pantalones encontrados: " + pantalones);
    } 
    
}