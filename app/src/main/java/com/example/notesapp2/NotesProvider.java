package com.example.notesapp2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NotesProvider extends ContentProvider {

    //unique string that identifies the content provider to the Android framework
    private static final String AUTHORITY = "com.example.notesapp2.notesprovider";
    private static final String BASE_PATH = "notes";

    //resource identifier that identifies the content provider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    //Constants to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); //class that parse a URI and then tell which operation has been requested

    public static final String CONTENT_ITEM_TYPE = "Note";

    static {
        //this block will execute the first time anything is called from this class
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID); // # is a wild card, it means any numerical value.
        // that means if I get a URI that starts with base_path and then ends with a / and a number that means I'm looking
        //for a particular note, a particular row in the database table.

    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBHelper helper = new DBHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (uriMatcher.match(uri) == NOTES_ID) {
            selection = DBHelper.NOTE_ID + "=" + uri.getLastPathSegment();
        }

        //get data from table
        return database.query(DBHelper.TABLE_NOTES, DBHelper.ALL_COLUMNS, selection, null, null, null, DBHelper.NOTE_CREATED + " DESC");
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // return a URI that match pattern: the base_path followed by
        // '/' and then the id of the record

        // get id value
        long id = database.insert(DBHelper.TABLE_NOTES, null, values);//ContentValues is a class that has a collection of name value pairs
        //ContentValues is used to pass data around on the back end.

        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete(DBHelper.TABLE_NOTES, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update(DBHelper.TABLE_NOTES, values, selection, selectionArgs);
    }
}
