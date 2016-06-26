package com.example.root.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TodoAddActivity extends AppCompatActivity {

    private int mYear,mMonth,mDay;
    EditText dueDate, todoTitle, todoDesc;
    static final int DATE_DIALOG_ID = 0;

    Spinner priorityDropDown, statusDropDown;

    private TodoDataSource datasource;

    //Items for dropdowns
    String priorityDDItems[] = new String[]{"Low","Medium","High"};
    String statusDDItems[] = new String[]{"To-Do", "Done"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);

        //setting up DB
        setupDB();

        //setting up drop downs
        // priority and status
        priorityDropDown = (Spinner) findViewById(R.id.priority);
        statusDropDown = (Spinner) findViewById(R.id.status);

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, priorityDDItems);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, statusDDItems);
        priorityDropDown.setAdapter(priorityAdapter);
        statusDropDown.setAdapter(statusAdapter);

        Calendar c=Calendar.getInstance();
        mYear=c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH);
        mDay=c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        dueDate = (EditText) findViewById(R.id.dueDate);
        todoTitle = (EditText) findViewById(R.id.todo_title);
        todoTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        todoDesc = (EditText) findViewById(R.id.todo_desc);
        dueDate.setText( sdf.format(c.getTime()));
        dueDate.setFocusable(false);
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    private void setupDB() {
        datasource = new TodoDataSource(this);
        datasource.open();
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            dueDate.setText(new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void onAddNewTodo(View view){
        //Creating object to store in database

        Todo newTodo = new Todo();
        newTodo.setDeleted(0);
        newTodo.setStatus(statusDropDown.getSelectedItem().toString());
        newTodo.setPriority(priorityDropDown.getSelectedItem().toString());
        newTodo.setCreateDate(System.currentTimeMillis() + "");
        newTodo.setDueDate(dueDate.getText().toString());
        newTodo.setTodoDesc(todoDesc.getText().toString());
        newTodo.setTodoTitle(todoTitle.getText().toString());

        datasource.createTodo(newTodo);

        Intent i = new Intent(TodoAddActivity.this, MainActivity.class);
        startActivity(i);
    }
}
