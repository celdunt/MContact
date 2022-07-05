package com.example.mcontact;

import android.graphics.Bitmap;

public class Contact {
    private int identifier;
    private Bitmap imageContact;
    private String fullname;
    private String midname;
    private String name;
    private String surname;
    private String phoneNumber;

    public Contact(int ID, Bitmap imageContact, String midname, String name, String surname, String phoneNumber) {
        identifier = ID;
        this.imageContact = imageContact;
        this.midname = midname;
        this.name = name;
        this.surname = surname;
        fullname = midname + " " + name + " " + surname;
        this.phoneNumber = phoneNumber;
    }

    public Contact(Bitmap imageContact, String midname, String name, String surname, String phoneNumber) {
        this.imageContact = imageContact;
        this.midname = midname;
        this.name = name;
        this.surname = surname;
        fullname = midname + " " + name + " " + surname;
        this.phoneNumber = phoneNumber;
    }

    public Contact(int ID) {
        identifier = ID;
    }

    public Bitmap getImageContact() {
        return imageContact;
    }

    public void setImageContact(Bitmap imageContact) {
        this.imageContact = imageContact;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMidname() {
        return midname;
    }

    public void setMidname(String midname) {
        this.midname = midname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getID() {return identifier;}
}
