package com.gmail.jmkemper.anagrams;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Jim on 2016-05-03.
 */
public class WordDatabase {

    public WordDatabase() {}

    public static abstract class Words implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_NAME_WORD = "word";
        public static final String COLUMN_NAME_LENGTH = "length";
        public static final String COLUMN_NAME_ANAGRAM = "anagram";
        public static final String COLUMN_NAME_SOLVED= "solved";
    }

    public static final String TEXT_TYPE = " VARCHAR";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + WordDatabase.Words.TABLE_NAME + " (" +
                    WordDatabase.Words.COLUMN_NAME_WORD +  TEXT_TYPE + " PRIMARY KEY," +
                    WordDatabase.Words.COLUMN_NAME_LENGTH + " INTEGER" + COMMA_SEP +
                    WordDatabase.Words.COLUMN_NAME_ANAGRAM + TEXT_TYPE + COMMA_SEP +
                    WordDatabase.Words.COLUMN_NAME_SOLVED + " INTEGER" +
                    " )";

    public static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + WordDatabase.Words.TABLE_NAME;

    public static String SQL_INSERT(String word, int length, String anagram, int solved){
        return "INSERT INTO " + Words.TABLE_NAME + " VALUES " +
                "(" +
                "'" + word + "'" +  ", " +
                length + ", " +
                "'" + anagram + "'" + ", " +
                solved +
                ");";
    }

    public static String SQL_SELECT(String word){
        return "SELECT * FROM " + Words.TABLE_NAME +
                " WHERE " + Words.COLUMN_NAME_WORD +
                " = " + "'" + word + "'";
    }

    public static String SQL_WORD_LENGTHS = "SELECT DISTINCT " +
            Words.COLUMN_NAME_LENGTH + " FROM " +
            Words.TABLE_NAME +
            " ORDER BY " + Words.COLUMN_NAME_LENGTH + " ASC";

    public static String SQL_GET_WORDS(int length){
        return "SELECT * FROM " + Words.TABLE_NAME +
                " WHERE " + Words.COLUMN_NAME_LENGTH + "=" + length;
    }

    public static String SQL_GET_WORD_FROM_ANAGRAM(String anagram){
        return "SELECT " + Words.COLUMN_NAME_WORD + " FROM " +
                Words.TABLE_NAME + " WHERE " + Words.COLUMN_NAME_ANAGRAM + "=" +
                "'" + anagram + "'";
    }

    public static String SQL_SET_SOLVED(String word){
        return "UPDATE " + Words.TABLE_NAME +
                " SET " + Words.COLUMN_NAME_SOLVED + "=1" +
                " WHERE " + Words.COLUMN_NAME_WORD + "=" +
                "'" + word + "'";
    }

}
