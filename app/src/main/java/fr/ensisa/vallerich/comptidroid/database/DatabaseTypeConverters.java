package fr.ensisa.vallerich.comptidroid.database;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class DatabaseTypeConverters {

    private static final BigDecimal converter = new BigDecimal(100);

    @TypeConverter
    public BigDecimal fromLong(Long value) {
        return value == null ? BigDecimal.ZERO : new BigDecimal(value).divide(converter);
    }

    @TypeConverter
    public Long toLong(BigDecimal bigDecimal) {
        return bigDecimal == null ? 0L : bigDecimal.multiply(converter).longValue();
    }

    @TypeConverter
    public static Date long2Date (long time) {
        return new Date(time);
    }

    @TypeConverter
    public static long date2Long (Date date) {
        if (date == null) return 0;
        return date.getTime();
    }
}
