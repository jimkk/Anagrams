package com.gmail.jmkemper.anagrams;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jim on 2016-05-06.
 */
public class WordListAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private List<String> objects;
    private WordSelection ws;

    public WordListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    public void addParent(WordSelection parent){
        ws = parent;
    }

    @Override
    public int getCount(){
        return objects.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Anagrams.Adapter", "View Requested");
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(resource, parent, false);
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ws.guessWord(view);
            }
        });

        String element = objects.get(position);

        if(element != null){
            Log.d("Anagrams.Adapter", "Processed word: " + element);
            TextView text = (TextView) v.findViewById(R.id.rowTextView);
            assert text != null;
            if(element.startsWith(" ")) {
                element = element.replaceAll(" ", "");
                text.setText(element);
                text.setTextColor(Color.parseColor("#00f211"));
                text.setTypeface(Typeface.DEFAULT_BOLD);
                text.setEnabled(false);
            } else {
                text.setText(element);
                text.setTextColor(Color.parseColor("#111111"));
                text.setTypeface(Typeface.DEFAULT);
                text.setEnabled(true);
            }

        }

        return v;
    }
}
