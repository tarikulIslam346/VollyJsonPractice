package com.example.tarikul.volleyjson.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherInfoListDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Weather.db";

    public WeatherInfoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        final  String SQL_CREATE_WEATHERLIST_TABLE = "CREATE TABLE " +
                WeatherInfoListContract.WeatherInfoListEntry.TABLE_NAME + " (" +
                WeatherInfoListContract.WeatherInfoListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                WeatherInfoListContract.WeatherInfoListEntry.COLUMN_COUNTRY + " TEXT NOT NULL, " +
                WeatherInfoListContract.WeatherInfoListEntry.COLUMN_TEMPERATURE + " TEXT NOT NULL"+
                " ); ";
        db.execSQL(SQL_CREATE_WEATHERLIST_TABLE );
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(" DROP TABLE IF EXISTS " + WeatherInfoListContract.WeatherInfoListEntry.TABLE_NAME);
        onCreate(db);
    }
    public String getData(){
        String ds = " ";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + WeatherInfoListContract.WeatherInfoListEntry.TABLE_NAME + "WHERE 1";
        Cursor c = db.rawQuery(query,null);
        while(!c.isAfterLast()) {
          if(c.getString(c.getColumnIndex("COLUMN_TEMPERATURE "))!= null){
              ds += c.getString(c.getColumnIndex("COLUMN_TEMPERATURE "));


          }
        }
        c.close();
        return ds;
    }
}
