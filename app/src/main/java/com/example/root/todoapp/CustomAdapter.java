package com.example.root.todoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mitul on 6/22/2016.
 */
public class CustomAdapter extends ArrayAdapter<Todo> {

    Context context;
    List<Todo> todoItems;

    CustomAdapter(Context context, List<Todo> todoItems) {
        super(context, R.layout.todo_list_view, todoItems);
        this.context = context;
        this.todoItems = todoItems;
    }

    @Override
    public Todo getItem(int position) {
        return todoItems.get(position);
    }


    /* private view holder class */
    private class TodoViewHolder {
        TextView todo_title;
        TextView due_date;
        TextView status;
        TextView priority;
        ImageView editImg;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        TodoViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.todo_list_view, null);

            holder = new TodoViewHolder();
            holder.todo_title = (TextView) convertView.findViewById(R.id.todo_name);
            holder.due_date = (TextView) convertView.findViewById(R.id.due_date);
            holder.status = (TextView) convertView.findViewById(R.id.todo_status);
            holder.priority = (TextView) convertView.findViewById(R.id.priority_view);
            holder.editImg = (ImageView) convertView.findViewById(R.id.edit_image_view);



            convertView.setTag(holder);
            //return convertView;
        } else {
            holder = (TodoViewHolder) convertView.getTag();
        }

        Todo row_pos = todoItems.get(position);

        holder.todo_title.setText(row_pos.getTodoTitle());
        holder.due_date.setText(row_pos.getDueDate());
        holder.status.setText(row_pos.getStatus());
        holder.priority.setText(row_pos.getPriority());

        switch (row_pos.getPriority()) {
            case "Low":
                holder.priority.setTextColor(Color.HSVToColor(new float[]{ 100f, 70f, 15f }));
                System.out.println("Low color " + position);
                break;
            case "Medium":
                System.out.println("Medieum color" + position);
                holder.priority.setTextColor(Color.HSVToColor(new float[]{ 30f, 100f, 30f }));
                break;
            case "High":
                System.out.println("High color" + position);
                holder.priority.setTextColor(Color.RED);
                break;
        }

        if(row_pos.getStatus().equalsIgnoreCase("Done")) {
            holder.priority.setTextColor(Color.BLACK);
            convertView.setBackgroundColor(Color.GRAY);
        }

        holder.editImg.setClickable(true);
        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TodoEditActivity.class);
                Todo selectedTodo = todoItems.get(position);
                i.putExtra("todoId", selectedTodo.get_id() + "");
                context.startActivity(i);
            }
        });

        return convertView;
    }

}
