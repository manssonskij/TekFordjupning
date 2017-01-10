package manssonskij.tekfordjupning;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import manssonskij.tekfordjupning.Adapters.AllTaskItemArrayAdapter;
import manssonskij.tekfordjupning.Objects.TaskItem;

public class AllTaskListActivity extends AppCompatActivity {

    private static final String TAG = "AllTaskListActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private Map<String, String> contactList = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllTaskListActivity.this, AddItemActivity.class));
            }
        });

        // init Firebase
        //firebaseAuth = FirebaseAuth.getInstance();
        //user = firebaseAuth.getCurrentUser();
        //makes a call to Firebase and fetches contact information
        getConctactsTasksFromFirebase();

    }

    private void getConctactsTasksFromFirebase() {

        // making a reference to the groups section in firebase
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference("groups").child("alfa");
        } catch (NullPointerException e) {
        }

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                // fetches the different contact info, ie user and their uid
                for (DataSnapshot contactSnapshot : snapshot.getChildren()) {
                    contactList.put(contactSnapshot.getKey(), contactSnapshot.getValue().toString());

                    getSharedTasksAndPopulateView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addValueEventListener(listener);
        //mDatabase.addListenerForSingleValueEvent(listener);
    }


    private void getSharedTasksAndPopulateView() {

        //  taskItem stuff
        final ArrayList<TaskItem> taskItemListAll = new ArrayList<TaskItem>();

        AllTaskItemArrayAdapter adapter = new AllTaskItemArrayAdapter(this, taskItemListAll, mDatabase);

        for (Map.Entry<String, String> entry : contactList.entrySet()) {

            try {
                mDatabase = FirebaseDatabase.getInstance().getReference("task").child(entry.getValue());
            } catch (NullPointerException e) {
            }

            try {
                // https://firebase.google.com/docs/database/android/read-and-write
                ValueEventListener taskListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get task object and use the values to update the UI
                        for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                            TaskItem task = taskSnapshot.getValue(TaskItem.class);
                            taskItemListAll.add(task);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadTaskItem:onCancelled", databaseError.toException());
                    }
                };
                mDatabase.addValueEventListener(taskListener);

            } catch (Exception e) {

            }
        }
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_list_scrolling, menu);
        return true;
    }

    public void signout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(AllTaskListActivity.this, MainActivity.class));
    }

    public void removeTaskItemFromListAndDatabase() {

    }

    public void displayAllTasks(MenuItem item) {

    }

    public void displayMyTasks(MenuItem item) {
        startActivity(new Intent(AllTaskListActivity.this, MyTaskListActivity.class));
    }

    public void aboutFragment(MenuItem item){
        AboutFragment newFragment = new AboutFragment();
        newFragment.show(getSupportFragmentManager(),"aboutFragment");

    }

}
