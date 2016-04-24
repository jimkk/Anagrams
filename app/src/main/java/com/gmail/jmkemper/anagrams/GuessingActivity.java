package com.gmail.jmkemper.anagrams;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GuessingActivity extends AppCompatActivity {

    private String word;
    private int wordLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guessing);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();

        wordLength = intent.getIntExtra(MainActivity.WORDLENGTH, 0);
        if(wordLength == 0){
            //TODO Something
        }

        try {
            word = getRandomWord(wordLength);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        String anagramed = null;
        do {
            anagramed = anagramWord(word);
        } while(anagramed.equals(word));

        TextView guessWord = (TextView) findViewById(R.id.guess_word);
        assert guessWord != null;
        guessWord.setText(anagramed);


    }

    private String getRandomWord(int length) throws IOException, ClassNotFoundException {
        FileInputStream fis = getApplicationContext().openFileInput("wordlist.obj");
        ObjectInputStream ois = new ObjectInputStream(fis);

        HashMap wordList = (HashMap) ois.readObject();

        ArrayList array = (ArrayList) wordList.get(length);

        Random r = new Random();
        int index = r.nextInt(array.size());

        return (String) array.get(index);
    }

    private String anagramWord(String word){
        char []chararray = word.toCharArray();

        Random r = new Random();
        for(int i = chararray.length-1; i > 0; i--){
            int index = r.nextInt(i+1);
            char c = chararray[index];
            chararray[index] = chararray[i];
            chararray[i] = c;
        }

        return new String(chararray);
    }

    public void submit(View view){
        TextView textView = (TextView) findViewById(R.id.guessed_word);
        assert textView != null;
        String guess = textView.getText().toString();

        if(guess.equals(word)) {
            textView.setText(R.string.correct);
        } else {
            textView.setText("");
            textView.setHint(R.string.incorrect);
        }
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void newWord(View view){
        TextView textView = (TextView) findViewById(R.id.guess_word);
        EditText guess = (EditText) findViewById(R.id.guessed_word);
        try {
            word = getRandomWord(wordLength);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        String anagramed = anagramWord(word);
        assert textView != null;
        textView.setText(anagramed);
        assert guess != null;
        guess.setText("");
    }

    public void back(View view){
        Intent intent = new Intent(this, LengthSelection.class);
        startActivity(intent);
    }
}
