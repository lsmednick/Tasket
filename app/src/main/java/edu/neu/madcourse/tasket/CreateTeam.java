package edu.neu.madcourse.tasket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateTeam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        Button saveButton = findViewById(R.id.save_team_button);
        saveButton.setOnClickListener(v -> {
            saveTeam();
        });

        Button setPermissions = findViewById(R.id.permissions_button);
        setPermissions.setOnClickListener(v -> {
            Intent create_to_permissions = new Intent(this, AdjustPermissions.class);
            startActivity(create_to_permissions);
            //TODO figure out when to save and how to retrieve permission info
            //idea- maybe create the team object here, pass through to permissions, then save later?
        });

        //TODO get recycler views working
    }

    private void saveTeam() {
        EditText nameEdit = findViewById(R.id.team_name_edit_text);
        String teamName = nameEdit.getText().toString();

        //TODO find a way to access/pass around the user's account info
    }
}