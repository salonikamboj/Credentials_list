package com.example.sen800_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.math.BigInteger;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Credentials.db";
    private static final int DATABASE_VERSION = 1 ;
    private static final String TABLE_NAME = "My_Credentials";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_APP_NAME = "app_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                          COLUMN_APP_NAME + " TEXT, " + COLUMN_EMAIL + " TEXT, "
                        + COLUMN_PASSWORD + " TEXT);"  ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addCredentials(String appName, String email, String password, BigInteger e, BigInteger n) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_APP_NAME, appName);
        cv.put(COLUMN_EMAIL, email);

        RSAHelper rsaHelper = new RSAHelper();
        rsaHelper.generateKeys(new BigInteger("61"), new BigInteger("53"));
        List<BigInteger> encryptedPassword = rsaHelper.encrypt(password, e, n);

        StringBuilder encryptedPasswordString = new StringBuilder();
        for (BigInteger block : encryptedPassword) {
            encryptedPasswordString.append(block.toString()).append(" ");
        }

        cv.put(COLUMN_PASSWORD, encryptedPasswordString.toString().trim());

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String app_name, String email, String password, BigInteger e, BigInteger n) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_APP_NAME, app_name);
        cv.put(COLUMN_EMAIL, email);

        // Encrypt the updated password
        RSAHelper rsaHelper = new RSAHelper();
        rsaHelper.generateKeys(new BigInteger("61"), new BigInteger("53")); // Example primes, replace with real ones
        List<BigInteger> encryptedPassword = rsaHelper.encrypt(password, e, n);

        // Convert the encrypted password list to a string
        StringBuilder encryptedPasswordString = new StringBuilder();
        for (BigInteger block : encryptedPassword) {
            encryptedPasswordString.append(block.toString()).append(" ");
        }

        cv.put(COLUMN_PASSWORD, encryptedPasswordString.toString().trim());

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
