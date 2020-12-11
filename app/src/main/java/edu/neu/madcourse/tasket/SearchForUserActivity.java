package edu.neu.madcourse.tasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SearchForUserActivity extends AppCompatActivity {
    private TextView displayName;
    private ImageView displayImg;
    private String targetUser;
    private String nameStr;
    private String imgStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_user);
        displayName = findViewById(R.id.displayName);
        displayImg = findViewById(R.id.userSearchImg);
        Button searchBut = findViewById(R.id.searchButton);
        Button saveBut = findViewById(R.id.saveBut);
        EditText nameTxt = findViewById(R.id.personName);
        searchBut.setOnClickListener(v -> {
            displayName.setText(R.string.search);
            searchForUser(nameTxt.getText().toString());
        });
        saveBut.setOnClickListener(v -> {
            if (targetUser != null) {
                Intent output = new Intent();
                output.putExtra("whichuser", targetUser);
                setResult(RESULT_OK, output);
            }
            finish();
        });

    }

    // Helper method to search for users
    private void searchForUser(String name) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean foundUser = false;
                for (DataSnapshot attribute : snapshot.getChildren()) {
                    HashMap<String, String> map = (HashMap<String, String>) attribute.getValue();
                    if (map.get("name").equals(name)) {
                        targetUser = attribute.getKey();
                        nameStr = map.get("name");
                        imgStr = map.get("image");
                        foundUser = true;
                        break;
                    }
                }
                if (foundUser) {
                    displayName.setText(nameStr);
                    if (!imgStr.equals("")) {
                        Picasso.get().load(imgStr).into(displayImg);
                    } else {
                        displayImg.setImageResource(R.drawable.tasket_logo);
                    }
                    System.out.println(targetUser);
                } else {
                    displayName.setText(R.string.nouserfound);
                    displayImg.setImageResource(R.drawable.tasket_logo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}