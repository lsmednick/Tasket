package edu.neu.madcourse.tasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ViewTasksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<String> types = new ArrayList<>();
    private final ArrayList<String> images = new ArrayList<>();
    private final ArrayList<String> deadlines = new ArrayList<>();
    private final ArrayList<String> categories = new ArrayList<>();
    private final ArrayList<String> priorities = new ArrayList<>();
    private String uid;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = Objects.requireNonNull(user).getUid();
        fab = findViewById(R.id.view_tasks_fab);
        fab.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Task Type\n(This cannot be changed later)").setItems(R.array.taskTypes,
                    (dialog, which) -> {
                        switch (which) {
                            case 0:
                                new Thread(() -> {
                                    Intent i = new Intent(ViewTasksActivity.this, EditTask.class);
                                    i.putExtra("isNewTask", true);
                                    i.putExtra("taskID", "none");
                                    startActivity(i);
                                }).start();
                                break;
                            case 1:
                                new Thread(() -> {
                                    Intent i = new Intent(ViewTasksActivity.this, HourlyTaskActivity.class);
                                    i.putExtra("isNewTask", true);
                                    i.putExtra("taskID", "none");
                                    startActivity(i);
                                }).start();
                                break;
                        }
                    });
            builder.show();
        });
        recyclerView = findViewById(R.id.taskRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        names.clear();
        types.clear();
        images.clear();
        deadlines.clear();
        categories.clear();
        priorities.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        names.clear();
        types.clear();
        images.clear();
        deadlines.clear();
        categories.clear();
        priorities.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        populateTaskList(database);
    }

    private void populateTaskList(FirebaseDatabase database) {
        ArrayList<String> taskIDs = new ArrayList<>();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users/" +
                uid + "/tasks");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Stores tasks that belong to our user
                for (DataSnapshot s : snapshot.getChildren()) {
                    taskIDs.add(s.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference().child("tasks");
        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String month = "", day = "", year = "";
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (taskIDs.contains(snap.getKey())) {
                        HashMap<String, String> map = (HashMap<String, String>) snap.getValue();
                        for (String str : map.keySet()) {
                            switch (str) {
                                case "category":
                                    categories.add(map.get(str));
                                    break;
                                case "deadlineDay":
                                    day = map.get(str);
                                    break;
                                case "deadlineMonth":
                                    String m = map.get(str);
                                    int addingOne = Integer.parseInt(m) + 1;
                                    month = String.valueOf(addingOne);
                                    break;
                                case "deadlineYear":
                                    year = map.get(str);
                                    break;
                                case "name":
                                    names.add(map.get(str));
                                    break;
                                case "picture":
                                    images.add(map.get(str));
                                    break;
                                case "priority":
                                    priorities.add(map.get(str));
                                    break;
                                case "type":
                                    types.add(map.get(str));
                                    break;
                            }
                        }
                        deadlines.add(month + "/" + day + "/" + year);
                    }
                    mAdapter = new TaskCardRecyclerAdapter(ViewTasksActivity.this, names, deadlines, images,
                            categories, priorities, types, taskIDs);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}