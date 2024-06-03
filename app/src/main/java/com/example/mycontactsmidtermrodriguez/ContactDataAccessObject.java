package com.example.mycontactsmidtermrodriguez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//This class will handle CRUD operations and communicates with the dbHelper class
public class ContactDataAccessObject {
    private SQLiteDatabase database;
    private dbHelper dbHelper;
    List<Contact> contacts = new ArrayList<>();

    public ContactDataAccessObject(Context context) {
        dbHelper = new dbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    //This method will display a list of all the contacts stored
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();

        Cursor cursor = database.query(dbHelper.TABLE_CONTACTS,
                new String[]{dbHelper.COLUMN_ID, dbHelper.COLUMN_NAME, dbHelper.COLUMN_PHONE, dbHelper.COLUMN_EMAIL},
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_PHONE));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_EMAIL));

            int contactId = 0;
            Contact contact = new Contact(name, phone, email);
            contact.setId(id);
            contacts.add(contact);
            cursor.moveToNext();
        }
        cursor.close();
        return contacts;
    }

    //This method will update contact information
    public boolean updateContact(int id, String name, String phone, String email) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_NAME, name);
        values.put(dbHelper.COLUMN_PHONE, phone);
        values.put(dbHelper.COLUMN_EMAIL, email);
        database.update(dbHelper.TABLE_CONTACTS, values, dbHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        int rowsAffected = database.update(dbHelper.TABLE_CONTACTS, values, dbHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        return rowsAffected > 0;
    }

    //This method will delete a contact
    public void deleteContact(int id) {
        database.delete(dbHelper.TABLE_CONTACTS, dbHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Contact getContactById(int id) {
        Cursor cursor = database.query(dbHelper.TABLE_CONTACTS,
                new String[]{dbHelper.COLUMN_ID, dbHelper.COLUMN_NAME, dbHelper.COLUMN_PHONE, dbHelper.COLUMN_EMAIL},
                dbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_PHONE));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.COLUMN_EMAIL));

            int contactId = 0;
            Contact contact = new Contact(contactId, name, phone, email);
            contact.setId(id);
            cursor.close();
            return contact;
        } else {
            return null;
        }
    }

    //This method will add/create a new contact
    public void addContact(String name, String phone, String email) {
        ContentValues values = new ContentValues();
        values.put(dbHelper.COLUMN_NAME, name);
        values.put(dbHelper.COLUMN_PHONE, phone);
        values.put(dbHelper.COLUMN_EMAIL, email);

        long result = database.insert(dbHelper.TABLE_CONTACTS, null, values);

        if (result == -1) {
            // Insert failed
            Log.e("ContactDataAccessObject", "Failed to insert contact");
        } else {
            // Insert successful
            Log.i("ContactDataAccessObject", "Contact inserted with ID: " + result);
        }
    }


    public void updateContact() {
    }
}
