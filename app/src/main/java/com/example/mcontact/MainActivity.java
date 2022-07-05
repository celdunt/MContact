package com.example.mcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView contactList;
    EditText searchField;

    public static ArrayList<Contact> contactArrayList;
    public static ArrayList<Contact> copyContactArrayList;
    public ArrayList<Contact> tempContactsList = new ArrayList<>();



    public static int selectedContact = -1;

    public static DatabaseController dbController;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#252323")));




        dbController = new DatabaseController(getBaseContext().openOrCreateDatabase("contact.db", MODE_PRIVATE, null));

        ContactAdapter.initialize(this);

        contactList = findViewById(R.id.contactList);
        contactArrayList = new ArrayList<>();
        contactList.setAdapter(ContactAdapter.contactDemonstrationAdapter);
        contactList.setOnItemClickListener((p, v, pos, id) -> {
            MainActivity.selectedContact = pos;
            selectContactActivity(null);
        });



        searchField = findViewById(R.id.searchField);
        searchFunction();

        loadValuesFromDatabaseTable("contacts");
    }

    public void searchFunction() {
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchField.getText().toString().equals("")) {
                    contactList.setAdapter(ContactAdapter.contactDemonstrationAdapter);
                    contactArrayList = (ArrayList<Contact>) copyContactArrayList.clone();
                }
                else {
                    contactList.setAdapter(ContactAdapter.demonstrationFoundContactsAdapter);

                    tempContactsList.clear();
                    ContactAdapter.demonstrationFoundContactsList.clear();

                    tempContactsList = (ArrayList<Contact>) contactArrayList.clone();
                    contactArrayList = contactArrayList.stream()
                            .filter(contact -> contact.getFullname().contains(searchField.getText().toString()) ||
                                    contact.getPhoneNumber().contains(searchField.getText().toString()))
                            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

                    for (Contact c : contactArrayList)
                        ContactAdapter.demonstrationFoundContactsList.add(c.getFullname());

                    ContactAdapter.demonstrationFoundContactsAdapter.notifyDataSetChanged();
                }
            }
        });
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

        copyContactArrayList = (ArrayList<Contact>) contactArrayList.clone();
    }
}