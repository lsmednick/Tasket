package edu.neu.madcourse.tasket;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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
 * Use the {@link tabSubteam#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tabSubteam extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "KEY";
    private static final String ARG_PARAM2 = "TYPE";
    private static final String CURRENT_USER_KEY = FirebaseAuth.getInstance().getUid();

    // TODO: Rename and change types of parameters
    private String key;
    private String type;

    private RecyclerView myRecycler;
    private FirebaseDatabase database;
    private View myView;
    private SimpleStringAdapter myAdapter;

    private HashMap<String, String> map;

    public tabSubteam() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tabSubteam.
     */
    // TODO: Rename and change types and number of parameters
    public static tabSubteam newInstance(String param1, String param2) {
        tabSubteam fragment = new tabSubteam();
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
        this.database = FirebaseDatabase.getInstance();
        this.map = new HashMap<>();

        getSubteamData();


    }

    private void getSubteamData() {

        //get list of subteam keys associated with current team
        DatabaseReference myRef = this.database.getReference(this.type + "/" + this.key);  //"teams/key
        this.map = new HashMap<>();

        Query query = this.database.getReference("subteams").orderByChild("parentKey").equalTo(this.key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //map.clear();

                if (snapshot.exists()) {

                    for (DataSnapshot postSnap : snapshot.getChildren()) {

                        String name = postSnap.child("teamName").getValue().toString();
                        System.out.println(name);
                        map.put(name, postSnap.getKey());

                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.myView = inflater.inflate(R.layout.fragment_tab_subteam, container, false);


        RecyclerView myRecycler = myView.findViewById(R.id.subteam_recyclerview);
        myRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        this.myAdapter = new SimpleStringAdapter(this.map, this.getActivity(), ViewTeam.class);
        myRecycler.setAdapter(this.myAdapter);


        //TODO for recycler use the same logic as in viewteams-- its essentially the same thing but with keys from the team instead of the user


        FloatingActionButton fab = myView.findViewById(R.id.floatingActionButton_tab_subteam);
        fab.setOnClickListener(v -> {
            Activity myActivity = this.getActivity();
            AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());

            alert.setTitle("Create Subteam");
            alert.setMessage("Enter the subteam name");

            // Set an EditText view to get user input
            final EditText input = new EditText(this.getContext());
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //TODO handle DB creation/updates
                    String newTeam = input.getText().toString();

                    Intent intent = new Intent(myActivity, ViewTeam.class);
                    intent.putExtra("TEAM_KEY", createSubteam(newTeam));
                    intent.putExtra("TEAM_TYPE", "subteams");
                    startActivity(intent);

                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        });


        return myView;
    }

    private String createSubteam(String teamName) {

        // add to subteams
        DatabaseReference teamRef = this.database.getReference("subteams");
        DatabaseReference pushID = teamRef.push();
        pushID.child("teamName").setValue(teamName);
        pushID.child("parentKey").setValue(this.key);

        // add to current user's subteams
        DatabaseReference newRef = this.database.getReference("Users/" + CURRENT_USER_KEY + "/" + "subteams");
        newRef.child(pushID.getKey()).setValue(true);

        // add to team's subteams
        DatabaseReference ref = this.database.getReference("teams/" + this.key + "/subteams");
        ref.child(pushID.getKey()).setValue(true);

        return pushID.getKey();
    }
}