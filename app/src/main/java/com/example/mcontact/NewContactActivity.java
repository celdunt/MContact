package com.example.mcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class NewContactActivity extends AppCompatActivity {

    EditText nameC;
    EditText middleNameC;
    EditText surnameC;
    EditText phoneNumberC;
    ImageButton imageC;
    Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        nameC = findViewById(R.id.firstnameContact);
        middleNameC = findViewById(R.id.middlenameContact);
        surnameC= findViewById(R.id.surnameContact);
        phoneNumberC = findViewById(R.id.phonenumberContact);
        imageC = findViewById(R.id.imageContactButton);

        Button acceptButton = findViewById(R.id.acceptButton);
        if (MainActivity.openProperty == MainActivity.NEW_ACC) acceptButton.setText("Добавить");
        if (MainActivity.openProperty == MainActivity.EXIST_ACC) {
            nameC.setText(MainActivity.contactArrayList.get(MainActivity.selectedContact).name);
            middleNameC.setText(MainActivity.contactArrayList.get(MainActivity.selectedContact).midname);
            surnameC.setText(MainActivity.contactArrayList.get(MainActivity.selectedContact).surname);
            phoneNumberC.setText(MainActivity.contactArrayList.get(MainActivity.selectedContact).phoneNumber);
            imageC.setImageBitmap(MainActivity.contactArrayList.get(MainActivity.selectedContact).imageContact);
            tempBitmap = MainActivity.contactArrayList.get(MainActivity.selectedContact).imageContact;
            acceptButton.setText("Изменить");
        }
    }

    public void onSelectPhoto(View view) {
        Intent selectPhotoIntent = new Intent();
        selectPhotoIntent.setType("image/*");
        selectPhotoIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectPhotoIntent, "Выберите фото"), 1234);
    }

    public void onAccept(View view) {
        String regexForPhone = "\\+\\d(\\d{3}){2}\\d{4}";
        if (!phoneNumberC.getText().toString().matches(regexForPhone)) {
            Toast.makeText(getApplicationContext(), "Неверно введен номер телефона", Toast.LENGTH_LONG).show();
            return;
        }

        if (MainActivity.openProperty == MainActivity.NEW_ACC) {
            Contact contact = new Contact(
                    tempBitmap,
                    middleNameC.getText().toString(),
                    nameC.getText().toString(),
                    surnameC.getText().toString(),
                    phoneNumberC.getText().toString()
            );
            MainActivity.contactArrayList.add(contact);

            MainActivity.adapterAsList.add(contact.fullname);
            MainActivity.adapter.addAll(MainActivity.adapterAsList);
        }
        if (MainActivity.openProperty == MainActivity.EXIST_ACC) {
            MainActivity.contactArrayList.get(MainActivity.selectedContact).imageContact = tempBitmap;
            MainActivity.contactArrayList.get(MainActivity.selectedContact).name = nameC.getText().toString();
            MainActivity.contactArrayList.get(MainActivity.selectedContact).midname = middleNameC.getText().toString();
            MainActivity.contactArrayList.get(MainActivity.selectedContact).surname = surnameC.getText().toString();
            MainActivity.contactArrayList.get(MainActivity.selectedContact).fullname = middleNameC.getText().toString() + " "
            + nameC.getText().toString() + " " + surnameC.getText().toString();
            MainActivity.contactArrayList.get(MainActivity.selectedContact).phoneNumber = phoneNumberC.getText().toString();

            MainActivity.adapterAsList.set(MainActivity.selectedContact,
                    MainActivity.contactArrayList.get(MainActivity.selectedContact).fullname);
            MainActivity.adapter.clear();
            MainActivity.adapter.addAll(MainActivity.adapterAsList);
        }

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
            case 1234:
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