package com.example.root.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by patel on 16/6/16.
 */
public class TodoItemDatabase extends SQLiteOpenHelper {


    public static final String TABLE_TODOS = "todos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TODO_TITLE = "todoTitle";
    public static final String COLUMN_TODO_DESC = "todoDesc";
    public static final String COLUMN_DUE_DATE = "dueDate";
    public static final String COLUMN_CREATE_DATE = "createDate";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DELETED = "deleted";

    private static final String DATABASE_NAME = "todos.db";
    private static final int DATABASE_VERSION = 1;


    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TODOS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TODO_TITLE
            + " text not null, " + COLUMN_TODO_DESC + " text not null, "
            + COLUMN_DUE_DATE + " text not null, " + COLUMN_CREATE_DATE + " text not null, "
            + COLUMN_PRIORITY + " text not null, " + COLUMN_STATUS + " text not null, "
            + COLUMN_DELETED + ");";

    public TodoItemDatabase(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(TodoItemDatabase.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        onCreate(db);

    }
}
