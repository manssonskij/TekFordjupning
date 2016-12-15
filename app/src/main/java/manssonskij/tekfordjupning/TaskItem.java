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
    public Date start_date;
    public Date end_date;
    public String descriptionText;

    public TaskItem() {
        // Default constructor required for calls to DataSnapshot.getValue(TaskItem.class)
    }

    public TaskItem(String owner_uid, String title, String descriptionText) {
        this.owner_uid = owner_uid;
        this.title = title;
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
