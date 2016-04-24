package com.gmail.jmkemper.anagrams;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final String WORDLIST = "com.gmail.jmkemper.anagrams.WORDLIST";
    public static final String WORDLENGTH = "com.gmail.jmkemper.anagrams.WORDLENGTH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WordProcessor wp = new WordProcessor();
        try {
            AssetManager assets = getAssets();
            HashMap<Integer, ArrayList<String>> array = wp.processWordFile(assets.open("wordlist.txt"));
            FileOutputStream fos = getApplicationContext().openFileOutput("wordlist.obj", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playGame(View view){
        Intent intent = new Intent(this, LengthSelection.class);
        startActivity(intent);
    }
}
