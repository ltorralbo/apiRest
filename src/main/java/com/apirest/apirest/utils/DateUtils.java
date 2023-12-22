package com.apirest.apirest.utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class DateUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static String getFechaActual(){
        Date dateAcual = new Date();
        Calendar calendarActual = Calendar.getInstance();
        calendarActual.setTime(dateAcual);
        calendarActual.getTime();
        return sdf.format(calendarActual.getTime());
    }
}
