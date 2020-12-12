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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Random;


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

    String[] quotes = {"Excellence is not a skill. It is an attitude. -Ralph Marston",
            "No yesterdays are ever wasted for those who give themselves to today. -Brendan Francis",
            "Only those who dare to fail greatly can ever achieve greatly. -Robert Kennedy",
            "The only limit to your impact is your imagination and commitment. -Tony Robbins",
            "Anything you really want, you can attain, if you really go after it. -Wayne Dyer",
            "A lot of times people look at the negative side of what they feel they can't do. I always look on the positive side of what I can do. -Chuck Norris",
            "We must overcome the notion that we must be regular. It robs you of the chance to be extraordinary and leads you to the mediocre. -Uta Hagen",
            "Most of the important things in the world have been accomplished by people who have kept on trying when there seemed to be no hope at all. -Dale Carnegie",
            "Ability is what you're capable of doing. Motivation determines what you do.Attitude determines how well you do it. -Lou Holtz",
            "Self-trust is the first secret of success. -Ralph Emerson",
            "Can you imagine what I would do if I could do all I can? -Sun Tzu",
            "Nothing diminishes anxiety faster than action. -Walter Anderson",
            "Do not turn back when you are just at the goal. -Publilius Syrus",
            "He can who thinks he can, and he can't who thinks he can't. This is an inexorable, indisputable law. -Pablo Picasso",
            "When it is obvious that the goals cannot be reached, don't adjust the goals, adjust the action steps. -Confucius"};


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
        int lower = 0;
        int upper = quotes.length - 1;
        int randInt = r.nextInt(upper - lower) + lower;
        Log.i("?>?????????????????? ", String.valueOf(randInt));
        String quote = quotes[randInt];
        return quote;
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