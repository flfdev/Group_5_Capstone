package com.example.shinobiplanner_yourninjato_dolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddNewTask  extends BottomSheetDialogFragment {


    public static final String TAG = "AddNewTask";

    private TextView setduedate;
    private EditText mtaskEdit;
    private Button mSaveBtn;
    private FirebaseFirestore firestore;
    private Context context;
    private String dueDate = "";
    private String id = "";
    private String dueDateUpdate = "";
    public static  AddNewTask newInstance(){
        return new AddNewTask();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setduedate = view.findViewById(R.id.set_due_tv);
        mtaskEdit = view.findViewById(R.id.task_edittext);
        mSaveBtn = view.findViewById(R.id.savebtn);

        firestore = FirebaseFirestore.getInstance();

         boolean  isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            id = bundle.getString("id");
            dueDateUpdate = bundle.getString("due");

            mtaskEdit.setText(task);
            setduedate.setText(dueDateUpdate);

            if (task.length() > 0 ){
                mSaveBtn.setEnabled(false);
                mSaveBtn.setBackgroundColor(Color.GRAY);
            }
        }

        mtaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mSaveBtn.setEnabled(false);
                    mSaveBtn.setBackgroundColor(Color.GRAY);
                } else {
                     mSaveBtn.setEnabled(true);
                     mSaveBtn.setBackgroundColor(getResources().getColor(R.color.orange));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        setduedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int  MONTH = calendar.get(Calendar.MONTH);
                int  year = calendar.get(Calendar.YEAR);
                int Day = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       month = month + 1;
                       setduedate.setText(dayOfMonth + "/" + month + "/" + year);
                       dueDate = dayOfMonth + "/" + month + "/" + year;
                    }
                } , year, MONTH, Day );

                datePickerDialog.show();
            }
        });
        boolean finalIsUpdate = isUpdate;
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String task = mtaskEdit.getText().toString();


                                            if (finalIsUpdate){
                                                firestore.collection("task").document(id).update("task" , task , "due" , dueDate);
                                                Toast.makeText(context, "task Updated", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                            if (task.isEmpty()) {
                                                Toast.makeText(context, "Empty task not Allowed", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Map<String, Object> Taskmap = new HashMap<>();

                                                Taskmap.put("task", task);
                                                Taskmap.put("due", dueDate);
                                                Taskmap.put("status", 0);
                                                Taskmap.put("time", FieldValue.serverTimestamp());

                                                firestore.collection("task").add(Taskmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                            }                               }
            dismiss();
            }

        });

        }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}

