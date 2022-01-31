package fr.ensisa.vallerich.comptidroid.ui.picker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    static public final String DATE = "date";
    private String requestKey;
    private Date initial;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DatePickerFragmentArgs arg = DatePickerFragmentArgs.fromBundle(getArguments());
        requestKey = arg.getRequestKey();
        initial = new Date(arg.getDate());
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(initial);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getContext(), this, year, month, day);
        if (arg.getTitle() != 0) {
            dialog.setTitle(arg.getTitle());
        }
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(initial);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        long date = calendar.getTimeInMillis();
        Bundle result = new Bundle();
        result.putLong(DATE, date);
        getParentFragmentManager().setFragmentResult(requestKey, result);
    }
}
