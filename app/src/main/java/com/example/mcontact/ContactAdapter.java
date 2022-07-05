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
                new ArrayAdapter<>(mainActivity, R.layout.list_white_text, contactDemonstrationList);

        demonstrationFoundContactsAdapter =
                new ArrayAdapter<>(mainActivity, R.layout.list_white_text, demonstrationFoundContactsList);
    }
}
