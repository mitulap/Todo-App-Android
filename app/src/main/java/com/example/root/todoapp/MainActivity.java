package com.example.root.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ListView lvItems;
    CustomAdapter itemsAdapter;
    private TodoDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        datasource = new TodoDataSource(this);
        datasource.open();

        setContentView(R.layout.activity_main);

        readItems();

        lvItems = (ListView) findViewById(R.id.lvItems);

        itemsAdapter = new CustomAdapter(this, datasource.getAllTodos());
        lvItems.setAdapter(itemsAdapter);

        // Setup remove listener method call
        setupListViewListener();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                Toast.makeText(this, "Add Todo Item selected.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, TodoAddActivity.class);
                startActivity(i);
                break;
            case R.id.action_info:
                Toast.makeText(this, "info selected.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return true;
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            public boolean onItemLongClick(AdapterView<?> adapter, View item, final int pos, long id){
                final int finalPos = pos;
                AlertDialog deleteBox =new AlertDialog.Builder(MainActivity.this)
                    //.setTitle("Delete")
                    .setMessage("Do you want to Delete this Todo?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //your deleting code`

                            //System.out.println(itemsAdapter.getItem(pos).get_id());
                            Todo todoTobeRemoved = itemsAdapter.getItem(finalPos);
                            itemsAdapter.remove(itemsAdapter.getItem(finalPos));
                            datasource.deleteTodo(todoTobeRemoved);

                            itemsAdapter.notifyDataSetChanged();
                            writeItems();
                            dialog.dismiss();
                        }

                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
                    return false;
            }

        });

        lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = lvItems.getAdapter().getItem(position);
                String value = obj.toString();

                Intent i = new Intent(MainActivity.this, TodoEditActivity.class);
                Todo selectedTodo = itemsAdapter.getItem(position);
                i.putExtra("todoId", selectedTodo.get_id() + "");
                startActivity(i);
            }
        });
    }

    //Method to read items from text file
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    //Method to write items to text file
    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
