package com.example.os;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
//Creating table
public class DatabaseAdapter extends SQLiteOpenHelper {
  //Declaration of attributes(columns)
    public static final String DATABASE_NAME = "OS.db";
    public static final String TABLE_NAME = "OS_PC";
    public static final String COL_1 = "Buffer_Size";
    public static final String COL_2 = "Thread";
    public static final String COL_3 = "At_Index";
    public static final String COL_4 = "Feedback";

  //call databaseadapter by passing four argument
    public DatabaseAdapter(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

  //Overriden method of SQLiteDatabase and create a table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (Buffer_Size INTEGER,Thread TEXT,At_Index INTEGER,Feedback INTEGER)");
    }

  //Overriden method of SQLiteDatabase and update the table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
  //Overriden method of SQLiteDatabase and use for to insert the data

    public void insertData(int Size, String T,int i)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,Size);
        contentValues.put(COL_2,T);
        contentValues.put(COL_3,i);
        long result = db.insert(TABLE_NAME,null,contentValues);
    }
  //Inserting a new row for feedback in database
    public boolean insertFeedback(String Feedback)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_4,Feedback);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
}
