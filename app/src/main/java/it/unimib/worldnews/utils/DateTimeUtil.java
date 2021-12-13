package it.unimib.worldnews.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Util class to convert the date and time associated with an article in different formats.
 */
public class DateTimeUtil {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private DateTimeUtil() {}

    /**
     * It converts the date and time based on the user settings.
     *
     * @param dateTime The date and time to be converted.
     * @return The date and time converted based the user settings.
     */
    public static String getDate(String dateTime) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault());
        SimpleDateFormat outputDateFormat = null;

        Locale italianLocale = new Locale("it","IT","");

        if (Locale.getDefault().equals(italianLocale)) {
            outputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        } else {
            outputDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        }

        Date parsedDate = null;

        try {
            parsedDate = simpleDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (parsedDate != null) {
            return outputDateFormat.format(parsedDate);
        }

        return null;
    }
}
