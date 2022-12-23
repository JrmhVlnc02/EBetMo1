package com.example.ebetmo;

public class NotifyModel {
    public int notify_Id;
    private String owner_id;
    private String item_id;
    private String item_owner_id;
    private String status;
    private String chosen;
    private String itemImage;
    private String date;

    public NotifyModel(int notify_Id, String owner_id, String item_id, String item_owner_id, String status, String chosen, String itemImage, String date) {
        this.notify_Id = notify_Id;
        this.owner_id = owner_id;
        this.item_id = item_id;
        this.item_owner_id = item_owner_id;
        this.status = status;
        this.chosen = chosen;
        this.itemImage = itemImage;
        this.date = date;
    }

    public int getNotify_Id() {
        return notify_Id;
    }

    public void setNotify_Id(int notify_Id) {
        this.notify_Id = notify_Id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_owner_id() {
        return item_owner_id;
    }

    public void setItem_owner_id(String item_owner_id) {
        this.item_owner_id = item_owner_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChosen() {
        return chosen;
    }

    public void setChosen(String chosen) {
        this.chosen = chosen;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
