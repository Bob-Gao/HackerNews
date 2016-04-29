package net.bobgao.ycombinatorhackernews;

import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by bobgao on 29/4/16.
 */
public class DatetimeHelper {

    public static String showTimeToPresend(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        long duration = calendar.getTime().getTime() / 1000 - timestamp;
        int days = (int) TimeUnit.SECONDS.toDays(duration);
        long hours = TimeUnit.SECONDS.toHours(duration) - TimeUnit.SECONDS.toHours(TimeUnit.SECONDS.toDays(duration));
        long minutes = TimeUnit.SECONDS.toMinutes(duration) - TimeUnit.SECONDS.toMinutes(TimeUnit.SECONDS.toHours(duration));
        long seconds = TimeUnit.SECONDS.toSeconds(duration) - TimeUnit.SECONDS.toSeconds(TimeUnit.SECONDS.toMinutes(duration));
        String time = "";
        if (days > 0) {
            if (days == 1) {
                time = String.format("%d day ago", days);
            } else {
                time = String.format("%d days ago", days);
            }
        } else if (hours > 0) {
            if (hours == 1) {
                time = String.format("%d hour ago", hours);
            } else {
                time = String.format("%d hours ago", hours);
            }
        } else if (minutes > 0) {
            if (minutes == 1) {
                time = String.format("%d minute ago", minutes);
            } else {
                time = String.format("%d minutes ago", minutes);
            }
        } else if (seconds > 0) {
            if (seconds == 1) {
                time = String.format("%d second ago", seconds);
            } else {
                time = String.format("%d seconds ago", seconds);
            }
        }
        return time;
    }
}
