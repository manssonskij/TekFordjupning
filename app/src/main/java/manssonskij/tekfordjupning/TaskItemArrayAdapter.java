package manssonskij.tekfordjupning;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anton on 2016-12-15.
 *
 * inspired by:https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */

public class TaskItemArrayAdapter extends ArrayAdapter<TaskItem> {

private static class ViewHolder{
    TextView task_title;
    TextView task_uid;
    TextView task_description;
}

    public TaskItemArrayAdapter(Context context, ArrayList<TaskItem> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskItem taskItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item_layout, parent, false);

            viewHolder.task_title = (TextView) convertView.findViewById(R.id.task_title);
            viewHolder.task_uid = (TextView) convertView.findViewById(R.id.task_uid);
            viewHolder.task_description = (TextView) convertView.findViewById(R.id.task_description);

            convertView.setTag(viewHolder);

        }else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Lookup view for data population

        // Populate the data into the template view using the data object
        viewHolder.task_title.setText(taskItem.title);
        viewHolder.task_uid.setText(taskItem.owner_uid);
        viewHolder.task_description.setText(taskItem.descriptionText);
    // Return the completed view to render on screen
        return convertView;
    }




}