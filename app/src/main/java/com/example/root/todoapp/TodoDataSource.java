package com.example.root.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitul on 16/6/16.
 */
public class TodoDataSource {

    private SQLiteDatabase database;
    private TodoItemDatabase dbHelper;

    private String[] allColumns = {TodoItemDatabase.COLUMN_ID, TodoItemDatabase.COLUMN_TODO_TITLE,
            TodoItemDatabase.COLUMN_TODO_DESC, TodoItemDatabase.COLUMN_DUE_DATE, TodoItemDatabase.COLUMN_CREATE_DATE,
            TodoItemDatabase.COLUMN_PRIORITY, TodoItemDatabase.COLUMN_STATUS, TodoItemDatabase.COLUMN_DELETED};

    public TodoDataSource(Context context){
        dbHelper = new TodoItemDatabase(context);
    }

    public void open() throws SQLException {                
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Todo createTodo(Todo todo){

        ContentValues values = new ContentValues();

        values.put(TodoItemDatabase.COLUMN_TODO_TITLE, todo.getTodoTitle());
        values.put(TodoItemDatabase.COLUMN_TODO_DESC, todo.getTodoDesc());
        values.put(TodoItemDatabase.COLUMN_DUE_DATE, todo.getDueDate());
        values.put(TodoItemDatabase.COLUMN_CREATE_DATE, todo.getCreateDate());
        values.put(TodoItemDatabase.COLUMN_PRIORITY, todo.getPriority());
        values.put(TodoItemDatabase.COLUMN_STATUS, todo.getStatus());
        values.put(TodoItemDatabase.COLUMN_DELETED, todo.getDeleted()+"");

        long insertId = database.insert(TodoItemDatabase.TABLE_TODOS, null, values);
        Cursor cursor = database.query(TodoItemDatabase.TABLE_TODOS, allColumns, TodoItemDatabase.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        Todo newTodo = cursorToTodo(cursor);

        cursor.close();

        return newTodo;
    }

    public int deleteTodo(Todo todo){
        long id = todo.get_id();
        System.out.println("Todo deleted with id : " + id);
        return database.delete(TodoItemDatabase.TABLE_TODOS, TodoItemDatabase.COLUMN_ID + " = " + id, null);
    }

    public Todo getTodo(String id) {
        String where = "_id = ?";
        long idInt = Integer.parseInt(id);
        String whereargs[] = {idInt + ""};

        Cursor cursor = database.query(TodoItemDatabase.TABLE_TODOS, allColumns,where, whereargs, null, null, null);
        cursor.moveToFirst();
        return cursorToTodo(cursor);
    }

    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<Todo>();
        Cursor cursor = database.query(TodoItemDatabase.TABLE_TODOS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Todo todo = cursorToTodo(cursor);
            todos.add(todo);
            cursor.moveToNext();
        }

        cursor.close();
        return todos;
    }

    public boolean updateTodo(Todo todo) {
        ContentValues values = new ContentValues();
        values.put(TodoItemDatabase.COLUMN_TODO_TITLE, todo.getTodoTitle());
        values.put(TodoItemDatabase.COLUMN_TODO_DESC, todo.getTodoDesc());
        values.put(TodoItemDatabase.COLUMN_DUE_DATE, todo.getDueDate());
        values.put(TodoItemDatabase.COLUMN_PRIORITY, todo.getPriority());
        values.put(TodoItemDatabase.COLUMN_STATUS, todo.getStatus());
        String whereargs [] = {todo.get_id() + ""};
        String strFilter = "_id=" + todo.get_id();
        database.update(TodoItemDatabase.TABLE_TODOS, values, strFilter, null);
        return true;
    }


    private Todo cursorToTodo(Cursor cursor) {
        Todo todo = new Todo();
        todo.set_id(cursor.getLong(0));
        System.out.println("Fetched id : " + todo.get_id());
        todo.setTodoTitle(cursor.getString(1));
        todo.setTodoDesc(cursor.getString(2));
        todo.setDueDate(cursor.getString(3));
        todo.setCreateDate(cursor.getString(4));
        todo.setPriority(cursor.getString(5));
        todo.setStatus(cursor.getString(6));
        todo.setDeleted(Integer.parseInt(cursor.getString(7)));

        return todo;
    }


}
