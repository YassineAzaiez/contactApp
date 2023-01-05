package com.example.contactapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.contactapp.models.Contact;

/**
 * Created by yassine 02/02/20 .
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DataBaseHelper";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    String sql = DatabaseContract.contacts_table.CREATE_TABLE;
    db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          db.execSQL(DatabaseContract.contacts_table.DELETE_TABLE);
          onCreate(db);
    }

    public long addContact(Contact contact){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.contacts_table.COLUMN_NAME_COL1,contact.getName());
        contentValues.put(DatabaseContract.contacts_table.COLUMN_NAME_COL2,contact.getPhonenumber());
        contentValues.put(DatabaseContract.contacts_table.COLUMN_NAME_COL3,contact.getDevice());
        contentValues.put(DatabaseContract.contacts_table.COLUMN_NAME_COL4,contact.getEmail());
        contentValues.put(DatabaseContract.contacts_table.COLUMN_NAME_COL5,contact.getProfileImage());






        long result = db.insert(DatabaseContract.contacts_table.TABLE_NAME,null,contentValues);

        return result;
    }

    public Cursor getAllContacts(){
        SQLiteDatabase db = this.getWritableDatabase();;
             return db.rawQuery("SELECT * FROM "+DatabaseContract.contacts_table.TABLE_NAME
                     ,null);
    }

    public long updateContact(Contact contact, int id){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.contacts_table.COLUMN_NAME_COL1,contact.getName());
        contentValues.put(DatabaseContract.contacts_table.COLUMN_NAME_COL2,contact.getPhonenumber());
        contentValues.put(DatabaseContract.contacts_table.COLUMN_NAME_COL3,contact.getDevice());
        contentValues.put(DatabaseContract.contacts_table.COLUMN_NAME_COL4,contact.getEmail());
        contentValues.put(DatabaseContract.contacts_table.COLUMN_NAME_COL5,contact.getProfileImage());

        long result = db.update(DatabaseContract.contacts_table.TABLE_NAME,contentValues
                ,DatabaseContract.contacts_table._ID+" = ? "
                ,new String[]{String.valueOf(id)});
        return  result;

    }

    public Cursor getContactId(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM "+DatabaseContract.contacts_table.TABLE_NAME+" WHERE NAME = '"
                +contact.getName()+"' AND  "+DatabaseContract.contacts_table.COLUMN_NAME_COL2+" = '"
                +contact.getPhonenumber()+"'";

        return  db.rawQuery(sql,null);
    }

    public Integer deleteContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DatabaseContract.contacts_table.TABLE_NAME, "_ID = ?", new String[] {String.valueOf(id)});
    }


}
