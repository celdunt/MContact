package com.example.mcontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private final int SELECT_IMAGE = 1234;
    private final int PERMISSION_REQUEST_CALL_PHONE = 1235;

    private Contact openedContact;
    EditText nameC;
    EditText middleNameC;
    EditText surnameC;
    EditText phoneNumberC;
    ImageButton imageC;
    Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        nameC = findViewById(R.id.firstnameContact);
        middleNameC = findViewById(R.id.middlenameContact);
        surnameC= findViewById(R.id.surnameContact);
        phoneNumberC = findViewById(R.id.phonenumberContact);
        imageC = findViewById(R.id.imageContactButton);

        Button acceptButton = findViewById(R.id.acceptButton);
        Button delButton = findViewById(R.id.deleteButton);
        Button dialButton = findViewById(R.id.dialButton);

        if (MainActivity.selectedContact != -1) {
            openedContact = MainActivity.contactArrayList.get(MainActivity.selectedContact);
            acceptButton.setText("Изменить");
            delButton.setVisibility(View.VISIBLE);
            dialButton.setVisibility(View.VISIBLE);

            nameC.setText(openedContact.getName());
            middleNameC.setText(openedContact.getMidname());
            surnameC.setText(openedContact.getSurname());
            phoneNumberC.setText(openedContact.getPhoneNumber());
            if (openedContact.getImageContact() != null) {
                imageC.setImageBitmap(openedContact.getImageContact());
                tempBitmap = openedContact.getImageContact();
            }

            System.out.println(openedContact.getID());
            System.out.println(MainActivity.dbController.getLastIndex("contacts"));
        }
    }

    public void onSelectPhoto(View view) {
        Intent selectPhotoIntent = new Intent();
        selectPhotoIntent.setType("image/*");
        selectPhotoIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectPhotoIntent, "Выберите фото"), SELECT_IMAGE);
    }

    public void onAccept(View view) {
        String regexForPhone = "\\+\\d(\\d{3}){2}\\d{4}";
        if (!phoneNumberC.getText().toString().matches(regexForPhone)) {
            Toast.makeText(getApplicationContext(), "Неверно введен номер телефона", Toast.LENGTH_LONG).show();
            return;
        }

        int contactID = MainActivity.selectedContact == -1? MainActivity.dbController.getLastIndex("contacts") + 1 : openedContact.getID();

        Contact contact = new Contact(
                contactID,
                tempBitmap,
                middleNameC.getText().toString(),
                nameC.getText().toString(),
                surnameC.getText().toString(),
                phoneNumberC.getText().toString()
        );

        if (MainActivity.selectedContact == -1) {
            MainActivity.contactArrayList.add(contact);

            ContactAdapter.contactDemonstrationList.add(contact.getFullname());

            MainActivity.dbController.addValuesToTable(
                    "contacts",
                    new String[] {"name", "midname", "surname", "phone"},
                    new String[] {contact.getName(), contact.getMidname(), contact.getSurname(), contact.getPhoneNumber()}
            );
            MainActivity.dbController.insertImageToTable(
                    "contacts",
                    MainActivity.dbController.getLastIndex("contacts"),
                    "photo",
                    contact.getImageContact()
            );
        } else {
            MainActivity.contactArrayList.set(MainActivity.selectedContact, contact);

            ContactAdapter.contactDemonstrationList.set(MainActivity.selectedContact, contact.getFullname());

            MainActivity.dbController.updateValueInTable("contacts", "name",
                    contact.getID(), contact.getName());
            MainActivity.dbController.updateValueInTable("contacts", "midname",
                    contact.getID(), contact.getMidname());
            MainActivity.dbController.updateValueInTable("contacts", "surname",
                    contact.getID(), contact.getSurname());
            MainActivity.dbController.updateValueInTable("contacts", "phone",
                    contact.getID(), contact.getPhoneNumber());
            MainActivity.dbController.insertImageToTable(
                    "contacts",
                    contact.getID(),
                    "photo",
                    contact.getImageContact()
            );
        }

        MainActivity.copyContactArrayList = (ArrayList<Contact>) MainActivity.contactArrayList.clone();

        ContactAdapter.contactDemonstrationAdapter.notifyDataSetChanged();

        this.finish();

        Toast.makeText(getApplicationContext(), "Успех!", Toast.LENGTH_SHORT).show();
    }

    public void onDelete(View view) {
        MainActivity.dbController.deleteFromTable("contacts", openedContact.getID());
        ContactAdapter.contactDemonstrationList.remove(MainActivity.selectedContact);
        MainActivity.contactArrayList.remove(MainActivity.selectedContact);

        ContactAdapter.contactDemonstrationAdapter.notifyDataSetChanged();

        MainActivity.selectedContact = -1;

        this.finish();

        Toast.makeText(getApplicationContext(), "Успех!", Toast.LENGTH_SHORT).show();
    }

    public void onBack(View view) {
        MainActivity.selectedContact = -1;

        this.finish();
    }

    public void toDial(View view) {
        String dial = "tel:"+openedContact.getPhoneNumber();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CALL_PHONE);
        } else startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + openedContact.getPhoneNumber())));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        tempBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imageC.setImageBitmap(tempBitmap);
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + openedContact.getPhoneNumber())));
                } else {
                    Toast.makeText(getApplicationContext(), "Разрешение не получено!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}