package com.example.appmobilesrentcars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //Conexion a Firestore de Firebase
    EditText etusername, etfullname, etpassword;
    ImageButton btnsave, btnsearch, btnlist, btnUpdate, btnDelete, btnClear;
    TextView tvmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etusername = findViewById(R.id.etusername);
        etfullname = findViewById(R.id.etfullname);
        etpassword = findViewById(R.id.etpassword);
        tvmessage = findViewById(R.id.tvmessage);

        btnsave = findViewById(R.id.btnsave);
        btnsearch = findViewById(R.id.btnsearch);
        btnlist = findViewById(R.id.btnlist);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnClear = findViewById(R.id.btnClear);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etusername.getText().toString();
                String fullName = etfullname.getText().toString();
                String password = etpassword.getText().toString();

                boolean isDataUserValid = IsDataUserValid(userName, fullName, password);

                if (isDataUserValid) {
                    RegistrarUsuario(userName, fullName, password);
                } else {
                    tvmessage.setTextColor(Color.RED);
                    tvmessage.setText("All fields are required");
                }
            }
        });
    }

    private void RegistrarUsuario(String userName, String fullName, String password) {
        UserAlreadyExist(userName).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean userExists) {
                if (userExists) {
                    tvmessage.setTextColor(Color.RED);
                    tvmessage.setText("The user already exists");
                } else {
                    // Tu código para agregar el nuevo usuario a Firestore
                    Map<String, Object> dataUser = new HashMap<>();
                    dataUser.put("userName", userName);
                    dataUser.put("FullName", fullName);
                    dataUser.put("Password", password);

                    db.collection("Users")
                            .add(dataUser)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    tvmessage.setTextColor(Color.GREEN);
                                    tvmessage.setText("DocumentSnapshot written with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    tvmessage.setTextColor(Color.RED);
                                    tvmessage.setText("Error adding document" + e);
                                }
                            });
                }
            }
        });
    }

    private boolean IsDataUserValid(String userName, String fullName, String password) {
        if (userName.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private Task<Boolean> UserAlreadyExist(String UserName) {
        return db.collection("Users")
                .document(UserName)
                .get()
                .continueWith(new Continuation<DocumentSnapshot, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            return document.exists();
                        }
                        return false;
                    }
                });
    }

    private void SearchUser(String username) {
        db.collection("Users")
                .whereEqualTo("userName", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tvmessage.setTextColor(Color.GREEN);
                                tvmessage.setText("User founded");
                                etfullname.setText(document.getString("FullName"));
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            tvmessage.setTextColor(Color.RED);
                            tvmessage.setText("Error inesperado!");
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
