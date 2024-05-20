package com.example.shinobiplanner_yourninjato_dolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shinobiplanner_yourninjato_dolist.AddNewTask;
import com.example.shinobiplanner_yourninjato_dolist.MainAppPage;
import com.example.shinobiplanner_yourninjato_dolist.R;
import com.example.shinobiplanner_yourninjato_dolist.model.Taskid;
import com.example.shinobiplanner_yourninjato_dolist.model.TodoModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.google.firebase.ktx.Firebase;

import java.util.List;

public class ToDoAdapter  extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<TodoModel> todolist;
    private MainAppPage activity;
    private FirebaseFirestore firestore;

    public  ToDoAdapter(MainAppPage mainAppPage , List<TodoModel> todoList){
        this.todolist = todoList;
        activity = mainAppPage;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task , parent , false );
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        TodoModel todoModel = todolist.get(position);
        firestore.collection("task").document(todoModel.TaskId).delete();
        todolist.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }
    public  void editTask(int position){
        TodoModel todoModel = todolist.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task" , todoModel.getTask());
        bundle.putString("due" , todoModel.getDue());
        bundle.putString("id" , todoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager() , addNewTask.getTag());

    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TodoModel toDoModel = todolist.get(position);
        holder.mCheckBox.setText(toDoModel.getTask());
        holder.mDueDateTv.setText("Due On" + toDoModel.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("task").document(toDoModel.TaskId).update("status" , 1);
                }else{
                    firestore.collection("task").document(toDoModel.TaskId).update("status" , 0);
                }
            }
        });

    }
    private  boolean toBoolean (int status){
        return  status !=0;
    }

    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView mDueDateTv;
        CheckBox mCheckBox;
        public  MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDateTv = itemView.findViewById(R.id.duedate);
            mCheckBox = itemView.findViewById(R.id.checkbox);

        }
    }
}
