package com.gmail.jmkemper.anagrams;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LengthSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_length_selection);

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<String> wordLengths = getWordLengths();

        assert wordLengths != null;
        ArrayAdapter<String> array = new ArrayAdapter<>(this, R.layout.length_element, wordLengths);
        assert listView != null;
        listView.setAdapter(array);
    }

    private ArrayList<String> getWordLengths() {
        ArrayList<String> lengths = new ArrayList<>();
        try {
            SQLiteDatabase db = openOrCreateDatabase(WordDatabase.Words.TABLE_NAME, MODE_PRIVATE, null);

            Cursor resultSet = db.rawQuery(WordDatabase.SQL_WORD_LENGTHS, null);
            if(resultSet.getCount() == 0){
                Log.e("exec", "Words table is empty");
                System.exit(-1);
            }
            resultSet.moveToFirst();
            while(!resultSet.isLast()) {
                int length = resultSet.getInt(0);
                lengths.add(Integer.toString(length));
                resultSet.moveToNext();
            }

            resultSet.close();
            db.close();

        } catch (Exception e){
            Log.e("exec", e.toString());
        }
        Log.d("exec", "Word Lengths Processed");
        return lengths;
    }

    public void lengthChoice(View view){
        TextView textView = (TextView) view.findViewById(view.getId());
        int length = Integer.parseInt(textView.getText().toString());
        Log.d("button_pushed", Integer.toString(length));
        Intent intent = new Intent(this, WordSelection.class);
        intent.putExtra(MainActivity.WORDLENGTH, length);
        startActivity(intent);
    }


}
