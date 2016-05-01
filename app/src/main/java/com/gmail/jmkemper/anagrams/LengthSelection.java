package com.gmail.jmkemper.anagrams;

import android.content.Intent;
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
        ArrayList<String> wordLengths = null;
        try {
            wordLengths = getWordLengths();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        assert wordLengths != null;
        ArrayAdapter<String> array = new ArrayAdapter<String>(this, R.layout.length_element, wordLengths);
        assert listView != null;
        listView.setAdapter(array);
    }

    private ArrayList<String> getWordLengths() throws IOException, ClassNotFoundException {
        ArrayList<Integer> lengths = new ArrayList<>();

        FileInputStream fis = getApplicationContext().openFileInput("wordlist.obj");
        ObjectInputStream ois = new ObjectInputStream(fis);

        HashMap wordList = (HashMap) ois.readObject();

        for (Object o : wordList.keySet()) {
            int key = (int) o;
            lengths.add(key);
        }
        Collections.sort(lengths);

        ArrayList<String> lengthsStrings = new ArrayList<>();
        for(Integer k : lengths){
            lengthsStrings.add(Integer.toString(k));
        }

        return lengthsStrings;
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
