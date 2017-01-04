package manssonskij.tekfordjupning;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import manssonskij.tekfordjupning.Objects.TaskDate;
import manssonskij.tekfordjupning.Objects.TaskItem;
import manssonskij.tekfordjupning.Objects.TaskPosition;
import manssonskij.tekfordjupning.PickerFragments.DatePickerFragment;
import manssonskij.tekfordjupning.PickerFragments.EndDatePickerFragment;
import manssonskij.tekfordjupning.PickerFragments.EndTimePickerFragment;
import manssonskij.tekfordjupning.PickerFragments.TimePickerFragment;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.firebase.database.ValueEventListener;

public class AddItemActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    private EditText taskItemTitle_editText, taskItemDescription_editText;
    private Button date_end_button, date_start_button, time_end_button, time_start_button, location_button;
    private TextView mLatitudeText, mLongitudeText;

    private String start_date, start_time, end_date, end_time;
    private String title, taskDescription;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private static final String TAG = "AddItemActivity";

    private String task_edit_id;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        checkFirebaseAuthStatus();

        // Bind the date and time buttons
        date_start_button = (Button) findViewById(R.id.date_start_Button);
        time_start_button = (Button) findViewById(R.id.time_start_Button);
        date_end_button = (Button) findViewById(R.id.date_end_Button);
        time_end_button = (Button) findViewById(R.id.time_end_Button);

        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        taskItemTitle_editText = (EditText) findViewById(R.id.taskItemTitle);
        taskItemDescription_editText = (EditText) findViewById(R.id.taskItemDescription);


        // Check for extra parameter if this is an existing task that should be edited

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            task_edit_id = getIntent().getStringExtra("TASK_ID");
            //String type = getIntent().getType();
            checkEditParameter();
        }



        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputFields()) return;
                createTaskItem();
                startActivity(new Intent(AddItemActivity.this, MyTaskListActivity.class));
            }
        });

        findViewById(R.id.clearButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                taskItemTitle_editText.setText("");
                date_start_button.setText("Start date");
                time_start_button.setText("Start time");
                date_end_button.setText("End date");
                time_end_button.setText("End time");

                //mLatitudeText.setText(String.valueOf(item.taskPosition.latitude));
                //mLongitudeText.setText(String.valueOf(item.taskPosition.longitude));

                taskItemDescription_editText.setText("");
            }
        });
    }

    private void checkFirebaseAuthStatus() {
        // check if there is a valid user, otherwise redirect to sign in activity
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    TextView t = (TextView) findViewById(R.id.taskItemUserName);
                    t.setText(user.getEmail());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //Toast.makeText(getApplicationContext(), "Logged in as " + user.getUid(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(AddItemActivity.this, MainActivity.class));
                    //Toast.makeText(getApplicationContext(), "You are not logged in", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };
    }

    private void checkEditParameter() {
        if (!TextUtils.isEmpty(task_edit_id))
            try {

                //Toast.makeText(getApplicationContext(), "user identified as " + mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                DatabaseReference ref =
                        FirebaseDatabase.getInstance().getReference().child("task").child(mAuth.getCurrentUser().getUid());

                ref.child(task_edit_id.toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        TaskItem item = snapshot.getValue(TaskItem.class);
                        loadTaskItemValuesIntoView(item);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            } catch (Exception e) {
            }
    }

    private void loadTaskItemValuesIntoView(TaskItem item) {
        taskItemTitle_editText.setText(item.title);
        date_start_button.setText(item.taskDate.start_date);
        time_start_button.setText(item.taskDate.start_time);
        date_end_button.setText(item.taskDate.end_date);
        time_end_button.setText(item.taskDate.end_time);

        mLatitudeText.setText(String.valueOf(item.taskPosition.latitude));
        mLongitudeText.setText(String.valueOf(item.taskPosition.longitude));

        taskItemDescription_editText.setText(item.descriptionText);

    }

    private boolean validateInputFields() {

        title = taskItemTitle_editText.getText().toString().trim();
        taskDescription = taskItemDescription_editText.getText().toString().trim();

        start_date = date_start_button.getText().toString().trim();
        start_time = time_start_button.getText().toString().trim();
        end_date = date_end_button.getText().toString().trim();
        end_time = time_end_button.getText().toString().trim();

        /**
         * Check that values exist, code from:
         * http://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/
         */
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(getApplicationContext(), "You need to enter a title", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.isEmpty(taskDescription)) {
            Toast.makeText(getApplicationContext(), "You should enter a description", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (TextUtils.equals(start_date, "Start date") || TextUtils.equals(start_time, "Start time") ||
                TextUtils.equals(end_date, "End date") || TextUtils.equals(end_time, "End time")) {
            Toast.makeText(getApplicationContext(), "You need to set dates and times", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    private boolean createTaskItem() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // creation time
        DateFormat.getTimeInstance().format(new Date());


        try {
            TaskPosition taskPosition = new TaskPosition(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            TaskDate taskDate = new TaskDate(start_date, end_date, start_time, end_time);
            //String owner_uid, String title, Date start_date, Date end_date, String start_time, String end_time, String descriptionText
            TaskItem task = new TaskItem(user.getUid(), user.getEmail(), title, taskDescription, taskDate, taskPosition);
            //TaskItem task = new TaskItem(taskId, title, taskDesmLastLocationcription);

            mDatabase.child("task").child(user.getUid()).child(title).setValue(task);

        } catch (Exception e) {
            Log.d(TAG, "Some kind of error");
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
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

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

        /*
     *  The following PickerDialogs should be implemented in a better way
     */

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showEndDatePickerDialog(View v) {
        DialogFragment newFragment = new EndDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showEndTimePickerDialog(View v) {
        DialogFragment newFragment = new EndTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}

