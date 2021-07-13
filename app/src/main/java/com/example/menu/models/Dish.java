package com.example.menu.models;

public class Dish {
    String name;
    String description;
    String image;
    float rating;
    float price;

    public Dish() {
    }

    public Dish(String name, String description, String image, float price, float rating) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.rating = rating;
        this.price = price;
    }

    public Dish(String name, String image, float price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public Dish(String name, float price) {
        this.name = name;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
