package com.example.notesapp2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class NotesCursorAdapter extends CursorAdapter {


    public NotesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //inflate layout note_list_item
        return LayoutInflater.from(context).inflate(
                R.layout.note_list_item,
                parent,
                false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //When you bind the view, you receive an instance of the cursor object, and it will already point to the particular row of your database that's supposed to be displayed.
        String noteText = cursor.getString(
                cursor.getColumnIndex(DBHelper.NOTE_TEXT)
        );

        //check whether there's a line feed
        int pos = noteText.indexOf(10);//10 is ASCII value of line feed character
        if (pos != -1) {
            noteText = noteText.substring(0, pos) + " ...";
        }

        TextView textView = view.findViewById(R.id.tvNote);
        textView.setText(noteText);
    }
}
