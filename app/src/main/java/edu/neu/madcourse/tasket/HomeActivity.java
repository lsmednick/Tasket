package edu.neu.madcourse.tasket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    Button btnLogout;
    Button btnViewTeams;
    Button btnViewTasks;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnLogout = findViewById(R.id.logout_btn);
        Button viewTask = findViewById(R.id.viewTaskButton);


        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intToMain = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intToMain);
            Toast.makeText(HomeActivity.this, "You are logged out", Toast.LENGTH_SHORT).show();

        });

        btnViewTeams = findViewById(R.id.home_to_ViewTeams_button);
        btnViewTeams.setOnClickListener(v -> {
            Intent home_to_view_teams = new Intent(HomeActivity.this, ViewTeams.class);
            startActivity(home_to_view_teams);
        });

        viewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ViewTaskActivity.class);
                startActivity(i);
            }
        });

    }
}