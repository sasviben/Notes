package com.example.notesapp2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.notesapp.DBOpenHelper;

public class NotesProvider extends ContentProvider {

    //Globalni jedinstveni string za identifikaciju content providera Androidu
    private static final String AUTHORITY = "com.example.notesapp.notesprovider";

    //Potpuni set podataka
    private static final String BASE_PATH = "notes";

    //Resource identifikator koji identificira content provider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    //Konstante za identifikaciju operacija. NOTES zahtjeva podatke
    private static final int NOTES = 1;
    //Notes_ID upravlja samo sa jednim zapisom
    private static final int NOTES_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); //parsira URI i govori koja operacija je zatražena

    static {
        //ovo se izvrašava prvo kad je bilo šta pozvano u ovoj klasi
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID); // # bilo koji broj. Ako dobijem uri koji pocinje sa base_path i zatim zavrsava sa / i brojem, to znaci da trazim odredenu biljesku
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        com.example.notesapp.DBOpenHelper helper = new com.example.notesapp.DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //dohvati podatke iz stupca iz notes baze
        return database.query(DBOpenHelper.TABLE_NOTES, DBOpenHelper.ALL_COLUMNS, selection, null, null, null, DBOpenHelper.NOTE_CREATED + " DESC");
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //vraća URI koji odgovara peternu:
        //base_path -> / -> primarni ključ zapisa

        //dohvati vrijednost primarnog ključa
        long id = database.insert(DBOpenHelper.TABLE_NOTES, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_NOTES, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_NOTES, values, selection, selectionArgs);
    }
}
