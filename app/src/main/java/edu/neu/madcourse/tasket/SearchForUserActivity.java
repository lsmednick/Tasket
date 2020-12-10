package edu.neu.madcourse.tasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SearchForUserActivity extends AppCompatActivity {
    private TextView displayName;
    private ImageView displayImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_user);
        displayName = findViewById(R.id.displayName);
        displayImg = findViewById(R.id.userSearchImg);
        Button searchBut = findViewById(R.id.searchButton);
        EditText nameTxt = findViewById(R.id.personName);
        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForUser(nameTxt.getText().toString());
            }
        });
    }

    // Helper method to search for users
    private void searchForUser(String name) {


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
        Query query = userRef.child("Users").orderByChild("name").equalTo(name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot attribute : snapshot.getChildren()) {
                    if (attribute.getKey().equals("image") && (!attribute.getValue().equals(""))) {
                        Picasso.get().load(attribute.getValue().toString()).into(displayImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}