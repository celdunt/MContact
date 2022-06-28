package com.example.mcontact;

import android.graphics.Bitmap;

public class Contact {
    Bitmap imageContact;
    String fullname;
    String midname;
    String name;
    String surname;
    String phoneNumber;

    public Contact(Bitmap imageContact, String midname, String name, String surname, String phoneNumber) {
        this.imageContact = imageContact;
        this.midname = midname;
        this.name = name;
        this.surname = surname;
        fullname = midname + " " + name + " " + surname;
        this.phoneNumber = phoneNumber;
    }
}
