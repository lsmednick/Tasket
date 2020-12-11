package edu.neu.madcourse.tasket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
 * Use the {@link tabMember#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tabMember extends Fragment {

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
    private ArrayList<String> memberList;
    private HashMap<String, String> myMap;
    private RecyclerView memberRecycler;

    public tabMember() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tabMember.
     */
    // TODO: Rename and change types and number of parameters
    public static tabMember newInstance(String param1, String param2) {
        tabMember fragment = new tabMember();
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

        this.myInflater = inflater.inflate(R.layout.fragment_tab_member, container, false);

        this.myMap = new HashMap<>();
        this.memberRecycler = myInflater.findViewById(R.id.fragment_recycler);
        memberRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        this.myAdapter = new SimpleStringAdapter(myMap, this.getActivity(), DashboardActivity.class);
        //TODO check destination activity validity with Clara
        memberRecycler.setAdapter(this.myAdapter);

        getMembers();

        FloatingActionButton fab = this.myInflater.findViewById(R.id.floatingActionButton_add_member);
        fab.setOnClickListener(v -> {
            //pop up dialog with text entry
            AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());

            alert.setTitle("Add Member");
            alert.setMessage("Enter the email of the member you'd like to add below");

            // Set an EditText view to get user input
            final EditText input = new EditText(this.getContext());
            alert.setView(input);

            alert.setPositiveButton("Ok", (dialog, whichButton) -> {
                String newMember = input.getText().toString();
                addMember(newMember);
            });

            alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
                // Canceled.
            });

            alert.show();
        });


        return myInflater;
    }

    public void addMember(String newMember) {

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
        //add user to team's associated_members
        DatabaseReference teamRef = this.database.getReference(type + "/" + this.key + "/associated_members");
        teamRef.child(key).setValue(true);

        //add team to user's teams
        DatabaseReference userRef = this.database.getReference("Users/" + key + "/" + type);
        userRef.child(this.key).setValue(true);
    }

    public void getMembers() {
        ArrayList<String> memberKeys = new ArrayList<>();
        DatabaseReference teamRef = this.database.getReference(this.type + "/" + this.key);
        teamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnap : snapshot.getChildren()) {
                    if (postSnap.getKey().equals("associated_members")) {
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

    private void makeMap(ArrayList<String> keys) {
        this.myMap = new HashMap<>();
        for (String key : keys) {
            DatabaseReference myref = this.database.getReference("Users/" + key);
            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue().toString();
                        if (name == null || name.equals("")) {
                            name = snapshot.child("email").getValue().toString();
                        }
                        myMap.put(name, key);
                    }
                    System.out.println("MEMBERS: " + myMap.toString());

                    memberRecycler.setAdapter(new SimpleStringAdapter(myMap, getActivity(), OtherProfileFragment.class));
                    System.out.println(myAdapter.getItemCount());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}