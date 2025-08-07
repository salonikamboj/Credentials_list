package com.example.sen800_project;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class AddActivity extends AppCompatActivity {

    EditText app_name, email, password;
    Button add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);


        app_name= findViewById(R.id.app_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        add_button= findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addCredentials(app_name.getText().toString().trim(),
                        email.getText().toString().trim(),
                        password.getText().toString().trim());
            }
        });
    }
}