package edu.neu.madcourse.tasket;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tabPermission#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tabPermission extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "KEY";
    private static final String ARG_PARAM2 = "TYPE";

    // TODO: Rename and change types of parameters
    private String key;
    private String type;

    private FirebaseDatabase database;
    private View myInflater;
    private SimpleStringAdapter myAdapter;
    private RecyclerView permissionRecycler;
    private HashMap<String, String> myMap;
    private ArrayList<String> memberList;
    private String permission;

    public tabPermission() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tabPermission.
     */
    // TODO: Rename and change types and number of parameters
    public static tabPermission newInstance(String param1, String param2) {
        tabPermission fragment = new tabPermission();
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
        this.database = FirebaseDatabase.getInstance();
        this.myInflater = inflater.inflate(R.layout.fragment_tab_permission, container, false);
        this.permissionRecycler = myInflater.findViewById(R.id.permissions_recycler);
        this.myMap = new HashMap<>();
        this.memberList = new ArrayList<>();
        permissionRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        this.myAdapter = new SimpleStringAdapter(memberList);
        permissionRecycler.setAdapter(this.myAdapter);

        setUpText();

        getMembers();

        FloatingActionButton fab = this.myInflater.findViewById(R.id.permissions_fab);
        fab.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());

            alert.setTitle("Add " + this.permission);
            alert.setMessage("Enter the email of the member you'd like to add below");

            final EditText input = new EditText(this.getContext());
            alert.setView(input);

            alert.setPositiveButton("Ok", (dialog, which) -> {
                String newMember = input.getText().toString();
                addMember(newMember);
            });

            alert.setNegativeButton("Cancel", (dialog, which) -> {
                //canceled
            });
            alert.show();
        });

        return myInflater;
    }

    private void addMember(String newMember) {
        // add user to adapter
        Query query = this.database.getReference("Users").orderByChild("email").equalTo(newMember);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot postSnap : snapshot.getChildren()) {
                        String name = postSnap.child("name").getValue().toString();
                        String key = postSnap.getKey();
                        addMemberDB(key);
                        if (name == null || name.equals("")) {
                            name = newMember;
                        }
                        System.out.println(name);
                        myMap.put(name, key);
                        memberList.add(name);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "could not add user", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addMemberDB(String key) {
        //add user to team's permissions
        DatabaseReference teamRef = this.database.getReference(type + "/" + this.key + "/permissions");
        teamRef.child(key).setValue(true);

        //add permission to user's privileges
        DatabaseReference userRef = this.database.getReference("Users/" + key + "/privileges");
        userRef.child(this.key).setValue(true);
    }


    private void getMembers() {
        ArrayList<String> memberKeys = new ArrayList<>();
        DatabaseReference myRef = this.database.getReference(this.type + "/" + this.key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnap : snapshot.getChildren()) {
                    if (postSnap.getKey().equals("permissions")) {
                        for (DataSnapshot childSnap : postSnap.getChildren()) {
                            memberKeys.add(childSnap.getKey());
                        }
                    }
                }
                makeMap(memberKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void makeMap(ArrayList<String> memberKeys) {
        this.myMap = new HashMap<>();
        for (String key : memberKeys) {
            DatabaseReference myref = this.database.getReference("Users/" + key);
            myref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue().toString();
                        if (name == null || name.equals("")) {
                            name = snapshot.child("email").getValue().toString();
                        }
                        myMap.put(name, key);
                    }
                    memberList.clear();
                    memberList.addAll(myMap.keySet());
                    permissionRecycler.setAdapter(new SimpleStringAdapter(memberList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setUpText() {
        if (this.type.equals("subteams")) {
            TextView mainText = this.myInflater.findViewById(R.id.owner_text_view);
            mainText.setText("Managers");
            TextView desc_text = this.myInflater.findViewById(R.id.owners_description);
            desc_text.setText("Managers are the only subteam-members that have the ability to " +
                    "add members and adjust permissions");
            this.permission = "manager";
        } else {
            this.permission = "owner";
        }
    }
}