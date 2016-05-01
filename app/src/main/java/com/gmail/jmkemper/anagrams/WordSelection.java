package com.gmail.jmkemper.anagrams;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordSelection extends AppCompatActivity {

    HashMap<String, String> scrambledWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_selection);

        scrambledWords = new HashMap<>();

        Intent intent = getIntent();

        int length = intent.getIntExtra(MainActivity.WORDLENGTH, 0);

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<String> words = null;
        try {
            words = getWords(length);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        assert words != null;
        ArrayAdapter<String> array = new ArrayAdapter<>(this, R.layout.word_element, words);
        assert listView != null;
        listView.setAdapter(array);
        try {
            for (int i = 0; i < listView.getCount(); i++) {
                View v = listView.getChildAt(i - listView.getFirstVisiblePosition());
                if(v == null) {
                    Log.d("exec", "index " + i + " is null");
                } else {
                    TextView tv = (TextView) v.findViewById(R.id.rowTextView);
                    if (tv.getText().toString().startsWith(" ")) {
                        tv.setTextColor(Color.GREEN);
                    }
                }
            }
        } catch (Exception e){
            Log.e("exec", e.toString());
        }
    }


    private ArrayList<String> getWords(int length) throws IOException, ClassNotFoundException {
        ArrayList<String> wordArray = new ArrayList<>();
        try {

            FileInputStream fis = getApplicationContext().openFileInput("wordlist.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);


            HashMap wordList = (HashMap) ois.readObject();
            HashMap words = (HashMap) wordList.get(length);

            for (Object entry : words.entrySet()) {
                Map.Entry e = (Map.Entry) entry;

                String word = (String) e.getKey();
                String anagramed = (String) e.getValue();

                if (word.equals(anagramed)) {
                    wordArray.add(" " + word);
                } else {
                    wordArray.add(anagramed);
                }
                scrambledWords.put(anagramed, word);

            }

        }catch (Exception e){
            Log.e("exec", e.toString());
        }
        return wordArray;
    }

    public void guessWord(View view){
        TextView textView = (TextView) view.findViewById(view.getId());
        String word = textView.getText().toString();
        Intent intent = new Intent(this, GuessingActivity.class);
        intent.putExtra(MainActivity.WORD, scrambledWords.get(word));
        intent.putExtra(MainActivity.ANAGRAM, word);
        Log.d("exec", "Word: " + scrambledWords.get(word));
        Log.d("exec", "Anagram: " + word);
        startActivity(intent);

    }
}
