package com.example.ebetmo;

public class ModelItem {
    private int itemId;
    private String itemName;
    private String Description;
    private String itemImage;
    private String type;
    private String date;
    private String owner;
    private String slots;
    private String price;
    //constructor


    public ModelItem(int itemId, String itemName, String description, String itemImage,  String type, String date, String owner, String slots, String price) {
        this.itemId = itemId;
        this.itemName = itemName;
        Description = description;
        this.itemImage = itemImage;
        this.type = type;
        this.date = date;
        this.owner = owner;
        this.slots = slots;
        this.price = price;
    }

    //getter and setter method

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
