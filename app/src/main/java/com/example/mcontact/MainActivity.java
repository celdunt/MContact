package com.example.mcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView contactList;
    public static ArrayAdapter<String> adapter;
    public static ArrayList<String> adapterAsList;
    public static ArrayList<Contact> contactArrayList;
    public static int openProperty;
    public static int selectedContact;

    public static final int NEW_ACC = 1;
    public static final int EXIST_ACC = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = findViewById(R.id.contactList);
        contactArrayList = new ArrayList<>();

        adapterAsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        contactList.setAdapter(adapter);

        contactList.setOnItemClickListener((p, v, pos, id) -> {
            MainActivity.selectedContact = pos;
            onExistContact();
        });
    }

    public void onAddNewContact(View view) {
        MainActivity.openProperty = NEW_ACC;
        startActivity(new Intent(this, NewContactActivity.class));
    }

    public void onExistContact() {
        MainActivity.openProperty = EXIST_ACC;
        startActivity(new Intent(this, NewContactActivity.class));
    }
}