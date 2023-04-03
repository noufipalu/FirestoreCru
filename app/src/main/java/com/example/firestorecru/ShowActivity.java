package com.example.firestorecru;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.grpc.internal.MessageDeframer;

public class ShowActivity extends AppCompatActivity {


    ListView listView;
    List<String> values = new ArrayList<>();
    String TAG = "ShowActivity";
    Button goBack;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        goBack = findViewById(R.id.btn_back);
        listView = findViewById(R.id.list);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);

        firestore.collection("student").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    Log.d(TAG, snapshot.getId() + ":" + snapshot.getData());
                                    values.add("Name: " + snapshot.getString("Name") +
                                            "\nAddress:" + snapshot.getString("Address") +
                                            "\nDOB:" + snapshot.getString("DateOfBirth") +
                                            "\nMobile Number:" + snapshot.getString("Mobile") +
                                            "\n Email:" + snapshot.getString("Email"));

                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowActivity.this, "Failed to load the list", Toast.LENGTH_SHORT).show();
                    }
                });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowActivity.this, MainActivity.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] options = {"delete"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            deleteData(values.indexOf(0));
                        }
                    }
                });
            }
        });
    }

    public void deleteData(int index){
        firestore.collection("student").document(values.get(index)).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ShowActivity.this, "Data deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowActivity.this, "Failed to delete data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}