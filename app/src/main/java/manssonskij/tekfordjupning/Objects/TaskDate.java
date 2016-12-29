package manssonskij.tekfordjupning.Objects;

/**
 * Created by Anton on 2016-12-21.
 */

public class TaskDate {
    public String start_date;
    public String end_date;
    public String start_time;
    public String end_time;


    public TaskDate() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public TaskDate(String start_date, String end_date, String start_time, String end_time) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
    }
}
