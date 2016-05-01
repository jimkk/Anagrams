package com.gmail.jmkemper.anagrams;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
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
            if(!getApplicationContext().getFileStreamPath("wordlist.obj").exists() || resetFlag){
                Log.d("exec", "No Word List Found. New one generated");
                AssetManager assets = getAssets();
                WordProcessor wp = new WordProcessor();
                HashMap<Integer, HashMap<String, String>> array = wp.processWordFile(assets.open("wordlist.txt"));
                FileOutputStream fos = getApplicationContext().openFileOutput("wordlist.obj", Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(array);
                oos.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("exec", "Processed Word List");
    }

    public void playGame(View view){
        Intent intent = new Intent(this, LengthSelection.class);
        startActivity(intent);
    }
}
