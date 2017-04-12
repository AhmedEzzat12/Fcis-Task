package com.elzobaba.fcisizitask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static Boolean check = false;
    RecyclerView recyclerView;
    TasksAdapter adapter;
    List<TaskObject> taskObjects = new ArrayList<>();
    FirebaseAuth ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO : Recycler view

        recyclerView = (RecyclerView)findViewById(R.id.tasks_recyclerview);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("tasks");


        taskObjects.add(new TaskObject("Temp","Archi","Task 1","12-4-2017","Temp"));
        taskObjects.add(new TaskObject("Temp","OS","Task 2","13-3-2017","Temp"));
        taskObjects.add(new TaskObject("Temp","DB","Task 3","30-4-2016","Temp"));
        taskObjects.add(new TaskObject("Temp","SA","Task 4","20-3-2017","Temp"));
        taskObjects.add(new TaskObject("Temp","AI","Task 2","13-5-2017","Temp"));
        taskObjects.add(new TaskObject("Temp","Graphics","Task 3","31-4-2017","Temp"));
        taskObjects.add(new TaskObject("Temp","OS","Task 4","20-3-2017","Temp"));

        sortList();

        adapter = new TasksAdapter(this,taskObjects);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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


    private void sortList()
    {
        for (int i =0;i<taskObjects.size();i++)
        {
            for(int j=i;j<taskObjects.size();j++)
            {
                String[] s2 = taskObjects.get(j).getDeadline().split("-");
                String[] s1 = taskObjects.get(i).getDeadline().split("-");

                if (Integer.parseInt(s1[2]) > Integer.parseInt(s2[2]))
                {
                    swap(i,j);
                    continue;
                }
                if (Integer.parseInt(s1[2]) < Integer.parseInt(s2[2]))
                    continue;


                if (Integer.parseInt(s1[1]) > Integer.parseInt(s2[1]))
                {
                    swap(i,j);
                    continue;
                }
                if (Integer.parseInt(s1[1]) < Integer.parseInt(s2[1]))
                    continue;


                if (Integer.parseInt(s1[0]) > Integer.parseInt(s2[0]))
                {
                    swap(i,j);
                    continue;
                }
                if (Integer.parseInt(s1[0]) < Integer.parseInt(s2[0]))
                    continue;

            }
        }
    }

    private void swap(int i,int j)
    {
        TaskObject object = taskObjects.get(i);
        taskObjects.set(i,taskObjects.get(j));
        taskObjects.set(j,object);
    }
}
