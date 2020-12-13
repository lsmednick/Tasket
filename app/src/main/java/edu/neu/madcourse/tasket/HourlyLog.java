package edu.neu.madcourse.tasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HourlyLog extends AppCompatActivity {
    private ArrayList<String> datesToAdapter;
    private ArrayList<String> hoursToAdapter;
    private HashMap<String, Object> hours;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private String tID;
    boolean hasHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_log);
        hours = new HashMap<>();
        datesToAdapter = new ArrayList<>();
        hoursToAdapter = new ArrayList<>();
        tID = getIntent().getStringExtra("taskID");
        hasHours = getIntent().getBooleanExtra("hasHours", false);
        Button addButton = findViewById(R.id.addHoursButton);
        addButton.setOnClickListener(v -> addHours());
        Button saveButton = findViewById(R.id.saveHours);
        saveButton.setOnClickListener(v -> saveHours());
        recyclerView = findViewById(R.id.saveHoursButton);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        datesToAdapter.clear();
        hoursToAdapter.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        datesToAdapter.clear();
        hoursToAdapter.clear();
        if (!hasHours) {
            mAdapter = new HourlyLogAdapter(HourlyLog.this, datesToAdapter, hoursToAdapter);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            populateHoursList();
        }
    }

    // Helper method to add hours to our task
    private void addHours() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter an amount of hours to log.");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Date currentTime = Calendar.getInstance().getTime();
                hours.put(currentTime.toString(), input.getText().toString());
                datesToAdapter.add(currentTime.toString());
                hoursToAdapter.add(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Helper method to populate our hours list when a task already has hours logged
    private void populateHoursList() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tasks/" +
                tID + "/hours");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    System.out.println(">>>>>> KEY: " + snap.getKey());
                    System.out.println(">>>>>> VALUE: " + snap.getValue());
                    datesToAdapter.add(snap.getKey());
                    hoursToAdapter.add((String) snap.getValue());
                    hours.put(snap.getKey(), snap.getValue());
                    mAdapter = new HourlyLogAdapter(HourlyLog.this, datesToAdapter, hoursToAdapter);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Helper method to save hours to task
    private void saveHours() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tasks/" +
                tID + "/");
        ref.child("hours").updateChildren(hours);
        finish();
    }
}