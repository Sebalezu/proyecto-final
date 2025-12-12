package com.example.proyectofinal;

public class Producto {
    private String id;
    private String title;
    private String imageUrl;
    private int price;
    private int quantityInCart = 0; // Cantidad agregada al carrito

    public Producto(String id, String title, String imageUrl, int price) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public int getPrice() { return price; }

    public int getQuantityInCart() { return quantityInCart; }
    public void setQuantityInCart(int quantity) { this.quantityInCart = quantity; }
}

