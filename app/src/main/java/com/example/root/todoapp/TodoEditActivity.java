package com.example.root.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class TodoEditActivity extends AppCompatActivity {

    EditText dueDate, todoTitle, todoDescription;
    Spinner priorityDropDown, statusDropDown;

    String priorityDDItems[] = new String[]{"Low","Medium","High"};
    String statusDDItems[] = new String[]{"To-Do", "Done"};

    private TodoDataSource datasource;

    private String todoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);

        setupDB();

        dueDate = (EditText) findViewById(R.id.edit_dueDate);
        todoTitle = (EditText) findViewById(R.id.edit_todo_title);
        todoDescription = (EditText) findViewById(R.id.edit_todo_desc);

        priorityDropDown = (Spinner) findViewById(R.id.edit_priority);
        statusDropDown = (Spinner) findViewById(R.id.edit_status);

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, priorityDDItems);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, statusDDItems);
        priorityDropDown.setAdapter(priorityAdapter);
        statusDropDown.setAdapter(statusAdapter);

        Intent intent = getIntent();
        todoId = intent.getStringExtra("todoId");
        setItems(todoId);

    }

    private void setupDB() {
        datasource = new TodoDataSource(this);
        datasource.open();
    }

    private void setItems(String todoId) {
        Todo editTodo = datasource.getTodo(todoId);
        dueDate.setText(editTodo.getDueDate());
        todoTitle.setText(editTodo.getTodoTitle());
        todoDescription.setText(editTodo.getTodoDesc());

        switch (editTodo.getPriority()) {
            case "Low":
                priorityDropDown.setSelection(0);
                break;
            case "Medium":
                priorityDropDown.setSelection(1);
                break;
            case "High":
                priorityDropDown.setSelection(2);
                break;
            default:
                break;
        }

        switch (editTodo.getStatus()){
            case "To-Do":
                statusDropDown.setSelection(0);
                break;
            case "Done":
                statusDropDown.setSelection(1);
                break;
            default:
                break;
        }

    }

    public void onEditTodo(View view) {

        Todo newUpdatedTodo = new Todo();
        newUpdatedTodo.set_id(Long.parseLong(todoId));
        newUpdatedTodo.setDeleted(0);
        newUpdatedTodo.setStatus(statusDropDown.getSelectedItem().toString());
        newUpdatedTodo.setPriority(priorityDropDown.getSelectedItem().toString());
        newUpdatedTodo.setDueDate(dueDate.getText().toString());
        newUpdatedTodo.setTodoDesc(todoDescription.getText().toString());
        newUpdatedTodo.setTodoTitle(todoTitle.getText().toString());

        datasource.updateTodo(newUpdatedTodo);

        Intent i = new Intent(TodoEditActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
