package com.gmail.jmkemper.anagrams;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GuessingActivity extends AppCompatActivity {

    private String word;
    private String anagram;
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guessing);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("5923A48C95EA9CCB7B4DB0C244FE0890")
                .build();
        assert mAdView != null;
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        word = intent.getStringExtra(MainActivity.WORD);
        length = word.length();
        anagram = intent.getStringExtra(MainActivity.ANAGRAM);

        TextView guessWord = (TextView) findViewById(R.id.guess_word);
        assert guessWord != null;
        guessWord.setText(anagram);

        EditText editText = (EditText) findViewById(R.id.guessed_word);
        assert editText != null;
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        i == KeyEvent.KEYCODE_ENTER){
                    try {
                        submit(view);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });


    }


    private void wordSolved() throws IOException, ClassNotFoundException {
        Log.d("exec", "Word Solved");

        SQLiteDatabase db = openOrCreateDatabase(WordDatabase.Words.TABLE_NAME, MODE_PRIVATE, null);
        db.execSQL(WordDatabase.SQL_SET_SOLVED(word));
        db.close();

        Button submit = (Button) findViewById(R.id.submit_button);
        assert submit != null;
        submit.setEnabled(false);
    }

    private void getNextWord() throws IOException, ClassNotFoundException {
        FileInputStream fis = getApplicationContext().openFileInput("wordlist.obj");
        ObjectInputStream ois = new ObjectInputStream(fis);

        HashMap wordList = (HashMap) ois.readObject();

        HashMap array = (HashMap) wordList.get(length);

        boolean foundWord = false;
        for(Object wordIt : array.keySet()){
            if(foundWord){
                word = (String) wordIt;
                anagram = (String) array.get(word);
                length = word.length();
                TextView textView = (TextView) findViewById(R.id.guess_word);
                EditText guess = (EditText) findViewById(R.id.guessed_word);
                assert textView != null;
                textView.setText(anagram);
                assert guess != null;
                guess.setText("");
                break;
            }
            if(word.equals(wordIt)){
                foundWord = true;
            }
        }
    }

    public void submit(View view) throws IOException, ClassNotFoundException {
        TextView textView = (TextView) findViewById(R.id.guessed_word);
        assert textView != null;
        String guess = textView.getText().toString();
        guess = guess.toLowerCase().replaceAll(" ", "");

        if(guess.equals(word)) {
            textView.setText(R.string.correct);
            wordSolved();
        } else {
            textView.setText("");
            textView.setHint(R.string.incorrect);
        }
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void nextWord(View view) throws IOException, ClassNotFoundException {
        getNextWord();
    }

    public void back(View view){
        finish();
    }
}
