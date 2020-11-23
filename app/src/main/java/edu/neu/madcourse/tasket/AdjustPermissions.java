package edu.neu.madcourse.tasket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class AdjustPermissions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_permissions);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton_add_permission_category);
        fab.setOnClickListener(v -> {
            //TODO add card to recyclerview
        });

        //create temp permissions object
        ArrayList<Boolean> settings = new ArrayList<Boolean>();
        settings.add(true);
        settings.add(false);
        settings.add(true);
        settings.add(false);

        String[] members = {"userA", "userB", "userC"};

        Permission myPermission = new Permission(settings, "permission name", members);
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(myPermission);
        settings.set(1, true);
        members = Arrays.copyOf(members, members.length + 1);
        members[members.length - 1] = "userD";
        myPermission = new Permission(settings, "New Permission", members);
        permissions.add(myPermission);

        //recyclerview
        RecyclerView card_recycler = findViewById(R.id.permissions_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        card_recycler.setLayoutManager(layoutManager);
        card_recycler.setItemAnimator(new DefaultItemAnimator());

        PermissionsCustomAdapter myAdapter = new PermissionsCustomAdapter(this, permissions);
        card_recycler.setAdapter(myAdapter);
        //card_recycler.addItemDecoration(new DividerItemDecoration(card_recycler.getContext(), DividerItemDecoration.VERTICAL));

    }
}