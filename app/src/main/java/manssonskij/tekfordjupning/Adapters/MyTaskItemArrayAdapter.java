package manssonskij.tekfordjupning.Adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import manssonskij.tekfordjupning.AddItemActivity;
import manssonskij.tekfordjupning.Objects.TaskItem;
import manssonskij.tekfordjupning.R;


/**
 * Created by Anton on 2016-12-15.
 * <p>
 * inspired by:https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class MyTaskItemArrayAdapter extends ArrayAdapter<TaskItem> {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();

    private DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference().child("task").child(user.getUid());

    private static class ViewHolder {
        TextView task_title;
        TextView task_uid;
        TextView task_description;
        TextView start_date, start_time, end_date, end_time;
        Button edit_button, delete_button;
        TextView location;
    }

    public MyTaskItemArrayAdapter(Context context, ArrayList<TaskItem> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskItem taskItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_task_item_layout, parent, false);

            viewHolder.task_title = (TextView) convertView.findViewById(R.id.task_title);
            viewHolder.task_uid = (TextView) convertView.findViewById(R.id.task_uid);
            viewHolder.task_description = (TextView) convertView.findViewById(R.id.task_description);

            viewHolder.start_date = (TextView) convertView.findViewById(R.id.start_date);
            viewHolder.start_time = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.end_date = (TextView) convertView.findViewById(R.id.end_date);
            viewHolder.end_time = (TextView) convertView.findViewById(R.id.end_time);

            viewHolder.edit_button = (Button) convertView.findViewById(R.id.edit_button);
            viewHolder.delete_button = (Button) convertView.findViewById(R.id.delete_button);

            viewHolder.location = (TextView) convertView.findViewById(R.id.task_location);

            convertView.setTag(viewHolder);

        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Lookup view for data population

        // Populate the data into the template view using the data object
        viewHolder.task_title.setText(taskItem.title);
        viewHolder.task_uid.setText("Owner: " + taskItem.owner_uid);
        viewHolder.task_description.setText(taskItem.descriptionText);

        viewHolder.start_date.setText(" @ " + taskItem.taskDate.start_date);
        viewHolder.start_time.setText(taskItem.taskDate.start_time);
        viewHolder.end_date.setText(" @ " + taskItem.taskDate.end_date);
        viewHolder.end_time.setText(taskItem.taskDate.end_time);

        viewHolder.location.setText(taskItem.taskPosition.toString());

        //convertView.setTag(taskItem);
        // Attach the click event handler
        viewHolder.edit_button.setTag(position);
        viewHolder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Access user from within the tag
                int position = (Integer) view.getTag();
                TaskItem taskItem = getItem(position);
                // Do what you want here...
                Intent intent = new Intent(getContext().getApplicationContext(), AddItemActivity.class);

                intent.putExtra("TASK_ID",taskItem.task_id);
                view.getContext().startActivity(intent);

                //Toast.makeText(getContext(), "taSuccess? "+ taskItem.title, Toast.LENGTH_LONG).show();
            }
        });

        viewHolder.delete_button.setTag(position);
        viewHolder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Access user from within the tag
                int position = (Integer) view.getTag();
                TaskItem taskItem = getItem(position);

                try {
                    Toast.makeText(getContext(), "Removing: "+ taskItem.title, Toast.LENGTH_LONG).show();
                    ref.child(taskItem.task_id).removeValue();
                }catch (Exception e){
                    Toast.makeText(getContext(), "Could not remove: "+ taskItem.title, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

}