package edu.neu.madcourse.tasket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ViewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        ImageView taskImage = findViewById(R.id.taskImage);
        taskImage.setImageResource(R.drawable.common_google_signin_btn_icon_dark_focused);
        //taskImage.
    }
}