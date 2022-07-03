package com.example.mcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView contactList;

    public static ArrayList<Contact> contactArrayList;
    public static int selectedContact = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContactAdapter.initialize(this);

        contactList = findViewById(R.id.contactList);
        contactArrayList = new ArrayList<>();

        contactList.setAdapter(ContactAdapter.contactDemonstrationAdapter);

        contactList.setOnItemClickListener((p, v, pos, id) -> {
            MainActivity.selectedContact = pos;
            selectContactActivity(null);
        });
    }

    public void selectContactActivity(View view) {
        startActivity(new Intent(this, ContactActivity.class));
    }
}