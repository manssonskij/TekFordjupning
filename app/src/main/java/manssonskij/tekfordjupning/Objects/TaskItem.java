package manssonskij.tekfordjupning.Objects;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by Anton on 2016-12-12.
 */
@IgnoreExtraProperties
public class TaskItem {

    public String task_id;
    public String owner_uid;
    public String email;
    public String title;
    public String descriptionText;
    public Location mLocation;
    public TaskDate taskDate;


    public TaskItem() {
        // Default constructor required for calls to DataSnapshot.getValue(TaskItem.class)
    }

    public TaskItem(String owner_uid, String title, String descriptionText) {
        this.owner_uid = owner_uid;
        this.title = title;
        this.descriptionText = descriptionText;
    }

    public TaskItem(String owner_uid,Location mLocation, String email, String title,String descriptionText, TaskDate taskDate) {
        this.owner_uid = owner_uid;
        this.email = email;
        this.title = title;
        this.taskDate = taskDate;
        //this.start_date = start_date;
        //this.end_date = end_date;
        //this.start_time = start_time;
        //this.end_time = end_time;
        this.descriptionText = descriptionText;
        this.mLocation = mLocation;
    }

    @Override
    public String toString() {
        return "owner_uid: '" + owner_uid +
                "\nTitle: " + title +
                "\nDescriptionTex: " + descriptionText;
    }

}
