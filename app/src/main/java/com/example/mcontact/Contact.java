package com.example.mcontact;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Contact implements Serializable {

    private int identifier;
    byte[] imageByte;
    private String fullname;
    private String midname;
    private String name;
    private String surname;
    private String phoneNumber;

    public Contact(int ID, Bitmap imageContact, String midname, String name, String surname, String phoneNumber) {
        identifier = ID;
        this.midname = midname;
        this.name = name;
        this.surname = surname;
        fullname = midname + " " + name + " " + surname;
        this.phoneNumber = phoneNumber;
        imageByte = DatabaseController.imageToBlob(imageContact);
    }

    public Contact(int ID) {
        identifier = ID;
    }

    public Bitmap getImageContact() {
        return DatabaseController.getImageValueFromBlob(imageByte);
    }

    public void setImageContact(Bitmap imageContact) {
        imageByte = DatabaseController.imageToBlob(imageContact);
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


    @Override
    public String toString() {
        return getFullname();
    }
}
