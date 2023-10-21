package com.example.apprentcars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText etusername, etfullname, etpassword;
    ImageButton btnsave, btnsearch, btnlist, btnUpdate, btnDelete, btnClear;
    TextView tvmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

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

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etusername.getText().toString();
                String fullName = etfullname.getText().toString();
                String password = etpassword.getText().toString();

                boolean isDataUserValid = IsDataUserValid(userName, fullName, password);

                if (isDataUserValid) {
                    registrarUsuario(userName, fullName, password);
                } else {
                    tvmessage.setTextColor(Color.RED);
                    tvmessage.setText("All fields are required");
                }
            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etusername.getText().toString();
                SearchUser(userName);
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

    private void SearchUser(String username) {
        if  (!username.isEmpty()){
            db.collection("Users").whereEqualTo("userName", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        if (!task.getResult().isEmpty()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                etfullname.setText(document.getString("FullName"));
                                tvmessage.setText("User founded");
                            }
                        } else {
                            showErrorMessage("User no exist");
                        }
                    }
                }
            });
        } else {
            showErrorMessage("UserName field are required");
        }
    }

    private void registrarUsuario(String username, String fullname, String password) {
        db.collection("Users").whereEqualTo("userName", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Map<String, Object> dataUser = new HashMap<>();
                    dataUser.put("userName", username);
                    dataUser.put("FullName", fullname);
                    dataUser.put("Password", password);
                    db.collection("Users").add(dataUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            tvmessage.setText("User registered");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showErrorMessage("Error 500");
                        }
                    });
                } else {
                    showErrorMessage("User already exist!");
                }
                //} else {
                //  showErrorMessage("All fields are required");
                //}
            }
        });
    }

    private void clearData() {
        etusername.setText("");
        etfullname.setText("");
        etpassword.setText("");
    }

    private void showErrorMessage(String message){
        tvmessage.setTextColor(Color.RED);
        tvmessage.setText(message);
        clearData();
    }
}