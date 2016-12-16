package manssonskij.tekfordjupning;

import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AddItemActivity extends AppCompatActivity {

    private EditText taskItemTitle, taskItemDescription;
    private Button date_end_button, date_start_button, time_end_button, time_start_button;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "AddItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserInput()) return;
                createTask();
                startActivity(new Intent(AddItemActivity.this, TaskListActivity.class));
            }
        });

        // Should be in onCreate
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(getApplicationContext(), "Logged in as " + user.getUid(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(getApplicationContext(), "You are not logged in", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };
    }

    private boolean createTask() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        try {
            String title = taskItemTitle.getText().toString().trim();
            String taskDescription = taskItemDescription.getText().toString().trim();
            String taskId = "";

            date_start_button = (Button) findViewById(R.id.date_start_Button);
            time_start_button = (Button) findViewById(R.id.time_start_Button);
            date_end_button = (Button) findViewById(R.id.date_end_Button);
            time_end_button = (Button) findViewById(R.id.time_end_Button);

            String start_date = date_start_button.getText().toString().trim();
            String start_time = time_start_button.getText().toString().trim();
            String end_date = date_end_button.getText().toString().trim();
            String end_time = time_end_button.getText().toString().trim();

/*
            String start_date = findViewById(R.id.date_start_Button).get.toString().trim();
            String start_time = findViewById(R.id.time_start_Button).getText().toString().trim();
            String end_date = findViewById(R.id.date_end_Button)getText().toString().trim();;
            String end_time = findViewById(R.id.time_end_Button).toString().trim();;
*/
            // DateFormat format = new SimpleDateFormat("d/M/y");
            //format.parse(start_date);

            //String owner_uid, String title, Date start_date, Date end_date, String start_time, String end_time, String descriptionText
            TaskItem task = new TaskItem(user.getEmail(), title, taskDescription, start_date, end_date, start_time, end_time);
            //TaskItem task = new TaskItem(taskId, title, taskDescription);

            mDatabase.child("task").child(user.getUid()).child(title).setValue(task);
        } catch (Exception e) {
            Log.d(TAG, "Some kind of error");
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private boolean checkUserInput() {
        taskItemTitle = (EditText) findViewById(R.id.taskItemTitle);
        taskItemDescription = (EditText) findViewById(R.id.taskItemDescription);
        String title = taskItemTitle.getText().toString().trim();
        String taskDescription = taskItemDescription.getText().toString().trim();

        date_start_button = (Button) findViewById(R.id.date_start_Button);
        time_start_button = (Button) findViewById(R.id.time_start_Button);
        date_end_button = (Button) findViewById(R.id.date_end_Button);
        time_end_button = (Button) findViewById(R.id.time_end_Button);

        String start_date = date_start_button.getText().toString().trim();
        String start_time = time_start_button.getText().toString().trim();
        String end_date = date_end_button.getText().toString().trim();
        String end_time = time_end_button.getText().toString().trim();

        /**
         * Check that values exist, code from:
         * http://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/
         */
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getApplicationContext(), "You need to enter a title", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.isEmpty(taskDescription)) {
            Toast.makeText(getApplicationContext(), "You need to enter a password", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.isEmpty(start_date) || TextUtils.isEmpty(start_time) ||
                TextUtils.isEmpty(end_date) || TextUtils.isEmpty(end_time)) {
            Toast.makeText(getApplicationContext(), "You need to set dates and times", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void signout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
    }
}

