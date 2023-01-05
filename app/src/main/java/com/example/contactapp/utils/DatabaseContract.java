package com.example.contactapp.utils;

import android.provider.BaseColumns;

/**
 * Created by yassine 02/02/20 .
 */
public final class DatabaseContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contact.db";
    private static final String TEXT_TYPE = " TEXT ";
    private static final String COMMA_SEP = ",";


    private DatabaseContract() {
    }

    public static abstract class contacts_table implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_COL1 = "NAME";
        public static final String COLUMN_NAME_COL2 = "PHONE_NUMBER";
        public static final String COLUMN_NAME_COL3 = "DEVICE";
        public static final String COLUMN_NAME_COL4 = "EMAIL";
        public static final String COLUMN_NAME_COL5 = "PROFILE_IMAGE";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_COL1 + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_COL2 + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_COL3 + TEXT_TYPE+ COMMA_SEP +
                COLUMN_NAME_COL4 + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_COL5 + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}