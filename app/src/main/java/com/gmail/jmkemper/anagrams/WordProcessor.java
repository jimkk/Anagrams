package com.gmail.jmkemper.anagrams;

import android.content.res.AssetManager;
import android.util.Log;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jim on 2016-04-23.
 */
public class WordProcessor {

    public HashMap<Integer, ArrayList<String>> processWordFile(InputStream file) throws IOException {
        HashMap<Integer, ArrayList<String>> wordList = new HashMap<>();
        InputStreamReader stream = new InputStreamReader(file);
        BufferedReader reader = new BufferedReader(stream);
        String word;

        while((word = reader.readLine()) != null){
            int length = word.length();
            if(wordList.get(length) != null){
                ArrayList<String> array = wordList.get(length);
                array.add(word);
                wordList.put(length, array);
            }
            else {
                ArrayList<String> array = new ArrayList<>();
                array.add(word);
                wordList.put(length, array);
            }
        }

        return wordList;
    }
}
