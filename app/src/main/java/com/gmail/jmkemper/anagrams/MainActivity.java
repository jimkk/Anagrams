package com.gmail.jmkemper.anagrams;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final String WORDLIST = "com.gmail.jmkemper.anagrams.WORDLIST";
    public static final String WORDLENGTH = "com.gmail.jmkemper.anagrams.WORDLENGTH";
    public static final String WORD = "com.gmail.jmkemper.anagrams.WORD";
    public static final String ANAGRAM = "com.gmail.jmkemper.anagrams.ANAGRAM";

    private boolean resetFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if(!checkIfDatabaseExists(WordDatabase.Words.TABLE_NAME)|| resetFlag){
                SQLiteDatabase db = openOrCreateDatabase(WordDatabase.Words.TABLE_NAME, MODE_PRIVATE, null);
                db.execSQL(WordDatabase.SQL_DELETE);
                db.execSQL(WordDatabase.SQL_CREATE_TABLE);
                Log.d("exec", "No Word List Found. New one generated");
                AssetManager assets = getAssets();
                WordProcessor wp = new WordProcessor();
                WordProcessor.importWords(assets.open("wordlist.txt"), db);
                //HashMap<Integer, HashMap<String, String>> array = wp.processWordFile(assets.open("wordlist.txt"));
                //FileOutputStream fos = getApplicationContext().openFileOutput("wordlist.obj", Context.MODE_PRIVATE);
                //ObjectOutputStream oos = new ObjectOutputStream(fos);
                //oos.writeObject(array);
                //oos.close();
                //fos.close();
            }
            try {
                SQLiteDatabase db = openOrCreateDatabase(WordDatabase.Words.TABLE_NAME, MODE_PRIVATE, null);
                Log.d("exec", db.rawQuery("SELECT * FROM " + WordDatabase.Words.TABLE_NAME, null).toString());
            } catch (Exception e){
                Log.e("exec", e.toString());
            }
        } catch (IOException e) {
            Log.e("exec", e.toString());
        }
        Log.d("exec", "Processed Word List");
    }

    public void playGame(View view){
        Intent intent = new Intent(this, LengthSelection.class);
        startActivity(intent);
    }

    private boolean checkIfDatabaseExists(String name){
        File dbFile = getApplicationContext().getDatabasePath(name);
        return dbFile.exists();
    }
}
