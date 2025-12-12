package com.example.proyectofinal;

import com.example.proyectofinal.Product;
import com.example.proyectofinal.CartItem;

import java.util.ArrayList;

public class CarritoManager {

    private static ArrayList<CartItem> carrito = new ArrayList<>();

    public static ArrayList<CartItem> getCarrito() {
        return carrito;
    }

    public static void agregarProducto(Product product) {

        for (CartItem item : carrito) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        carrito.add(new CartItem(product));
    }

    public static void eliminarProducto(Product product) {
        carrito.removeIf(item -> item.getProduct().getId().equals(product.getId()));
    }

    public static void vaciarCarrito() {
        carrito.clear();
    }

    public static int getTotal() {
        int total = 0;
        for (CartItem item : carrito) {
            total += item.getTotalPrice();
        }
        return total;
    }
}

