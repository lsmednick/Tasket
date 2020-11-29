package edu.neu.madcourse.tasket;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.zip.Inflater;

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

        getMembers();

        FloatingActionButton fab = this.myInflater.findViewById(R.id.floatingActionButton_add_member);
        fab.setOnClickListener(v -> {
            //pop up dialog with text entry
            AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());

            alert.setTitle("Add Member");
            alert.setMessage("Enter the username of the member you'd like to add below");

            // Set an EditText view to get user input
            final EditText input = new EditText(this.getContext());
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String newMember = input.getText().toString();
                    addMember(newMember);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        });


        return myInflater;
    }

    public void addMember(String newMember) {
        this.memberList.clear();
        this.memberList.add(newMember);
        this.memberList.clear();

        //add under team
        DatabaseReference myref = this.database.getReference(this.type + "/" + this.key + "/associated_members");
        myref.child(newMember).setValue(true);

        //add team to user
        //TODO determine if we are creating usernames or using emails to add other users to a team/task
        //TODO determine if we should store the user variables using the email, username, or by unique ID within the teams and subteams

        //get unique key for new user
        DatabaseReference userRef = this.database.getReference("Users");
        userRef.addValueEventListener(new ValueEventListener() {
            final String myKey = key;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = false;
                for (DataSnapshot postSnap : snapshot.getChildren()) {
                    if (postSnap.getValue() == newMember) {
                        postSnap.getRef().child("teams").setValue(true);
                        flag = true;
                    }
                }
                if (!flag) {
                    //TODO don't allow adding the member to team
                    // maybe there's a way to make this a transaction?
                    // add team to member, add member to team
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //this.myAdapter.notifyItemInserted(this.memberList.size()-1);

    }

    public void getMembers() {
        ArrayList<String> memberList = new ArrayList<>();
        DatabaseReference myRef = this.database.getReference(this.type + "/" + this.key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnap : snapshot.getChildren()) {
                    if (postSnap.getKey().equals("associated_members")) {
                        for (DataSnapshot snap : postSnap.getChildren()) {
                            memberList.add(snap.getKey());
                        }

                    }
                }
                setRecycler(memberList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setRecycler(ArrayList<String> myMembers) {
        this.memberList = myMembers;
        RecyclerView memberRecycler = myInflater.findViewById(R.id.fragment_recycler);
        memberRecycler.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        this.myAdapter = new SimpleStringAdapter(myMembers);
        memberRecycler.setAdapter(this.myAdapter);
    }
}