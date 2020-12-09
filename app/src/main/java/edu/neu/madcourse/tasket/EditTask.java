package edu.neu.madcourse.tasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    private String uid;
    private HashMap<String, Object> collabs;
    TextView yearView;
    TextView monthView;
    TextView dayView;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private boolean isNewTask;
    private String taskId;
    // Recycler view essentials
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<String> emailList = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> imgList = new ArrayList<>();
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
        ImageView img = findViewById(R.id.editTaskImage);
        FirebaseUser user = mAuth.getCurrentUser();
        uid = Objects.requireNonNull(user).getUid();
        collabs = new HashMap<>();

        //init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Start with default values if this is a new task. Else populate task with databse values
        if (isNewTask) {
            taskName = "Task Name";
            taskType = "to-do";
            taskPriority = "low";
            taskCategory = "work";
            taskPicture = "https://firebasestorage.googleapis.com/v0/b/tasket-bf4b2.appspot.com/o/Task_Images%2Ftasket_logo.png?alt=media&token=2501c751-14cf-4491-8fe8-02c945221b83";
            status = "in progress";
            collabs.put(uid, true);
            deadlineYear = Calendar.getInstance().get(Calendar.YEAR);
            yearView.setText(String.valueOf(deadlineYear));
            deadlineMonth = Calendar.getInstance().get(Calendar.MONTH);
            monthView.setText(monthConverter(deadlineMonth + 1));
            deadlineDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            dayView.setText(String.valueOf(deadlineDay));
            img.setImageResource(R.drawable.tasket_logo);
        } else {
            String tID = getIntent().getStringExtra("taskID");
            DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference("tasks/" +
                    tID);
            taskRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        System.out.println(snap.getKey() + " : " + snap.getValue());
                        switch (snap.getKey()) {
                            case "category":
                                taskCategory = (String) snap.getValue();
                                switch (taskCategory) {
                                    case "work":
                                        Chip workChip = findViewById(R.id.editWorkChip);
                                        workChip.setChecked(true);
                                        break;
                                    case "home":
                                        Chip homeChip = findViewById(R.id.editHomeChip);
                                        homeChip.setChecked(true);
                                        break;
                                    case "school":
                                        Chip schoolChip = findViewById(R.id.editSchoolChip);
                                        schoolChip.setChecked(true);
                                        break;
                                    case "personal":
                                        Chip personalChip = findViewById(R.id.editPersonalChip);
                                        personalChip.setChecked(true);
                                        break;
                                    default:
                                        Chip otherChip = findViewById(R.id.editOtherChip);
                                        otherChip.setChecked(true);
                                        break;
                                }
                                break;
                            case "deadlineDay":
                                deadlineDay = Integer.parseInt((String) snap.getValue());
                                dayView.setText(String.valueOf(deadlineDay));
                                break;
                            case "deadlineMonth":
                                deadlineMonth = Integer.parseInt((String) snap.getValue());
                                monthView.setText(monthConverter(deadlineMonth + 1));
                                break;
                            case "deadlineYear":
                                deadlineYear = Integer.parseInt((String) snap.getValue());
                                yearView.setText(String.valueOf(deadlineYear));
                                break;
                            case "name":
                                taskName = (String) snap.getValue();
                                TextView nameView = findViewById(R.id.editTaskName);
                                nameView.setText(taskName);
                                break;
                            case "picture":
                                taskPicture = (String) snap.getValue();
                                break;
                            case "priority":
                                taskPriority = (String) snap.getValue();
                                if (taskPriority.equals("low")) {
                                    Chip lowChip = findViewById(R.id.editLow);
                                    lowChip.setChecked(true);
                                } else if (taskPriority.equals("medium")) {
                                    Chip medChip = findViewById(R.id.editMed);
                                    medChip.setChecked(true);
                                } else {
                                    Chip highChip = findViewById(R.id.editHigh);
                                    highChip.setChecked(true);
                                }
                                break;
                            case "type":
                                taskType = (String) snap.getValue();
                                break;
                            case "status":
                                status = (String) snap.getValue();
                                ToggleButton tog = findViewById(R.id.toggleButton);
                                if (status.equals("complete")) {
                                    tog.setChecked(true);
                                }
                        }
                    }
                    Picasso.get().load(taskPicture).into(img);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        findViewById(R.id.edit_deadline_button).setOnClickListener(v -> showDatePicker());
        findViewById(R.id.editName).setOnClickListener(v -> nameAlert());
        findViewById(R.id.saveButton).setOnClickListener(v -> {
            saveNewTaskToDatabase();
            finish();
        });
        findViewById(R.id.editPhoto).setOnClickListener(v -> showImagePicDialog());
        ToggleButton tog = (ToggleButton) findViewById(R.id.toggleButton);
        tog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    status = "complete";
                } else {
                    status = "in progress";
                }
            }
        });
        setPriorityListener();
        setCategoryListener();
        recyclerView = findViewById(R.id.editCollabRecyc);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nameList.clear();
        imgList.clear();
        emailList.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nameList.clear();
        imgList.clear();
        emailList.clear();
        setUpCollabRecycler();
    }

    // Helper method to set up collaborator recycler view
    private void setUpCollabRecycler() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (collabs.containsKey(snap.getKey())) {
                        HashMap<String, String> map = (HashMap<String, String>) snap.getValue();
                        for (String str : map.keySet()) {
                            switch (str) {
                                case "email":
                                    emailList.add(map.get(str));
                                    break;
                                case "image":
                                    imgList.add(map.get(str));
                                    break;
                                case "name":
                                    nameList.add(map.get(str));
                                    break;
                            }
                        }
                    }
                    System.out.println(emailList);
                    System.out.println(imgList);
                    System.out.println(nameList);
                    mAdapter = new CollabCardRecyclerAdapter(EditTask.this, nameList, imgList,
                            emailList);
                    recyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    // Helper method to prompt an alert dialog for a user to enter their task name.
    // Task names are limited to 10 characters.
    private void nameAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Task Name");
        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
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
        DatabaseReference userRef = database.getReference("Users/" + uid + "/tasks");

        // Set up data to insert ito database
        HashMap<String, Object> taskData = new HashMap<>();
        taskData.put("name", taskName);
        taskData.put("type", taskType);
        taskData.put("picture", taskPicture);
        taskData.put("deadlineYear", String.valueOf(deadlineYear));
        taskData.put("deadlineMonth", String.valueOf(deadlineMonth));
        taskData.put("deadlineDay", String.valueOf(deadlineDay));
        taskData.put("priority", taskPriority);
        taskData.put("category", taskCategory);
        taskData.put("status", status);

        HashMap<String, Object> collabData = new HashMap<>();
        collabData.put(uid, true);

        taskData.put("collaborators", collabData);

        if (isNewTask) {
            tasksRef.child(Objects.requireNonNull(taskId)).setValue(taskData);
            // Insert unique task ID into user section
            userRef.child(taskId).setValue(true);
        } else {
            tasksRef.child(getIntent().getStringExtra("taskID")).updateChildren(taskData);
        }
    }

    private void updateTask() {

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