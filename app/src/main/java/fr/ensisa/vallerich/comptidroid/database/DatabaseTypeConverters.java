package fr.ensisa.vallerich.comptidroid.database;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.util.Date;

public class DatabaseTypeConverters {

    @TypeConverter
    public static long bigDecimal2Long(BigDecimal amount) {
        return amount == null ? 0L : amount.longValue();
    }

    @TypeConverter
    public static BigDecimal long2BigDecimal(long amount) {
        return new BigDecimal(amount);
    }
}
