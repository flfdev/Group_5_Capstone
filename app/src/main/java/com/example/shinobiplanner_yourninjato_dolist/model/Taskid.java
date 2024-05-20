package com.example.shinobiplanner_yourninjato_dolist.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class Taskid {
    @Exclude
    public String TaskId;

    public <T extends Taskid> T withId(@NonNull final String id){
        this.TaskId = id;
        return  (T)  this;
    }
}
