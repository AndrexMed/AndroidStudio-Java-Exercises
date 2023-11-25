package com.example.apprentcarsfbfs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {

    EditText etUserName, etSecretWord, etPassword;
    Button btSaveUser;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        etUserName = findViewById(R.id.etUserName);
        etSecretWord = findViewById(R.id.etSecretWord);
        etPassword = findViewById(R.id.etPassword);

        btSaveUser = findViewById(R.id.btSaveUser);

        btSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });

    }

    private void saveUser() {

        String userName = etUserName.getText().toString();
        String secretWord = etSecretWord.getText().toString();
        String password = etPassword.getText().toString();

        if (!userName.isEmpty() && !secretWord.isEmpty() && !password.isEmpty()){
            saveUserInFS(userName, secretWord, password);
        } else {

        }
    }

    private void saveUserInFS(String userName, String secretWord, String password) {
        db.collection("Users").whereEqualTo("UserName", userName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        // Add a new document with a generated id.
                        Map<String, Object> dataUser = new HashMap<>();
                        dataUser.put("UserName", userName);
                        dataUser.put("SecretWord", secretWord);
                        dataUser.put("Password", password);
                        dataUser.put("Role", "User");

                        db.collection("Users").add(dataUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(UserRegister.this, "User created", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserRegister.this, "Error to create user", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(UserRegister.this, "User already exist", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UserRegister.this, "Internal Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}