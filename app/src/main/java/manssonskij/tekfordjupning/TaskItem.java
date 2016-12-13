package manssonskij.tekfordjupning;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by Anton on 2016-12-12.
 */
@IgnoreExtraProperties
public class TaskItem {

    public String title;
    public Date start_date;
    public Date end_date;
    public String descriptionText;

    public TaskItem() {
        // Default constructor required for calls to DataSnapshot.getValue(TaskItem.class)
    }

    public TaskItem(String title, String descriptionText) {
        this.title = title;
        this.descriptionText = descriptionText;
    }

}
