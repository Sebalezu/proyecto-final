package com.example.proyectofinal;

public class Producto {

    public String id;
    public String nombre;
    public String imagen;
    public double precio;
    public int cantidad = 1; // Para el carrito

    public Producto(String id, String nombre, String imagen, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.precio = precio;
    }
}


