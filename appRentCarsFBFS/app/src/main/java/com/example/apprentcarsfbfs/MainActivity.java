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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    EditText etUser, etPassword;
    Button btLogin, btRegister, btForgotPass;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);

        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        btForgotPass = findViewById(R.id.btForgotPass);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUser.getText().toString();
                String password = etPassword.getText().toString();

                if(!userName.isEmpty() && !password.isEmpty()){
                    Login(userName, password);
                } else{
                    Toast.makeText(MainActivity.this, "Campos obligarios!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoRegister();
            }
        });

        btForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoForgotPassword();
            }
        });
    }//FIN ONCREATE

    private void GoForgotPassword() {
        Intent forgot = new Intent(getApplicationContext(), Remember_Password.class);
        startActivity(forgot);
    }

    private void GoRegister() {
        Intent register = new Intent(getApplicationContext(), UserRegister.class);
        startActivity(register);
    }

    private void Login(String userName, String password) {
        db.collection("Users").whereEqualTo("UserName", userName).whereEqualTo("Password", password).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String role = document.getString("Role");
                            String user = document.getString("UserName");

                            Intent rentCar = new Intent(getApplicationContext(), HomeMenu.class);
                            rentCar.putExtra("userName", user);
                            rentCar.putExtra("role", role);
                            startActivity(rentCar);

                             break;
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error de servidor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//Fin metodo Login

}