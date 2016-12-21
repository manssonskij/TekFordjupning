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

import manssonskij.tekfordjupning.Objects.TaskItem;


public class TaskListActivity extends AppCompatActivity {

    private static final String TAG = "TaskListActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

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
                startActivity(new Intent(TaskListActivity.this, AddItemActivity.class));
            }
        });

        // init Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("task").child(user.getUid());

        //  taskItem stuff
        final ArrayList<TaskItem> taskItemList = new ArrayList<TaskItem>();
        //ArrayAdapter<TaskItem> itemsAdapter = new ArrayAdapter<TaskItem>(this, R.layout.task_item, taskItemList);
        TaskItemArrayAdapter adapter = new TaskItemArrayAdapter(this, taskItemList);


        try {
            // https://firebase.google.com/docs/database/android/read-and-write
            ValueEventListener taskListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get task object and use the values to update the UI
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        TaskItem task = taskSnapshot.getValue(TaskItem.class);
                        taskItemList.add(task);
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

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


        /*
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                int position = (Integer) view.getTag();
             //   TaskItem item = (TaskItem) view.getTag();
                //
                /*
                Intent intent = new Intent(TaskListActivity.this, AddItemActivity.class);
                TaskItem taskId = null;
                intent.putExtra("EXTRA_SESSION_ID", taskId);
                startActivity(intent);

               Toast.makeText(TaskListActivity.this, "You clicked an item: " +position, Toast.LENGTH_LONG).show();
            }
        });
        */
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

    public void removeTaskItemFromListAndDatabase(){


    }
}
