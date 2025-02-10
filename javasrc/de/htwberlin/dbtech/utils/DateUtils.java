package de.htwberlin.dbtech.utils;

import java.time.LocalDate;
import java.util.Calendar;

public class DateUtils {
    public static LocalDate sqlDate2LocalDate(java.sql.Date d) {
        if (d == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        int year = cal.get(Calendar.YEAR);
        // Month indexing start at 0
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return LocalDate.of(year, month, day);
    }

    public static java.sql.Date localDate2SqlDate(LocalDate d) {
        if (d == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        // Month indexing start at 0
        cal.set(d.getYear(), d.getMonthValue() - 1, d.getDayOfMonth(), 0, 0, 0);
        return new java.sql.Date(cal.getTime().getTime());
    }

}