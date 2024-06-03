package com.example.mycontactsmidtermrodriguez;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* I'm creating this dbHelper class that extends SQLiteOpenHelper to
manage creation and version management of the SQLite database that stores contact information.
* */

/*The database configuration defines the constants for the database name*/
public class dbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    //Database creation SQL
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CONTACTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_PHONE
            + " text not null, " + COLUMN_EMAIL
            + " text not null);";

    //Constructor that calls the parent SQLiteOpenHelper constructor with the context. This set up the helper to manage the database
    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //This method is called when the database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    //This method ensures the database schema changes are updated. It drops the old table and creates a new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle any database upgrade as necessary
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }
}
