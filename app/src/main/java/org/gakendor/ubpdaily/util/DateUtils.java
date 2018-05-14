package org.gakendor.ubpdaily.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dizzay on 1/17/2018.
 */

public class DateUtils {

    public static String getThisMonth(){
        String[] monthName = {"January", "February",
                "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};

        Calendar cal = Calendar.getInstance();
        return monthName[cal.get(Calendar.MONTH)];
    }

    public static String getDateUI(String date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date now = format.parse(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            return dateFormatter.format(now);
        }catch (Exception e){
            e.printStackTrace();
            return null;

        }
    }

    public static String getMonth(String date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date now = format.parse(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM");
            String[] monthName = {"JAN", "FEB",
                    "MAR", "APR", "MAY", "JUN", "JUL",
                    "AUG", "SEP", "OCT", "NOV",
                    "DEC"};
            return monthName[Integer.parseInt(dateFormatter.format(now))-1];
        }catch (Exception e){
            e.printStackTrace();
            return null;

        }

    }

    public static String getDateOnly(String date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date now = format.parse(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd");
            return dateFormatter.format(now);
        }catch (Exception e){
            e.printStackTrace();
            return null;

        }

    }
}
