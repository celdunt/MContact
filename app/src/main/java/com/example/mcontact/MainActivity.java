package com.example.mcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int WRITE_REQUEST_CODE = 0;
    private static final int READ_REQUEST_CODE = 1;


    ListView contactList;
    EditText searchField;

    public static ArrayList<Contact> contactArrayList;
    public static ArrayList<Contact> copyContactArrayList;
    public static ArrayAdapter<Contact> contactAdapter;



    public static int selectedContact = -1;

    public static DatabaseController dbController;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#252323")));




        dbController = new DatabaseController(getBaseContext().openOrCreateDatabase("contact.db", MODE_PRIVATE, null));


        contactList = findViewById(R.id.contactList);
        contactArrayList = new ArrayList<>();
        contactAdapter =
                new ArrayAdapter<>(this, R.layout.list_white_text, contactArrayList);
        contactList.setAdapter(contactAdapter);
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (searchField.getText().toString().equals("")) {
                    contactArrayList.clear();
                    contactArrayList.addAll((ArrayList<Contact>)copyContactArrayList.clone());

                    contactAdapter.notifyDataSetChanged();
                }
                else {
                    contactArrayList.clear();
                    contactArrayList.addAll(copyContactArrayList.stream()
                            .filter(contact -> contact.getFullname().contains(searchField.getText().toString()) ||
                                    contact.getPhoneNumber().contains(searchField.getText().toString()))
                            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));

                    contactAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void selectContactActivity(View view) {
        startActivity(new Intent(this, ContactActivity.class));
    }


    private void uploadValuesToDatabaseTable(String tableName) {
        dbController.deleteTable(tableName);
        dbController = new DatabaseController(getBaseContext().openOrCreateDatabase("contact.db", MODE_PRIVATE, null));

        for (Contact c : contactArrayList) {
            MainActivity.dbController.addValuesToTable(
                    "contacts",
                    new String[] {"name", "midname", "surname", "phone"},
                    new String[] {c.getName(), c.getMidname(), c.getSurname(), c.getPhoneNumber()}
            );
            MainActivity.dbController.insertImageToTable(
                    "contacts",
                    MainActivity.dbController.getLastIndex("contacts"),
                    "photo",
                    c.getImageContact()
            );
        }
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

                contactArrayList.add(contact);

                query.moveToNext();
            }
        }

        copyContactArrayList = (ArrayList<Contact>) contactArrayList.clone();
    }


    public void onExport(View view) {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, WRITE_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    OpenFileDialog fileDialog = new OpenFileDialog(this)
                            .setOnlyFoldersFilter()
                            .setOpenDialogListener(fileName -> {
                                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName + "/contact.dat"))) {
                                    oos.writeObject(contactArrayList);

                                    System.out.println(fileName + "contact.dat");

                                    Toast.makeText(getApplicationContext(), "Экспорт успешен!", Toast.LENGTH_SHORT).show();
                                } catch (Exception ex) {
                                    System.out.println(ex.getMessage());
                                }
                            });
                    fileDialog.show();
                } else {
                }
                break;
            case READ_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    OpenFileDialog fileDialog = new OpenFileDialog(this)
                            .setFilter(".*\\.dat")
                            .setOpenDialogListener(fileName -> {
                                try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
                                    contactArrayList.clear();
                                    ArrayList<Contact> contacts = (ArrayList<Contact>) ois.readObject();
                                    contactArrayList.addAll(contacts);

                                    uploadValuesToDatabaseTable("contacts");

                                    contactAdapter.notifyDataSetChanged();

                                    Toast.makeText(getApplicationContext(), "Импорт успешен!", Toast.LENGTH_SHORT).show();
                                } catch (Exception ex) {
                                    System.out.println(ex.getMessage());
                                    System.out.println(fileName);
                                }
                            });
                    fileDialog.show();
                } else {
                }
                break;
        }
    }
    public void onImport(View view) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissions, READ_REQUEST_CODE);
    }
}