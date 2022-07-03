package com.example.mcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class ContactActivity extends AppCompatActivity {

    private final int SELECT_IMAGE = 1234;

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

        if (MainActivity.selectedContact != -1) {
            openedContact = MainActivity.contactArrayList.get(MainActivity.selectedContact);
            acceptButton.setText("Изменить");

            nameC.setText(openedContact.getName());
            middleNameC.setText(openedContact.getMidname());
            surnameC.setText(openedContact.getSurname());
            phoneNumberC.setText(openedContact.getPhoneNumber());
            imageC.setImageBitmap(openedContact.getImageContact());

            tempBitmap = openedContact.getImageContact();
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

        Contact contact = new Contact(
                tempBitmap,
                middleNameC.getText().toString(),
                nameC.getText().toString(),
                surnameC.getText().toString(),
                phoneNumberC.getText().toString()
        );

        if (MainActivity.selectedContact == -1) {
            MainActivity.contactArrayList.add(contact);

            ContactAdapter.contactDemonstrationList.add(contact.getFullname());
        } else {
            MainActivity.contactArrayList.set(MainActivity.selectedContact, contact);

            ContactAdapter.contactDemonstrationList.set(MainActivity.selectedContact, contact.getFullname());
        }

        ContactAdapter.contactDemonstrationAdapter.notifyDataSetChanged();

        this.finish();

        Toast.makeText(getApplicationContext(), "Успех!", Toast.LENGTH_SHORT).show();
    }

    public void onBack(View view) {
        this.finish();
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
}