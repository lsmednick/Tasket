package edu.neu.madcourse.tasket;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tabTask#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tabTask extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "KEY";
    private static final String ARG_PARAM2 = "TYPE";
    private RecyclerView.Adapter mAdapter;

    private String mParam1;
    private String mParam2;
    private String key;
    private String type;
    private View myView;
    private String uid;
    private View fab;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<String> types = new ArrayList<>();
    private final ArrayList<String> images = new ArrayList<>();
    private final ArrayList<String> deadlines = new ArrayList<>();
    private final ArrayList<String> categories = new ArrayList<>();
    private final ArrayList<String> priorities = new ArrayList<>();

    public tabTask() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tabTask.
     */
    // TODO: Rename and change types and number of parameters
    public static tabTask newInstance(String param1, String param2) {
        tabTask fragment = new tabTask();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.key = getArguments().getString(ARG_PARAM1);
            this.type = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        this.myView = inflater.inflate(R.layout.activity_view_tasks, container, false);
        this.uid = Objects.requireNonNull(user).getUid();
        this.fab = myView.findViewById(R.id.view_tasks_fab);
        fab.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose Task Type\n(This cannot be changed later)").setItems(R.array.taskTypes,
                    (dialog, which) -> {
                        switch (which) {
                            case 0:
                                new Thread(() -> {
                                    Intent i = new Intent(getContext(), EditTask.class);
                                    i.putExtra("isNewTask", true);
                                    i.putExtra("taskID", "none");
                                    startActivity(i);
                                }).start();
                                break;
                            case 1:
                                new Thread(() -> {
                                    Intent i = new Intent(getContext(), HourlyTaskActivity.class);
                                    i.putExtra("isNewTask", true);
                                    i.putExtra("taskID", "none");
                                    startActivity(i);
                                }).start();
                                break;
                        }
                    });
            builder.show();
        });
        this.recyclerView = myView.findViewById(R.id.taskRecyclerView);
        recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        return myView;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.names.clear();
        this.types.clear();
        this.images.clear();
        this.deadlines.clear();
        this.categories.clear();
        this.priorities.clear();
    }

    @Override
    public void onResume() {
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
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child(this.type + "/" +
                this.key + "/tasks");
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
                    mAdapter = new TaskCardRecyclerAdapter(getContext(), names, deadlines, images,
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