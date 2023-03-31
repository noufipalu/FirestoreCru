package com.example.firestorecru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.grpc.internal.MessageDeframer;

public class ShowActivity extends AppCompatActivity {


    TextView name, email, adrs, dob, phone;
    Button goBack;
    FirebaseFirestore firestore;
    String userId;
    FirebaseUser logUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        name = findViewById(R.id.txt_name);
        adrs = findViewById(R.id.txt_address);
        email = findViewById(R.id.txt_email);
        dob = findViewById(R.id.txt_dob);
        phone = findViewById(R.id.txt_phone);
        goBack = findViewById(R.id.btn_back);

        firestore = FirebaseFirestore.getInstance();

        DocumentReference reference = firestore.collection("student").document();
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()){
                                String Name = name.getText().toString();
                                String Address = adrs.getText().toString();
                                String Dob = dob.getText().toString();
                                String Phone  = phone.getText().toString();
                                String Email = email.getText().toString();

                                name.setText(Name);
                                adrs.setText(Address);
                                dob.setText(Dob);
                                phone.setText(Phone);
                                email.setText(Email);

                            }else {
                                Toast.makeText(ShowActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ShowActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowActivity.this, "Data can't be retrieved", Toast.LENGTH_SHORT).show();
                    }
                });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowActivity.this, MainActivity.class));
            }
        });
    }
}