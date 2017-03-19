package com.elzobaba.fcisizitask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    public static Boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO : Recycler view

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            clearUserName(getApplicationContext());

            check = true;
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainStart.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearUserName(Context applicationContext) {
        SharedPreferences.Editor editor = SaveSharedPreference.getSharedPreferences(applicationContext).edit();
        editor.clear(); //clear all stored data
        editor.commit();
        Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
