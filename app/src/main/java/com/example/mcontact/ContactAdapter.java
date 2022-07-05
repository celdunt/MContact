package com.example.mcontact;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ContactAdapter {
    public static ArrayList<String> contactDemonstrationList =
            new ArrayList<>();
    public static ArrayAdapter<String> contactDemonstrationAdapter;

    public static ArrayList<String> demonstrationFoundContactsList =
            new ArrayList<>();
    public static ArrayAdapter<String> demonstrationFoundContactsAdapter;

    public static void initialize(MainActivity mainActivity) {
        contactDemonstrationAdapter =
                new ArrayAdapter<>(mainActivity, android.R.layout.simple_list_item_1, contactDemonstrationList);

        demonstrationFoundContactsAdapter =
                new ArrayAdapter<>(mainActivity, android.R.layout.simple_list_item_1, demonstrationFoundContactsList);
    }
}
