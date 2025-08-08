package com.example.sen800_project;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;

public class UpdateActivity extends AppCompatActivity {

    EditText app_name_input, email_input, password_input;
    Button update_button, delete_button;

    String _id, app_name, email;

    private BigInteger e, n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);

        app_name_input = findViewById(R.id.app_name2);
        email_input = findViewById(R.id.email2);
        password_input = findViewById(R.id.password2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        // RSA key setup
        BigInteger p = new BigInteger("61");
        BigInteger q = new BigInteger("53");
        n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        e = new BigInteger("65537");

        // Get intent data and show only the decrypted password
        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle("Update Credentials for " + app_name);

        update_button.setOnClickListener(v -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
            String newAppName = app_name_input.getText().toString().trim();
            String newEmail   = email_input.getText().toString().trim();
            String plainPassword = password_input.getText().toString().trim();

            myDB.updateData(_id, newAppName, newEmail, plainPassword, e, n);
        });

        delete_button.setOnClickListener(v -> confirmDialog());
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("_id") &&
                getIntent().hasExtra("app_name") &&
                getIntent().hasExtra("email") &&
                getIntent().hasExtra("password_decrypted")) {

            _id      = getIntent().getStringExtra("_id");
            app_name = getIntent().getStringExtra("app_name");
            email    = getIntent().getStringExtra("email");
            String decryptedPassword = getIntent().getStringExtra("password_decrypted");

            app_name_input.setText(app_name);
            email_input.setText(email);
            password_input.setText(decryptedPassword);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + app_name + " ?");
        builder.setMessage("Are you sure you want to delete " + app_name + " ?");
        builder.setPositiveButton("Yes", (DialogInterface dialogInterface, int i) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
            myDB.deleteOneRow(_id);
            finish();
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(android.R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(android.R.color.black));
    }
}
