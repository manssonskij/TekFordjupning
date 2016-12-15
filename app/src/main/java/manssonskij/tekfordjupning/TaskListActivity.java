package manssonskij.tekfordjupning;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TaskListActivity extends AppCompatActivity {

    private static final String TAG = "TaskListActivity";
    LinearLayout linearLayout;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayout =(LinearLayout) findViewById(R.id.item_list_linear_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskListActivity.this, AddItemActivity.class));
            }
        });




        //  taskItem stuff
        final ArrayList<TaskItem> taskItemList = new ArrayList<TaskItem>();

        //ArrayAdapter<TaskItem> itemsAdapter = new ArrayAdapter<TaskItem>(this, R.layout.task_item, taskItemList);
        TaskItemArrayAdapter adapter = new TaskItemArrayAdapter(this, taskItemList);

        // init Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference("task").child(user.getUid());
        // writeDatabase();

        // https://firebase.google.com/docs/database/android/read-and-write

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    TaskItem task = postSnapshot.getValue(TaskItem.class);
                  //  addIssueView(task);
                    taskItemList.add(task);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);

       // ListView listView = (ListView) findViewById(R.id.listView);
       // listView.setAdapter(itemsAdapter);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

}

    private void addIssueView(TaskItem task) {


        TextView tv = new TextView(this);
        tv.setPadding(15,15,5,0);
        tv.setText("Title: " + task.title + "\nDescription: " + task.descriptionText + "\nOwned by: "+ task.owner_uid);

        Button button  = new Button(this);
        button.setText("Edit");

        linearLayout.addView(tv);
        linearLayout.addView(button);



        /*
        TextView t = new TextView(this);
        t = (TextView) findViewById(R.id.dynamic_task_view);
        t.setText("Description: "+ task.descriptionText);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void signout(MenuItem item){
        FirebaseAuth.getInstance().signOut();
    }



    public void writeDatabase() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
