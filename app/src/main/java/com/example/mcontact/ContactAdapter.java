package com.example.mcontact;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ContactAdapter {
    public static ArrayList<String> contactDemonstrationList =
            new ArrayList<>();
    public static ArrayAdapter<String> contactDemonstrationAdapter;

    public static void initialize(MainActivity mainActivity) {
        contactDemonstrationAdapter =
                new ArrayAdapter<>(mainActivity, android.R.layout.simple_list_item_1, contactDemonstrationList);
    }
}
