package com.example.appusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {

    TextView tvList;
    ListView lvUsers;
    Button btnBack;

    ArrayList<String> arrUsers = new ArrayList<String>();

    clsbd dbase = new clsbd(this, "dbusers",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        btnBack = findViewById(R.id.btnBack);
        lvUsers = findViewById(R.id.lvUsers);
        tvList = findViewById(R.id.tvList);

        tvList.setText(getIntent().getStringExtra("mtitle"));

        LoadUsers();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GoBack();
                finish();
            }
        });

    }// Fin OnCreate

    private void LoadUsers() {
        arrUsers = myArrUsers();
        lvUsers.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arrUsers));
    }

    private ArrayList<String> myArrUsers() {
        ArrayList<String> dataUsers = new ArrayList<String>();

        SQLiteDatabase db = dbase.getReadableDatabase();

        String query = "SELECT username,fullname, email, role FROM user ORDER BY fullname";

        Cursor cUsers = db.rawQuery(query,null);

        if (cUsers.moveToFirst()){
            do {
                String mrec = "Nombre completo: "+cUsers.getString(1) + "\n"
                            + "Email: "+cUsers.getString(2) + "\n"
                            + "Usuario: "+cUsers.getString(0);

                dataUsers.add(mrec);

            }while (cUsers.moveToNext());
        }
        db.close();
        return dataUsers;
    }

    private void GoBack() {
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
    }
}