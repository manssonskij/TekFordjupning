package manssonskij.tekfordjupning.PickerFragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;
import java.util.Calendar;

import manssonskij.tekfordjupning.R;

/**
 * Created by Anton on 2016-12-12.
 */



public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Button date_button= (Button) getActivity().findViewById(R.id.date_start_Button);
        date_button.setText(view.getDayOfMonth() + "/" + (view.getMonth()+1) + "/" + view.getYear());

    }
}