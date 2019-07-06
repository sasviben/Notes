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
        //inflate layout note_list_item. Vraća ga natrag kad god newView metoda je pozvana
        return LayoutInflater.from(context).inflate(
                R.layout.note_list_item,
                parent,
                false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Kada se bind-am na view primam instancu cursor objekta koji gleda na redak u bazi koji bi se trebao prikazati
        String noteText = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT)
        );

        int pos = noteText.indexOf(10);
        if(pos != -1){
            noteText = noteText.substring(0, pos) + " ...";
        }

        TextView textView = view.findViewById(R.id.tvNote);
        textView.setText(noteText);
    }
}
