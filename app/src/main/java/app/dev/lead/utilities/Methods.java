package app.dev.lead.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Methods {

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void showToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean hasPermissions(Context context, String... permissions) {

        Log.d("theH", "hasPermissions: permission size= " + permissions.length);

        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                continue;
            } else {
                Log.d("theH", "hasPermissions: returning false");
                return false;
            }
        }
        Log.d("theH", "hasPermissions: returning true");
        return true;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void goNextActivityWithOutFinish(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        activity.startActivity(intent);
    }

    public static void goNextActivityWithFinish(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        activity.startActivity(intent);
        activity.finish();
    }

    public static String getStartEndDateOfWeekFromCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(new Date());
        String[] dateArray = date.split("-");
        int day = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]) - 1;
        int year = Integer.parseInt(dateArray[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        while (calendar.get(Calendar.DAY_OF_WEEK) > calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);

        calendar.add(Calendar.DATE, 6);
        Date endDate = calendar.getTime();
        String endDaString = formatter.format(endDate);

        return startDateInStr+" - "+endDaString;
    }

    public static String getStartEndDateOfWeekFromCalendar(int weekNumber) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM", Locale.ENGLISH);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endDate = calendar.getTime();
        String endDaString = formatter.format(endDate);

        return startDateInStr+" - "+endDaString;
    }
}
