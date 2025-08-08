package com.example.sen800_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;

public class AddActivity extends AppCompatActivity {

    EditText app_name, email, password;
    Button add_button;

    // Example RSA public key components
    private BigInteger e;
    private BigInteger n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);

        app_name = findViewById(R.id.app_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        add_button = findViewById(R.id.add_button);

        // RSA setup
        BigInteger p = new BigInteger("61"); // Replace with a generated prime
        BigInteger q = new BigInteger("53"); // Replace with a generated prime
        n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        e = new BigInteger("65537"); // Common public exponent
        BigInteger d = e.modInverse(phi); // Private key (if needed later)

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addCredentials(
                        app_name.getText().toString().trim(),
                        email.getText().toString().trim(),
                        password.getText().toString().trim(),
                        e,
                        n
                );
            }
        });
    }
}
