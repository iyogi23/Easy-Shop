package com.example.easyshop.model;

public class Products
{
     private  String Pname, image,date,description,category,price,pid,time;

     public Products()
     {

     }

    public Products(String pname, String image, String date, String description, String category, String price, String pid, String time) {
        Pname = pname;
        this.image = image;
        this.date = date;
        this.description = description;
        this.category = category;
        this.price = price;
        this.pid = pid;
        this.time = time;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
