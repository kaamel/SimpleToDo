package com.codepath.simpletodo;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kaamel on 8/8/17.
 */

public final class Utils {

    public static DateFormat getDateFormat() {
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    }

    public static boolean validDate(String dueDate) {
        DateFormat formatter = getDateFormat();
        if (dueDate == null || dueDate.trim().equals(""))
            return false;
        formatter.setLenient(false);
        try {
            formatter.parse(dueDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static long todayToLong() {
        return dateToLong(null);
    }

    public static String getTodayString() {
        return longToDateString(System.currentTimeMillis());
    }

    public static long getTodayLong() {
        return dateToLong(null);
    }

    public static String longToDateString(long time) {
        Date date = new Date(time);
        DateFormat formatter = getDateFormat();
        formatter.setLenient(false);
        return formatter.format(date);
    }

    public static long dateToLong(String date) {
        DateFormat formatter = getDateFormat();
        formatter.setLenient(false);
        long time = System.currentTimeMillis();
        try {
            time = formatter.parse(longToDateString(System.currentTimeMillis())).getTime(); //time at the begining of today
            if (date != null && !date.trim().equals("")) {
                time = formatter.parse(formatter.format(formatter.parse(date))).getTime(); //time at the begining of the date
            }
        } catch (ParseException ignored) {
        }
        return time;
    }
}
