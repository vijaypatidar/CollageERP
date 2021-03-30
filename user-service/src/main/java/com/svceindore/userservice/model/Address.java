package com.svceindore.userservice.model;


public class Address {
    public String state;
    public int zip;
    public String address;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "state='" + state + '\'' +
                ", zip=" + zip +
                ", address='" + address + '\'' +
                '}';
    }
}
