package com.arifacar.service.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtil {

    public static final String DATE_FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String DATE_FORMAT_dd_MM_yyyy = "dd-MM-yyyy";
    public static final String DATE_FORMAT_dd_MM_yyyy_HH_mm = "dd-MM-yyyy HH:mm";

    public static String convertDateStringToDateString(String dateText, String dateFormat, String convertedFormat) {
        String date = null;
        if (StringUtil.isNothing(dateText))
            return null;
        try {
            date = convertDateToString(convertDateStringToDate(dateText, dateFormat).getTime(), convertedFormat);
        } catch (Exception e) {
            // Log here
        }
        return date;
    }

    public static Date convertDateStringToDate(String dateText, String dateFormat) {
        if (StringUtil.isNothing(dateText))
            return null;
        DateFormat df = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Date date = new Date();
        try {
            date = df.parse(dateText);
        } catch (ParseException e) {
            // Log here
        }
        return date;
    }

    public static String convertDateToString(Long dateTime, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        String date = df.format(dateTime);
        return date;
    }

    public static String convertDateToString(Date date, String dateFormat) {
        if (date == null)
            return null;
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }

    public static int compareDatesInString(String s1, String s2, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        int result = 0;
        if (StringUtil.isNothing(s1) || StringUtil.isNothing(s2))
            return result;

        try {
            Date d1 = sdf.parse(s1);
            Date d2 = sdf.parse(s2);
            result = d1.after(d2) ? 1 : (d2.after(d1) ? -1 : 0);
        } catch (ParseException e) {
            // Log here
        }
        return result;
    }

    public static Date addDayToDate(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    public static Date addYearToDate(Date date, int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }

    public static Date convertDateToFormattedDate(Date date, String dateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        try {
            Date convertedDate = df.parse(df.format(date));
            return convertedDate;
        } catch (ParseException e) {
            // Log here
        }
        return null;
    }

    public static int dateDifference(Date d1, Date d2) {
        if (d1.before(d2))
            return (int) ((d2.getTime() - d1.getTime()) / 86400000);
        else
            return (int) ((d1.getTime() - d2.getTime()) / 86400000);
    }

    public static int dateDifference(String d1, String d2) {

        return ((int) ((convertDateStringToDate(d1, DateUtil.DATE_FORMAT_dd_MM_yyyy).getTime() - (convertDateStringToDate(d2, DateUtil.DATE_FORMAT_dd_MM_yyyy)).getTime()) / 86400000));
    }

    public static double dateDifferenceByTime(Date d1, Date d2) {
        return ((d1.getTime() - d2.getTime()) / (1000 * 60 * 60));
    }

    public static int getDay(Date d1) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(Date d1) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getYear(Date d1) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        return cal.get(Calendar.YEAR);
    }
}