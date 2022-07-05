package com.example.mcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView contactList;

    public static ArrayList<Contact> contactArrayList;
    public static int selectedContact = -1;

    public static DatabaseController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbController = new DatabaseController(getBaseContext().openOrCreateDatabase("contact.db", MODE_PRIVATE, null));

        ContactAdapter.initialize(this);

        contactList = findViewById(R.id.contactList);
        contactArrayList = new ArrayList<>();
        contactList.setAdapter(ContactAdapter.contactDemonstrationAdapter);
        contactList.setOnItemClickListener((p, v, pos, id) -> {
            MainActivity.selectedContact = pos;
            selectContactActivity(null);
        });

        dbController.testDB("contacts");
        loadValuesFromDatabaseTable("contacts");
    }

    public void selectContactActivity(View view) {
        startActivity(new Intent(this, ContactActivity.class));
    }


    private void loadValuesFromDatabaseTable(String tableName) {
        Cursor query = dbController.getValuesFromTable(tableName);

        if (query.moveToFirst()) {
            for (int i = 0; i < query.getCount(); ++i) {
                Contact contact = new Contact(Integer.parseInt(query.getString(0)));
                contact.setName(query.getString(1));
                contact.setMidname(query.getString(2));
                contact.setSurname(query.getString(3));
                contact.setPhoneNumber(query.getString(4));
                if (query.getBlob(5) != null)
                    contact.setImageContact(dbController.getImageValueFromBlob(query.getBlob(5)));
                else contact.setImageContact(null);
                contact.setFullname(contact.getMidname() + " " + contact.getName() + " " + contact.getSurname());

                System.out.println(contact.getID());
                System.out.println(query.getString(0));

                contactArrayList.add(contact);
                ContactAdapter.contactDemonstrationList.add(contact.getFullname());

                query.moveToNext();
            }
        }
    }
}