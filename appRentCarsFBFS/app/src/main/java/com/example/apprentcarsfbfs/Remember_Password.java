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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Remember_Password extends AppCompatActivity {

EditText etUser, etKeyword, etNewPass;
Button btnChangePass;

FirebaseFirestore db = FirebaseFirestore.getInstance();
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_remember_password);

    etUser = findViewById(R.id.etUser);
    etKeyword = findViewById(R.id.etKeyword);
    etNewPass = findViewById(R.id.etNewPass);

    btnChangePass = findViewById(R.id.btnChangePass);

    btnChangePass.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String userName = etUser.getText().toString();
            String keyWord = etKeyword.getText().toString();
            String newPass = etNewPass.getText().toString();

            if(!userName.isEmpty() && !keyWord.isEmpty() && !newPass.isEmpty()){
                ChangePassword(userName, keyWord, newPass);
            } else {
                Toast.makeText(Remember_Password.this, "Campos obligatorios!", Toast.LENGTH_SHORT).show();
            }
        }
    });
}//Fin oncreate

private void ChangePassword(String userName, String keyWord, String newPassword) {
    db.collection("Users").whereEqualTo("UserName", userName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()){
                handleUserQueryResult(task.getResult(), keyWord, newPassword);
            }else{
                Toast.makeText(Remember_Password.this, "Internal Error", Toast.LENGTH_SHORT).show();
            }
        }
    });
}

    private void handleUserQueryResult(QuerySnapshot querySnapshot, String keyWord, String newPassword) {
        if (querySnapshot.size() > 0) {
            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
            String storedPalabraSecreta = document.getString("SecretWord");

            if (storedPalabraSecreta.equals(keyWord)) {
                updateUserPassword(document.getId(), newPassword);
            } else {
                Toast.makeText(Remember_Password.this, "Secret word is invalid!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Remember_Password.this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserPassword(String userId, String newPassword) {
        db.collection("Users").document(userId)
                .update("Password", newPassword)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Contraseña actualizada!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Remember_Password.this, "Password changed!", Toast.LENGTH_SHORT).show();
                });
    }

}