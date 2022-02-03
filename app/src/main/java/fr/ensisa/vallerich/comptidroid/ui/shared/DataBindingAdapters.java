package fr.ensisa.vallerich.comptidroid.ui.shared;

import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.ensisa.vallerich.comptidroid.R;

public class DataBindingAdapters {

    private static SimpleDateFormat output = null;

    @BindingAdapter("android:text")
    public static void setDate(TextView view, Date date) {
        String text;
        if (date == null) {
            text = view.getResources().getString(R.string.select_date);
        } else {
            if (output == null) {
                output = new SimpleDateFormat("dd MMMM yyyy");
            }
            text = output.format(date);
        }
        view.setText(text);
    }

    @BindingAdapter("android:text")
    public static void setAmount(TextView view, BigDecimal amount) {
        view.setText(amount == null ? "0.0" : amount.toString());
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static BigDecimal setText(TextInputEditText view) {
        if (view.getText() == null) return BigDecimal.ZERO;
        if (view.getText().toString().isEmpty()) return BigDecimal.ZERO;
        BigDecimal inView = new BigDecimal(view.getText().toString());
        return inView;
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getText(EditText view) {
        if (view.getText() == null) return 0;
        if (view.getText().toString().isEmpty()) return 0;
        try {
            int inView = Integer.parseInt(view.getText().toString());
            return inView;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
