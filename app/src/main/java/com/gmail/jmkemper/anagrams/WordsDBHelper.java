package com.gmail.jmkemper.anagrams;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jim on 2016-05-03.
 */
public class WordsDBHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABLE =
            "CREATE_TABLE" + WordDatabase.Words.TABLE_NAME + " (" +
                    WordDatabase.Words.COLUMN_NAME_WORD + " TEXT PRIMARY KEY," +
                    WordDatabase.Words.COLUMN_NAME_LENGTH + " INTEGER" + COMMA_SEP +
                    WordDatabase.Words.COLUMN_NAME_ANAGRAM + TEXT_TYPE + COMMA_SEP +
                    WordDatabase.Words.COLUMN_NAME_SOLVED + " BOOLEAN" +
                    " )";

    private static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + WordDatabase.Words.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Words.db";

    public WordsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
