package manssonskij.tekfordjupning;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by Anton on 2016-12-12.
 */
@IgnoreExtraProperties
public class TaskItem {

    public String owner_uid;
    public String title;
    public String start_date;
    public String end_date;
    public String start_time;
    public String end_time;
    public String descriptionText;


    public TaskItem() {
        // Default constructor required for calls to DataSnapshot.getValue(TaskItem.class)
    }

    public TaskItem(String owner_uid, String title, String descriptionText) {
        this.owner_uid = owner_uid;
        this.title = title;
        this.descriptionText = descriptionText;
    }

    public TaskItem(String owner_uid, String title,String descriptionText, String start_date, String end_date, String start_time, String end_time) {
        this.owner_uid = owner_uid;
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.descriptionText = descriptionText;
    }

    @Override
    public String toString() {
        return "owner_uid: '" + owner_uid +
                "\nTitle: " + title +
                "\nDescriptionTex: " + descriptionText +
                "\nstart_date=" + start_date +
                "\nend_date=" + end_date;
    }

}
