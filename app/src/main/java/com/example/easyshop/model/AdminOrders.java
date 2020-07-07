package com.example.easyshop.model;

public class AdminOrders
{
    private String name,phone,address,city,pin,time,date,totalAmount;

    public AdminOrders() {
    }


    public AdminOrders(String name, String phone, String address, String city, String pin, String time, String date, String totalAmount) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.pin = pin;
        this.time = time;
        this.date = date;
        this.totalAmount = totalAmount;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
