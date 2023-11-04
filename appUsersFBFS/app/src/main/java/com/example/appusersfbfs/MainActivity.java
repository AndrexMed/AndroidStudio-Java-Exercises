package com.example.appusersfbfs;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    //Conexion a Firestore de Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner spRole;
    String[] spRoles = {"Seleccione un rol", "Admin", "Usuario"};

    EditText etusername, etfullname, etemail, etpassword;
    ImageButton btnsave, btnsearch, btnlist, btnUpdate, btnDelete, btnClear;
    TextView tvmessage;

    String idAutomatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etusername = findViewById(R.id.etusername);
        etfullname = findViewById(R.id.etfullname);
        etemail = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        spRole = findViewById(R.id.spRole);
        tvmessage = findViewById(R.id.tvmessage);
        btnsave = findViewById(R.id.btnsave);
        btnsearch = findViewById(R.id.btnsearch);
        btnlist = findViewById(R.id.btnlist);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnClear = findViewById(R.id.btnClear);

        spRole.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, spRoles));

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etusername.getText().toString();
                String fullname = etfullname.getText().toString();
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();
                String role = spRole.getSelectedItem().toString();

                boolean isDataUserValid = dataUserValidation(username, fullname, email, password);
                boolean isEmailUserValid = emailUserValidation(email);

                if (isDataUserValid) {
                    if (!role.equals("Seleccione un rol")) {
                        SaveUser(username, fullname, email, password, role);
                    }
                    tvmessage.setTextColor(Color.RED);
                    tvmessage.setText("Seleccione un rol valido!");
                } else {
                    tvmessage.setTextColor(Color.RED);
                    tvmessage.setText("Debe ingresar todos los datos solicitados");
                }

                /*if (!username.isEmpty() && !fullname.isEmpty()
                        && !email.isEmpty() && !password.isEmpty() && !role.isEmpty()) {

                    Pattern patEmail = Patterns.EMAIL_ADDRESS;

                    if (patEmail.matcher(email).matches()) {

                        if (!role.equals("Seleccione un rol")) {
                            SaveUser(username, fullname, email, password, role);
                        } else {
                            tvmessage.setTextColor(Color.RED);
                            tvmessage.setText("Seleccione un rol valido!");
                        }
                    } else {
                        etemail.setError("Email invalido");
                    }

                    //saveUser(username, fullname, email, password, role);
                } else {
                    tvmessage.setTextColor(Color.RED);
                    tvmessage.setText("Debe ingresar todos los datos solicitados");
                }*/
            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etusername.getText().toString();

                if (!username.isEmpty()) {
                    SearchUser(username);
                } else {

                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etusername.getText().toString();
                String fullname = etfullname.getText().toString();
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();
                String role = spRole.getSelectedItem().toString();

                boolean isDataUserValidation = dataUserValidation(username, fullname, email, password);

                if (isDataUserValidation) {
                    if (!role.equals("Seleccione un rol")) {
                        UpdateUser(username, fullname, email, password, role);
                    }
                    tvmessage.setTextColor(Color.RED);
                    tvmessage.setText("Selecionne un rol valido");
                } else {
                    tvmessage.setTextColor(Color.RED);
                    tvmessage.setText("Campos obligatorios");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document(idAutomatic)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                tvmessage.setTextColor(Color.GREEN);
                                tvmessage.setText("Usuario eliminado correctamente");
                            }
                        });
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etusername.setText("");
                etfullname.setText("");
                etemail.setText("");
                etpassword.setText("");
                tvmessage.setText("");
                spRole.setSelection(0);
            }
        });
    }

    private void UpdateUser(String username, String fullname, String email, String password, String role) {
        Map<String, Object> dataUser = new HashMap<>();
        dataUser.put("UserName", username);
        dataUser.put("FullName", fullname);
        dataUser.put("Email", email);
        dataUser.put("Password", password);
        dataUser.put("Role", role);

        db.collection("Users").document(idAutomatic)
                .set(dataUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        tvmessage.setTextColor(Color.GREEN);
                        tvmessage.setText("Usuario actualizado...");
                    }
                });
    }

    private void DeleteUser(String username) {
        db.collection("Users").document(username)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tvmessage.setText("User deleted");
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvmessage.setText("Error deleting user");
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    private void SearchUser(String username) {
        db.collection("Users")
                .whereEqualTo("UserName", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    idAutomatic = document.getId();
                                    tvmessage.setTextColor(Color.GREEN);
                                    tvmessage.setText("User founded");
                                    etfullname.setText(document.getString("FullName"));
                                    etemail.setText(document.getString("Email"));
                                    spRole.setSelection(document.get("Role").equals("Admin") ? 1 : 2);
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                tvmessage.setTextColor(Color.RED);
                                tvmessage.setText("Usuario no existe");
                            }

                        } else {
                            tvmessage.setTextColor(Color.RED);
                            tvmessage.setText("Error inesperado!");
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void SaveUser(String username, String fullname, String email, String password, String role) {
        db.collection("Users")
                .whereEqualTo("UserName", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                // Add a new document with a generated id.
                                Map<String, Object> dataUser = new HashMap<>();
                                dataUser.put("UserName", username);
                                dataUser.put("FullName", fullname);
                                dataUser.put("Email", email);
                                dataUser.put("Password", password);
                                dataUser.put("Role", role);

                                db.collection("Users")
                                        .add(dataUser)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                tvmessage.setTextColor(Color.GREEN);
                                                tvmessage.setText("DocumentSnapshot written with ID: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Log.w(TAG, "Error adding document", e);
                                                tvmessage.setTextColor(Color.RED);
                                                tvmessage.setText("Error adding document" + e);
                                            }
                                        });
                            } else {
                                tvmessage.setTextColor(Color.RED);
                                tvmessage.setText("User already exist");
                            }

                        } else {
                            tvmessage.setTextColor(Color.RED);
                            tvmessage.setText("Error inesperado!");
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }//Fin SaveUser

    private boolean dataUserValidation(String username, String fullname, String email, String password) {
        if (!username.isEmpty() || !fullname.isEmpty() || !email.isEmpty() || !password.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean emailUserValidation(String email) {
        Pattern patEmail = Patterns.EMAIL_ADDRESS;

        if (patEmail.matcher(email).matches()) {
            return true;
        } else {
            return false;
        }
    }

}