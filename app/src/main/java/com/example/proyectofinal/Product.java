package com.example.proyectofinal;

public class Product {

    private String id;
    private String title;
    private int price;  // precio en centavos (API lo trae así)
    private String imageUrl;

    public Product(String id, String title, int price, String imageUrl) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}

