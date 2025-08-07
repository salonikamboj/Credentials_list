package com.example.sen800_project;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_Button;
    MyDatabaseHelper myDB;
    ArrayList<String> _id, app_name, email, password;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         recyclerView = findViewById(R.id.recyclerView);
         add_Button = findViewById(R.id.add_button);

         add_Button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this, AddActivity.class);
                 startActivity(intent);

             }
         });

        myDB = new MyDatabaseHelper(MainActivity.this);
        _id = new ArrayList<>();
        app_name = new ArrayList<>();
        email = new ArrayList<>();
        password = new ArrayList<>();

        displayData();
        customAdapter = new CustomAdapter(MainActivity.this,_id,app_name,email,password );
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    void displayData(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() ==0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                _id.add(cursor.getString(0));
                app_name.add(cursor.getString(1));
                email.add(cursor.getString(2));
                password.add(cursor.getString(3));
            }
        }
    }
}
