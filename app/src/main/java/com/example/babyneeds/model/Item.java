package com.example.babyneeds.model;

public class Item {

    private int id;
    private String itemName;
    private String quantity;
    private String colour;
    private String size;
    private String dateAdded;

    public Item() {
    }

    public Item(String itemName, String quantity, String colour, String size, String dateAdded) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.colour = colour;
        this.size = size;
        this.dateAdded = dateAdded;
    }

    public Item(int id, String itemName, String quantity, String colour, String size, String dateAdded) {
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.colour = colour;
        this.size = size;
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
