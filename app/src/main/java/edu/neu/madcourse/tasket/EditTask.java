package edu.neu.madcourse.tasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class EditTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private int deadlineMonth;
    private int deadlineDay;
    private int deadlineYear;
    private String taskPriority;
    private String taskName;
    private String taskType;
    private String taskCategory;
    private String taskPicture;
    private String status;
    TextView yearView;
    TextView monthView;
    TextView dayView;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private boolean isNewTask;
    private String taskId;
    //storage
    StorageReference storageReference;
    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //arrays of permissions to be requested
    String[] cameraPermissions;
    String[] storagePermissions;
    //uri of picked image
    Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        yearView = findViewById(R.id.year);
        monthView = findViewById(R.id.month);
        dayView = findViewById(R.id.date);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        isNewTask = getIntent().getBooleanExtra("isNewTask", false);
        storageReference = getInstance().getReference(); //firebase storage reference
        DatabaseReference tasksRef = database.getReference("tasks");
        taskId = tasksRef.push().getKey();

        //init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Start with default values if this is a new task
        if (isNewTask) {
            taskName = "Task Name";
            taskType = "to-do";
            taskPriority = "low";
            taskCategory = "work";
            taskPicture = "";
            status = "in progress";
            deadlineYear = Calendar.getInstance().get(Calendar.YEAR);
            yearView.setText(String.valueOf(deadlineYear));
            deadlineMonth = Calendar.getInstance().get(Calendar.MONTH);
            monthView.setText(monthConverter(deadlineMonth + 1));
            deadlineDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            dayView.setText(String.valueOf(deadlineDay));
            ImageView img = findViewById(R.id.editTaskImage);
            img.setImageResource(R.drawable.tasket_logo);
        } else {
            // TODO add functionality
        }

        findViewById(R.id.edit_deadline_button).setOnClickListener(v -> showDatePicker());
        findViewById(R.id.editName).setOnClickListener(v -> nameAlert());
        findViewById(R.id.saveButton).setOnClickListener(v -> {
            if (isNewTask) {
                saveNewTaskToDatabase();
            } else {
                //TODO add functionality here
                System.out.println("Not a new task!");
            }
            finish();
        });
        findViewById(R.id.editPhoto).setOnClickListener(v -> showImagePicDialog());
        ToggleButton tog = (ToggleButton) findViewById(R.id.toggleButton);
        tog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    status = "complete";
                    System.out.println(status);
                } else {
                    status = "in progress";
                    System.out.println(status);
                }
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

    // Helper method to edit the tasks's photo
    private void editPhotoHelper() {

    }

    private void showImagePicDialog() {
        // show dialog containing options Camera and Gallery to pick the image
        String[] options = {"Camera", "Gallery"};
        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set title
        builder.setTitle("Pick Image From");
        // set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //Camera clicked
                    if (!checkCameraPermission()) {
                        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    //Gallery clicked
                    if (!checkStoragePermission()) {
                        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*This method called when user press Allow or Deny from permission request dialog
         * here we will handle permission cases (allowed & denied)*/

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                //picking from camera, first check if camera and storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        //permissions enabled
                        pickFromCamera();
                    } else {
                        //pemissions denied
                        Toast.makeText(this, "Please enable camera & storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {

                //picking from galler, first check if storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        //permissions enabled
                        pickFromGallery();
                    } else {
                        //pemissions denied
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }


            }
            break;
        }
    }

    private void uploadProfileCoverPhoto(final Uri uri) {
        /*Instead of creating separate function for Profile Picture and Cover Photo
         * i'm doing work for both in same function
         *
         * To add check ill add a string variable and assign it value "image" when user clicks
         * "Edit Profile Pic", and assign it value "cover" when user clicks "Edit Cover Photo"
         * Here: image is the key in each user containing url of user's profile picture
         *       cover is the key in each user containing url of user's cover photo */

        /*The parameter "image_uri" contains the uri of image picked either from camera or gallery
         * We will use UID of the currently signed in user as name of the image so there will be only one image for
         * profile and one image for cover for each user*/

        //path and name of image to be stored in firebase storage
        //e.g. Users_Profile_Cover_Imgs/image_e12f3456f789.jpg
        //e.g. Users_Profile_Cover_Imgs/cover_c123n4567g89.jpg
        String filePathAndName = "Task_Images/" + "" + "task_" + taskId;

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image is uploaded to storage, now get it's url and store in user's database
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();

                        //check if image is uploaded or not and url is received
                        if (uriTask.isSuccessful()) {
                            //image uploaded
                            taskPicture = downloadUri.toString();
                            Picasso.get().load(taskPicture).into((ImageView) findViewById(R.id.editTaskImage));
                        } else {
                            //error
                            Toast.makeText(EditTask.this, "Some error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //there were some error(s), get and show error message, dismiss progress dialog
                        Toast.makeText(EditTask.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*This method will be called after picking image from Camera or Gallery*/
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is picked from gallery, get uri of image
                image_uri = data.getData();

                uploadProfileCoverPhoto(image_uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri of image

                uploadProfileCoverPhoto(image_uri);

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean checkCameraPermission() {
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromCamera() {
        //Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        //put image uri
        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
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

    private void saveNewTaskToDatabase() {
        DatabaseReference tasksRef = database.getReference("tasks");
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = Objects.requireNonNull(user).getUid();
        DatabaseReference userRef = database.getReference("Users/" + uid + "/tasks");

        // Set up data to insert ito database
        HashMap<Object, String> taskData = new HashMap<>();
        taskData.put("name", taskName);
        taskData.put("type", taskType);
        taskData.put("picture", taskPicture);
        taskData.put("deadlineYear", String.valueOf(deadlineYear));
        taskData.put("deadlineMonth", String.valueOf(deadlineMonth));
        taskData.put("deadlineDay", String.valueOf(deadlineDay));
        taskData.put("priority", taskPriority);
        taskData.put("category", taskCategory);
        taskData.put("status", status);
        tasksRef.child(Objects.requireNonNull(taskId)).setValue(taskData);

        // Insert unique task ID into user section
        userRef.child(taskId).setValue(true);
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