package com.example.firestorecru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    EditText firstname, name;
    TextView goback;
    Button update;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        name = findViewById(R.id.nametbc);
        firstname = findViewById(R.id.firstname);
        update = findViewById(R.id.update_btn);
        goback = findViewById(R.id.goback);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearData();

                String FirstName = firstname.getText().toString();
                String NewName = name.getText().toString();

                firstname.setText("");
                name.setText("");

                updateData(FirstName, NewName);
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateActivity.this, MainActivity.class));
            }
        });
    }

    private void clearData() {
        name.getText().clear();
    }

    private void updateData(String FirstName, String NewName) {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("Name", NewName);

        firestore.collection("student").whereEqualTo("Name", FirstName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot snapshot = task.getResult().getDocuments().get(0);
                            String docId = snapshot.getId();
                            firestore.collection("student").document(docId).update(userDetails)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(UpdateActivity.this, "Data updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else {
                            Toast.makeText(UpdateActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}