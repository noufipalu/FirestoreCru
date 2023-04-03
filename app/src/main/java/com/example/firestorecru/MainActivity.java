package com.example.firestorecru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText name, adrs, phone, email, dob;
    Button save, show, update, delete;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.et_name);
        adrs = findViewById(R.id.et_address);
        phone = findViewById(R.id.et_phone);
        email = findViewById(R.id.et_email);
        dob = findViewById(R.id.et_dob);
        save = findViewById(R.id.btn_save);
        show = findViewById(R.id.btn_show);
        update = findViewById(R.id.btn_update);
        delete = findViewById(R.id.btn_delete);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                clearFields();
            }
        });
        
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowActivity.class));
            }
        });
        
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UpdateActivity.class));
            }
        });
        
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void clearFields() {
        name.getText().clear();
        adrs.getText().clear();
        phone.getText().clear();
        email.getText().clear();
        dob.getText().clear();
    }

    private void createUser() {
        String Name = name.getText().toString();
        String Adr = adrs.getText().toString();
        String Phone = phone.getText().toString();
        String Email = email.getText().toString();
        String Dob = dob.getText().toString();

        Map<String, Object> student = new HashMap<>();
        student.put("Name", Name);
        student.put("Address", Adr);
        student.put("DateOfBirth", Dob);
        student.put("Mobile", Phone);
        student.put("Email", Email);

        firestore.collection("student").add(student)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}