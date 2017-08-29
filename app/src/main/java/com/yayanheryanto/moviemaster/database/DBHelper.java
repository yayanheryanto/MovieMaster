package com.yayanheryanto.moviemaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yayanheryanto.moviemaster.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.yayanheryanto.moviemaster.database.DBContract.COLUMN_BACKDROP_PATH;
import static com.yayanheryanto.moviemaster.database.DBContract.COLUMN_ID;
import static com.yayanheryanto.moviemaster.database.DBContract.COLUMN_MOVIE_NAME;
import static com.yayanheryanto.moviemaster.database.DBContract.COLUMN_OVERVIEW;
import static com.yayanheryanto.moviemaster.database.DBContract.COLUMN_POSTER_PATH;
import static com.yayanheryanto.moviemaster.database.DBContract.COLUMN_RATING;
import static com.yayanheryanto.moviemaster.database.DBContract.COLUMN_RELASE_DATE;
import static com.yayanheryanto.moviemaster.database.DBContract.DATABASE_NAME;
import static com.yayanheryanto.moviemaster.database.DBContract.TABLE_NAME;

/**
 * Created by Yayan Heryanto on 6/10/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME  + "("
            + COLUMN_ID  + " INTEGER PRIMARY KEY ,"
            + COLUMN_MOVIE_NAME + " TEXT, "
            + COLUMN_POSTER_PATH + " TEXT,"
            + COLUMN_BACKDROP_PATH + " TEXT, "
            + COLUMN_RATING + " VARCHAR, "
            + COLUMN_RELASE_DATE + " VARCHAR, "
            + COLUMN_OVERVIEW + " TEXT)";

    private ContentValues values;
    private Cursor cursor;
    private SQLiteDatabase database;

    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;
    private Context context;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }

    public void addFavoriteMovie(Movie movie){
        database = this.getWritableDatabase();
        String checkId = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " =?";
        Cursor cursor = database.rawQuery(checkId, new String[] {String.valueOf(movie.getId())});

        boolean isExists = false;
        if(cursor.moveToFirst()){
            isExists = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d("Databases", String.format("%d records found", count));
            Log.d("Databases", "Sudah ada di favorit");
            //Toast.makeText(context, "Movie Sudah Masuk dalam Favorit", Toast.LENGTH_SHORT).show();

        }else{
            values = new ContentValues();
            values.put(COLUMN_ID, movie.getId());
            values.put(COLUMN_MOVIE_NAME, movie.getTitle());
            values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
            values.put(COLUMN_BACKDROP_PATH, movie.getBackdropPath());
            values.put(COLUMN_RATING, String.valueOf(movie.getVoteAverage()));
            values.put(COLUMN_RELASE_DATE, movie.getReleaseDate());
            values.put(COLUMN_OVERVIEW, movie.getOverview());
            database.insert(TABLE_NAME, null, values);
        }

        cursor.close();
        database.close();

    }

    public List<Movie> getAllMovie() {
        List<Movie> movie = new ArrayList<Movie>();
        String query = "SELECT * FROM " + TABLE_NAME;

        database = this.getReadableDatabase();
        cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Movie model = new Movie();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setTitle(cursor.getString(1));
                model.setPosterPath(cursor.getString(2));
                model.setBackdropPath(cursor.getString(3));
                model.setVoteAverage(Double.valueOf(cursor.getString(4)));
                model.setReleaseDate(cursor.getString(5));
                model.setOverview(cursor.getString(6));
                movie.add(model);
            } while (cursor.moveToNext());
        }

        return movie;
    }

    public void deleteFavoriteMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[] { String.valueOf(movie.getId()) });
        db.close();
    }

//    public int getMovieCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }

}
