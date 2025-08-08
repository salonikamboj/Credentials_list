package com.example.sen800_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_Button;
    MyDatabaseHelper myDB;

    ArrayList<String> _id, app_name, email, password; // this is the displayed password string
    ArrayList<String> decryptedPasswords; // only decrypted values
    ArrayList<String> encryptedPasswords; // only encrypted values

    CustomAdapter customAdapter;
    ImageView empty_imageview;
    TextView no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_Button = findViewById(R.id.add_button_icon);
        empty_imageview = findViewById(R.id.empty);
        no_data = findViewById(R.id.no_data);

        add_Button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        myDB = new MyDatabaseHelper(MainActivity.this);
        _id = new ArrayList<>();
        app_name = new ArrayList<>();
        email = new ArrayList<>();
        password = new ArrayList<>();
        decryptedPasswords = new ArrayList<>();
        encryptedPasswords = new ArrayList<>();

        displayData();

        customAdapter = new CustomAdapter(
                MainActivity.this,
                this,
                _id,
                app_name,
                email,
                password,
                decryptedPasswords,
                encryptedPasswords
        );

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void displayData() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            // RSA key setup
            BigInteger p = new BigInteger("61");
            BigInteger q = new BigInteger("53");
            BigInteger n = p.multiply(q);
            BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
            BigInteger e = new BigInteger("65537");
            BigInteger d = e.modInverse(phi);

            RSAHelper rsaHelper = new RSAHelper();

            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String appName = cursor.getString(1);
                String emailVal = cursor.getString(2);
                String encryptedStr = cursor.getString(3);

                String decryptedStr;
                try {
                    String[] parts = encryptedStr.split(" ");
                    List<BigInteger> encryptedBlocks = new ArrayList<>();
                    for (String part : parts) {
                        encryptedBlocks.add(new BigInteger(part));
                    }
                    decryptedStr = rsaHelper.decrypt(encryptedBlocks, d, n);
                } catch (Exception ex) {
                    decryptedStr = "Decryption Error";
                }

                _id.add(id);
                app_name.add(appName);
                email.add(emailVal);
                password.add("Enc: " + encryptedStr + "\nDec: " + decryptedStr);

                decryptedPasswords.add(decryptedStr); // only decrypted value
                encryptedPasswords.add(encryptedStr); // only encrypted value
            }

            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
            myDB.deleteAllData();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(android.R.color.black));
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(android.R.color.black));
    }
}
