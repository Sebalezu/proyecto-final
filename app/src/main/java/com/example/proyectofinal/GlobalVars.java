package com.example.proyectofinal;

import java.util.ArrayList;

public class GlobalVars {

    public static ArrayList<Producto> carrito = new ArrayList<>();

    public static void agregarAlCarrito(Producto p) {
        for (Producto x : carrito) {
            if (x.id.equals(p.id)) {
                x.cantidad++;   // Aumenta cantidad si ya existe
                return;
            }
        }
        carrito.add(p); // Si no existe, lo agrega
    }
}

