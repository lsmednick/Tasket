package edu.neu.madcourse.tasket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //firebase auth
    FirebaseAuth firebaseAuth;

    private static final String ARG_PARAM1 = "";
    private static final String ARG_PARAM2 = "";
    String mParam1;
    String mParam2;
    private FirebaseStorage storage;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflater = inflater.inflate(R.layout.fragment_home, container, false);

        //init
        firebaseAuth = FirebaseAuth.getInstance();
        this.storage = FirebaseStorage.getInstance();

        Button to_teams = myInflater.findViewById(R.id.view_teams);
        to_teams.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ViewTeams.class);
            startActivity(intent);
        });

        Button to_tasks = myInflater.findViewById(R.id.view_tasks_button);
        to_tasks.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ViewTasksActivity.class);
            startActivity(intent);
        });

        TextView quoteView = myInflater.findViewById(R.id.quote_placement);

        quoteView.setText(getQuote());


        return myInflater;
    }


    private String getQuote() {
        Random r = new Random();
        int lower = 1;
        int upper = 1492;
        int randInt = r.nextInt(upper - lower) + lower;
        String myLine = null;
        String quote = "Always do your best. What you plant now, you will harvest later";
        String author = "Og Mandino";

        StorageReference storageReference = this.storage
                .getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/tasket-bf4" +
                        "b2.appspot.com/o/quotes?alt=media&token=bc568d44-9ae0-49f1-a084-8aa642ec9c4a");

        File localFile;
        try {
            localFile = File.createTempFile("quotes", "txt");
        } catch (IOException e) {
            localFile = null;
        }
        String[] splitLines;
        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
            }
        });
        splitLines = new String[2];


        try {
            Stream<String> lines = Files.lines(Paths.get(localFile.getAbsolutePath()));
            Log.i("FILE>>>>>>>>>", lines.toString());

            Log.i("FILE>>>>>>>>>>> ", String.valueOf(lines.skip(randInt).findFirst().isPresent()));

            splitLines = lines.skip(randInt).findFirst().get().split("\",\"");
            Log.i("FILE>>>>>>>>>>>>>", splitLines.toString());

        } catch (IOException e) {
            Log.i("FILE>>>>>>>>>>", "failed");
        }


        if (splitLines != null) {
            author = splitLines[0].replaceAll("\"", "");
            quote = splitLines[1].replaceAll("\"", "");
        }

        return "\"" + quote + "\"  -" + author;
    }


    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //user is signed in stay here
            //set email of logged in user
            //mProfileTv.setText(user.getEmail());
        } else {
            //user not signed in, go to main acitivity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }


    /*inflate options menu*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*handle menu item clicks*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }

}