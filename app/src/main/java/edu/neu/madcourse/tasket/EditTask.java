package edu.neu.madcourse.tasket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class EditTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private int deadlineMonth;
    private int deadlineDay;
    private int deadlineYear;
    private String taskPriority;
    private String taskName;
    private String taskType;
    private String taskCategory;
    TextView yearView;
    TextView monthView;
    TextView dayView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        yearView = findViewById(R.id.year);
        monthView = findViewById(R.id.month);
        dayView = findViewById(R.id.date);
        mAuth = FirebaseAuth.getInstance();


        // Start with default values
        taskName = "Task Name";
        taskType = "to-do";
        taskPriority = "low";
        taskCategory = "work";
        deadlineYear = Calendar.getInstance().get(Calendar.YEAR);
        yearView.setText(String.valueOf(deadlineYear));
        deadlineMonth = Calendar.getInstance().get(Calendar.MONTH);
        monthView.setText(monthConverter(deadlineMonth + 1));
        deadlineDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        dayView.setText(String.valueOf(deadlineDay));

        ImageView img = findViewById(R.id.editTaskImage);
        img.setImageResource(R.drawable.tasket_logo);
        findViewById(R.id.edit_deadline_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        findViewById(R.id.editName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameAlert();
            }
        });

        findViewById(R.id.editTaskType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeAlert();
            }
        });

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setPriorityListener();
        setCategoryListener();
    }

    // Helper method to prompt an alert dialog for a user to enter their task name.
    // Task names are limited to 10 characters.
    private void nameAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Task Name");
        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                taskName = input.getText().toString();
                TextView name = findViewById(R.id.editTaskName);
                name.setText(taskName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Helper method to prompt alert dialog to let users choose between different task types
    private void typeAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Task Type").setItems(R.array.taskTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView thisTask = findViewById(R.id.taskType);
                switch (which) {
                    case 0:
                        taskType = "to-do";
                        thisTask.setText(R.string.todotask);
                        break;
                    case 1:
                        taskType = "hourly";
                        thisTask.setText(R.string.hourlyTask);
                        break;
                }

            }
        });
        builder.show();
    }

    // Helper method to get data from our chip group
    private void setPriorityListener() {
        Chip chip = findViewById(R.id.editLow);
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskPriority = "low";
            }
        });
        Chip chipMed = findViewById(R.id.editMed);
        chipMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskPriority = "medium";
            }
        });
        Chip chipHigh = findViewById(R.id.editHigh);
        chipHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskPriority = "high";
            }
        });
    }

    // Helper method to get data from our *other* chip group
    private void setCategoryListener() {
        Chip chipWork = findViewById(R.id.editWorkChip);
        chipWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCategory = "work";
                System.out.println("Category selected: " + taskCategory);
            }
        });
        Chip chipHome = findViewById(R.id.editHomeChip);
        chipHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCategory = "home";
                System.out.println("Category selected: " + taskCategory);
            }
        });
        Chip chipSchool = findViewById(R.id.editSchoolChip);
        chipSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCategory = "school";
                System.out.println("Category selected: " + taskCategory);
            }
        });
        Chip chipPersonal = findViewById(R.id.editPersonalChip);
        chipPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCategory = "personal";
                System.out.println("Category selected: " + taskCategory);
            }
        });
        Chip chipOther = findViewById(R.id.editOtherChip);
        chipOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCategory = "other";
                System.out.println("Category selected: " + taskCategory);
            }
        });
    }

    // Method to pull up date picker window
    private void showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        deadlineYear = year;
        yearView.setText(String.valueOf(year));
        deadlineMonth = month;
        monthView.setText(monthConverter(month + 1));
        deadlineDay = dayOfMonth;
        dayView.setText(String.valueOf(dayOfMonth));
    }

    private void saveTaskToDatabase() {
        //TODO uuuuuuhhhhhhh
    }

    // Helper function to convert month int to string
    private String monthConverter(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "Error";
    }
}