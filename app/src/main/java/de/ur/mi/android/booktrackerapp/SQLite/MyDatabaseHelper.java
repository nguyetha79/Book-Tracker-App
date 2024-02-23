package de.ur.mi.android.booktrackerapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import de.ur.mi.android.booktrackerapp.Model.BookItemModel;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BookTrackerApp.db";
    private static final int DATABASE_VERSION = 6;

    private static final String TABLE_NAME = "my_bookTrackerApp";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHORS = "authors";
    private static final String COLUMN_COVER = "cover";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_PAGES = "pages";
    private static final String COLUMN_LANG = "language";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_CURR_PAGE = "curr_page";
    private static final String COLUMN_NOTE = "note";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_AUTHORS + " TEXT, " +
                        COLUMN_COVER + " TEXT, " +
                        COLUMN_RATING + " DOUBLE, " +
                        COLUMN_PAGES + " INTEGER, " +
                        COLUMN_LANG + " TEXT, " +
                        COLUMN_STATUS + " TEXT, " +
                        COLUMN_CURR_PAGE + " INTEGER, " +
                        COLUMN_NOTE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addData(String title, String authors, String cover,
                        double rating, int pages, String language,
                        String status, int currPage){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_AUTHORS, authors);
        contentValues.put(COLUMN_COVER, cover);
        contentValues.put(COLUMN_RATING, rating);
        contentValues.put(COLUMN_PAGES, pages);
        contentValues.put(COLUMN_LANG, language);
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_CURR_PAGE, currPage);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String title, String status, int currPage, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_STATUS, status);
        cv.put(COLUMN_CURR_PAGE, currPage);
        cv.put(COLUMN_NOTE, note);

        long result = db.update(TABLE_NAME, cv, "title = '" + title +"'", null);
        if (result == -1){
            Toast.makeText(context, "Failed to update!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
        if (result == -1){
            Toast.makeText(context, "Failed to delete!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(BookItemModel bookItemModel){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(bookItemModel.getId())});
        if (result == -1){
            Toast.makeText(context, "Failed to delete!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
