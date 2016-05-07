package com.gmail.jmkemper.anagrams;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

public class WordProcessor {

    public static void importWords(InputStream file, SQLiteDatabase db) throws IOException {
        InputStreamReader stream = new InputStreamReader(file);
        BufferedReader reader = new BufferedReader(stream);
        String word;
        while((word = reader.readLine()) != null){
            int length = word.length();
            if(length > 1) {
                try {
                    if (db.rawQuery(WordDatabase.SQL_SELECT(word), null).getCount() == 0) {
                        String anagramed = anagramWord(word);
                        db.execSQL(WordDatabase.SQL_INSERT(word, length, anagramed, 0));
                    }
                } catch (Exception e){
                    Log.e("exec", e.toString());
                }
            }
        }
    }

    public HashMap<Integer, HashMap<String, String>> processWordFile(InputStream file) throws IOException {
        HashMap<Integer, HashMap<String, String>> wordList = new HashMap<>();
        InputStreamReader stream = new InputStreamReader(file);
        BufferedReader reader = new BufferedReader(stream);
        String word;

        while((word = reader.readLine()) != null){
            int length = word.length();
            if(length > 1) {
                if (wordList.get(length) != null) {
                    HashMap<String, String> array = wordList.get(length);
                    String anagramed = anagramWord(word);
                    array.put(word, anagramed);
                    wordList.put(length, array);
                } else {
                    HashMap<String, String> array = new HashMap<>();
                    String anagramed = anagramWord(word);
                    array.put(word, anagramed);
                    wordList.put(length, array);
                }
            }
        }

        return wordList;
    }

    private static String anagramWord(String word){
        char [] chararray = word.toCharArray();

        do {
            Random r = new Random();
            for (int i = chararray.length - 1; i > 0; i--) {
                int index = r.nextInt(i + 1);
                char c = chararray[index];
                chararray[index] = chararray[i];
                chararray[i] = c;
            }
        } while (word.equals(new String(chararray)));

        return new String(chararray);
    }
}
