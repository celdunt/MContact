package com.example.mcontact;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaDrm;

import java.io.ByteArrayOutputStream;

public class DatabaseController {

    SQLiteDatabase db;

    public DatabaseController(SQLiteDatabase db) {
        this.db = db;

        createTable("contacts",
                new String[]{"id", "name", "midname", "surname", "phone", "photo"},
                new String[]{"INTEGER PRIMARY KEY NOT NULL", "TEXT", "TEXT", "TEXT", "TEXT", "BLOB"});
    }

    private void createTable(String tableName, String[] fieldName, String[] fieldType) {
        if (fieldName.length != fieldType.length) return;

        String request = "CREATE TABLE IF NOT EXISTS " + tableName + " (";

        for (int i = 0; i < fieldName.length; ++i) {
            request += fieldName[i] + " " + fieldType[i].toUpperCase() + ", ";
        }
        request = request.substring(0, request.length()-2);
        request += ")";

        db.execSQL(request);
    }

    public void addValuesToTable(String tableName, String[] fieldName, String[] value) {
        if (fieldName.length != value.length) return;

        String request = "INSERT INTO " + tableName + " (";

        for (int i = 0; i < fieldName.length; ++i) {
            request += fieldName[i] + ", ";
        }
        request = request.substring(0, request.length()-2);
        request += ") VALUES (";
        for (int i = 0; i < fieldName.length; ++i) {
            request += "'" + value[i] + "', ";
        }
        request = request.substring(0, request.length()-2);
        request += ")";

        db.execSQL(request);
    }

    public void updateValueInTable(String tableName, String fieldName, int id, String value) {
        String request = "UPDATE " + tableName + " SET " + fieldName + " = '" + value + "' WHERE id = " + id;

        db.execSQL(request);
    }

    public void insertImageToTable(String tableName, int id, String fieldName, Bitmap image) {
        String request = "UPDATE " + tableName + " SET " + fieldName + " = ? WHERE id = ?";
        SQLiteStatement insertImg = db.compileStatement(request);

        insertImg.bindBlob(1, imageToBlob(image));
        insertImg.bindLong(2, id);

        insertImg.executeInsert();
        insertImg.clearBindings();
    }

    public void deleteFromTable(String tableName, int id) {
        String request = "DELETE FROM " + tableName + " WHERE id = " + id;

        db.execSQL(request);
    }

    public void deleteTable(String tableName) {
        String request = "DROP TABLE " + tableName;

        db.execSQL(request);
    }

    public Cursor getValuesFromTable(String tableName) {
        String request = "SELECT * FROM " + tableName;
        Cursor query = db.rawQuery(request, null);

        return query;
    }

    public Bitmap getImageValueFromBlob(byte[] blob) {
        return BitmapFactory.decodeByteArray(blob, 0, blob.length);
    }


    public byte[] imageToBlob(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, outputStream);

        return outputStream.toByteArray();
    }

    public int getLastIndex(String tableName) {
        String request = "SELECT * FROM " + tableName;
        Cursor query = db.rawQuery(request, null);

        if (query.moveToFirst()) {
            query.moveToLast();

            return Integer.parseInt(query.getString(0));
        }

        return -1;
    }

    public void testDB(String tableName) {
        String request = "SELECT * FROM " + tableName;
        Cursor query = db.rawQuery(request, null);
        if (query.moveToFirst()) {
            System.out.println(query.getString(0));
        }
        System.out.println("ТЕСТ ТЕСТ ТЕСТ");
    }
}
