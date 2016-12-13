package manssonskij.tekfordjupning;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year;
    private int month;
    private int day;

    private EditText taskItemTitle, taskItemDescription;
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
        /*
        String userdisplayname = user.getUid();
        Log.d("user: ", userdisplayname);
        */

        try {
            String title =  taskItemTitle.getText().toString().trim();
            String taskDescription = taskItemDescription.getText().toString().trim();
            String taskId = "";

            TaskItem task = new TaskItem(title, taskDescription);

            mDatabase.child("task").child(user.getUid()).setValue(task);
        }
        catch (Exception e){
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
        taskItemTitle= (EditText) findViewById(R.id.taskItemTitle);
        taskItemDescription = (EditText)findViewById(R.id.taskItemDescription);
        String title =  taskItemTitle.getText().toString().trim();
        String taskDescription = taskItemDescription.getText().toString().trim();

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

}

