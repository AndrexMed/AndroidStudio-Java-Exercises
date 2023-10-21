package com.example.apprentcars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etUser, etPassword;
    TextView tvtitle;
    Button btregister, btlogin, btRegisterCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvtitle = findViewById(R.id.tvtitle);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);

        btregister = findViewById(R.id.btregister);
        btlogin = findViewById(R.id.btlogin);
        btRegisterCar = findViewById(R.id.btRegisterCar);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUser.getText().toString();
                String password = etPassword.getText().toString();

                if (!userName.isEmpty() || !password.isEmpty()){
                    Login(userName, password);
                } else {
                    tvtitle.setTextColor(Color.RED);
                    tvtitle.setText("All fields are required");
                }
            }
        });

        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoUserRegister();
            }
        });
        btRegisterCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoCarRegister();
            }
        });
    }

    private void GoCarRegister() {
        Intent carRegister = new Intent(getApplicationContext(), CarRegister.class);
        startActivity(carRegister);
    }

    private void GoUserRegister() {
        Intent userRegisterActivity = new Intent(getApplicationContext(), UserRegister.class);
        startActivity(userRegisterActivity);
    }

    private void Login(String userName, String password) {

        db.collection("Users").whereEqualTo("userName", userName).whereEqualTo("Password", password).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {

                            Intent rentCar = new Intent(getApplicationContext(), RentCar.class);
                            startActivity(rentCar);
                    } else {
                        tvtitle.setTextColor(Color.RED);
                        tvtitle.setText("User Invalid!");
                        clearData();
                    }
                } else {
                    tvtitle.setText("Error servidor");
                    clearData();
                }
            }
        });
    }

    private void clearData() {
        etUser.setText("");
        etPassword.setText("");
    }
}