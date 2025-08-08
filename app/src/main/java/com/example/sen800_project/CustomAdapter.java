package com.example.sen800_project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> _id, app_name, email, password;
    private ArrayList<String> decryptedPasswords, encryptedPasswords;

    private Activity activity;

    CustomAdapter(Activity activity,
                  Context context,
                  ArrayList<String> _id,
                  ArrayList<String> app_name,
                  ArrayList<String> email,
                  ArrayList<String> password,
                  ArrayList<String> decryptedPasswords,
                  ArrayList<String> encryptedPasswords) {

        this.activity = activity;
        this.context = context;
        this._id = _id;
        this.app_name = app_name;
        this.email = email;
        this.password = password; // display text (Enc: ... Dec: ...)
        this.decryptedPasswords = decryptedPasswords;
        this.encryptedPasswords = encryptedPasswords;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder._id_txt.setText(String.valueOf(position + 1));
        holder.app_name_txt.setText(app_name.get(position));
        holder.email_txt.setText(email.get(position));
        holder.password_txt.setText(password.get(position)); // Enc + Dec for list display

        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("_id", _id.get(position));
            intent.putExtra("app_name", app_name.get(position));
            intent.putExtra("email", email.get(position));
            intent.putExtra("password_decrypted", decryptedPasswords.get(position)); // ✅ only decrypted
            intent.putExtra("password_encrypted", encryptedPasswords.get(position)); // ✅ only encrypted
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return _id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView _id_txt, app_name_txt, email_txt, password_txt;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            _id_txt = itemView.findViewById(R.id._id_txt);
            app_name_txt = itemView.findViewById(R.id.app_name_txt);
            email_txt = itemView.findViewById(R.id.email_txt);
            password_txt = itemView.findViewById(R.id.password_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
