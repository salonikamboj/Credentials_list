package com.example.sen800_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList _id, app_name, email, password;


    CustomAdapter(Context context, ArrayList _id, ArrayList app_name, ArrayList email, ArrayList password){
        this.context = context;
        this._id = _id;
        this.app_name = app_name;
        this.email = email;
        this.password = password;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder._id_txt.setText(String.valueOf(_id.get(position)));
        holder.app_name_txt.setText(String.valueOf(app_name.get(position)));
        holder.email_txt.setText(String.valueOf(email.get(position)));
        holder.password_txt.setText(String.valueOf(password.get(position)));


    }

    @Override
    public int getItemCount() {
        return _id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView _id_txt, app_name_txt, email_txt, password_txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            _id_txt= itemView.findViewById(R.id._id_txt);
            app_name_txt= itemView.findViewById(R.id.app_name_txt);
            email_txt = itemView.findViewById(R.id.email_txt);
            password_txt = itemView.findViewById(R.id.password_txt);
        }
    }

}
