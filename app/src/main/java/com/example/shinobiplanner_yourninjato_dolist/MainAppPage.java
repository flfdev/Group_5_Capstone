package com.example.shinobiplanner_yourninjato_dolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.shinobiplanner_yourninjato_dolist.Adapter.ToDoAdapter;
import com.example.shinobiplanner_yourninjato_dolist.model.TodoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainAppPage extends AppCompatActivity implements OnDialogCloseListner {

    private RecyclerView recyclerView;
    private FloatingActionButton lutaw;
    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private List<TodoModel> mList;
    private Query query;
    private ListenerRegistration listenerRegistration;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recycleview);
        lutaw = findViewById(R.id.floatingActionButton);
        firestore = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainAppPage.this));

        lutaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager() , AddNewTask.TAG);

            }
        });

        mList = new ArrayList<>();
        adapter = new ToDoAdapter(MainAppPage.this , mList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper( new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        showData();
        recyclerView.setAdapter(adapter);
    }

    private void showData(){
       query = firestore.collection("task").orderBy("time" , Query.Direction.ASCENDING);

       listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        String id = documentChange.getDocument().getId();
                        TodoModel todoModel =  documentChange.getDocument().toObject(TodoModel.class).withId(id);

                        mList.add(todoModel);
                        adapter.notifyDataSetChanged();

                    }
                }
                listenerRegistration.remove();


            }
        });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
}
