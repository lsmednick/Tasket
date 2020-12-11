package edu.neu.madcourse.tasket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OtherProfileFragment extends AppCompatActivity {


    private View view;
    private String key;

    ImageView avatarIv, coverIv;
    TextView nameTv, emailTv, phoneTv;
    FloatingActionButton fab;
    FirebaseDatabase database;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_profile);

        Intent intent = getIntent();
        this.key = intent.getStringExtra("USER_KEY");

        avatarIv = findViewById(R.id.avatarIv);
        coverIv = findViewById(R.id.coverIv);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        fab = findViewById(R.id.fab);

        fab.hide();

        database = FirebaseDatabase.getInstance();

        setData();
    }

    public void setData() {
        DatabaseReference myref = database.getReference("Users/" + this.key);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get data
                String name = "" + snapshot.child("name").getValue();
                String email = "" + snapshot.child("email").getValue();
                String phone = "" + snapshot.child("phone").getValue();
                String image = "" + snapshot.child("image").getValue();
                String cover = "" + snapshot.child("cover").getValue();
                Log.i("KEY >>>>>>>>>>>", snapshot.getKey() + " " + snapshot.getValue());

                Log.i("USER DATA>>>>>>>>> ", snapshot.getChildren().toString());

                //set data
                nameTv.setText(name);
                emailTv.setText(email);
                phoneTv.setText(phone);
                try {
                    //if image is received then set
                    Picasso.get().load(image).into(avatarIv);
                } catch (Exception e) {
                    //if there is any exception while getting image then set default
                    Picasso.get().load(R.drawable.ic_default_img_white).into(avatarIv);
                }

                try {
                    //if image is received then set
                    Picasso.get().load(cover).into(coverIv);
                } catch (Exception e) {
                    //if there is any exception while getting image then set default
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
