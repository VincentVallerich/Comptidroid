package fr.ensisa.vallerich.comptidroid.ui.shared;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.ensisa.vallerich.comptidroid.R;

public class DataBindingAdapters {

    private static SimpleDateFormat output = null;

    @BindingAdapter("android:text")
    public static void setDate(TextView view, Date date) {
        String text;
        if (date == null) {
            text = view.getResources().getString(R.string.nodate);
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

    @BindingAdapter("android:text")
    public static void setLong(TextView view, Long l){
        view.setText(l == null ? "0" : l.toString());
    }
}
