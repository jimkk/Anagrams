package com.gmail.jmkemper.anagrams;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_selection);

        scrambledWords = new HashMap<>();

        Intent intent = getIntent();

        length = intent.getIntExtra(MainActivity.WORDLENGTH, 0);

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<String> words = null;
        try {
            words = getWords(length);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        assert words != null;
        WordListAdapter array = new WordListAdapter(getApplicationContext(), R.layout.word_element, words);
        array.addParent(this);
        assert listView != null;
        listView.setAdapter(array);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView listView = (ListView) findViewById(R.id.listView);
        assert listView != null;
        int index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
        ArrayList<String> words = null;
        try {
            words = getWords(length);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        assert words != null;
        WordListAdapter array = new WordListAdapter(getApplicationContext(), R.layout.word_element, words);
        array.addParent(this);
        listView.setAdapter(array);
        listView.setSelectionFromTop(index, top);


    }

    private ArrayList<String> getWords(int length) throws IOException, ClassNotFoundException {
        ArrayList<String> words= new ArrayList<>();
        try {
            SQLiteDatabase db = openOrCreateDatabase(WordDatabase.Words.TABLE_NAME, MODE_PRIVATE, null);

            Cursor resultSet = db.rawQuery(WordDatabase.SQL_GET_WORDS(length), null);
            if(resultSet.getCount() == 0){
                Log.e("exec", "Words table is empty");
                System.exit(-1);
            }
            resultSet.moveToFirst();
            while(!resultSet.isLast()) {
                String word = resultSet.getString(0);
                String anagram = resultSet.getString(2);
                if(resultSet.getInt(3) == 0) {
                    words.add(anagram);
                } else {
                    words.add(" " +word);
                }
                scrambledWords.put(anagram, word);

                resultSet.moveToNext();
            }

            resultSet.close();
            db.close();

        } catch (Exception e){
            Log.e("exec", e.toString());
        }
        Log.d("exec", "Words Processed");
        return words;
    }

    public void guessWord(View view){
        TextView textView = (TextView) view.findViewById(view.getId());
        String word = textView.getText().toString();
        Log.d("exec", "Anagram: " + word);
        if(!word.startsWith(" ")) {
            Intent intent = new Intent(this, GuessingActivity.class);
            intent.putExtra(MainActivity.WORD, scrambledWords.get(word));
            intent.putExtra(MainActivity.ANAGRAM, word);
            Log.d("exec", "Word: " + scrambledWords.get(word));
            startActivity(intent);
        }

    }
}
