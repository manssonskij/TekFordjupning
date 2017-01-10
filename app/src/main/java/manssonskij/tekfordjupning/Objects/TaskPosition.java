package manssonskij.tekfordjupning.Objects;

/**
 * Created by Anton on 2016-12-22.
 */

public class TaskPosition {

    public double latitude;
    public double longitude;

    public TaskPosition() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public TaskPosition(double latitude,double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return  "Latitude: " + latitude +
                "\nLongitude: " + longitude;
    }
}
