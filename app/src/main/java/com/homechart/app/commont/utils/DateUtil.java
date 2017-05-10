package com.homechart.app.commont.utils;

import android.content.Context;
import android.util.Log;

import com.homechart.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author yangxuewu .
 * @version v1.0 .
 * @date 2016-6-8 .
 * @file DateUtil.java .
 * @brief 日期转换工具类 .
 */
public class DateUtil {

    private static String TAG = "DateUtil";
    //"March 14, 2016 11:08:35"
    private static long timeStemp(String dateString){
        long timeStemp = 0L;
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.US);
        TimeZone tz = TimeZone.getTimeZone("UTC");
        sdf.setTimeZone(tz);
        Date s;
        try {
            s = sdf.parse(dateString);
            sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
            String dssate = sdf.format(s);
            Date date = sdf.parse(dssate);
            timeStemp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStemp;
    }
    public static String getTimeMY(String dateString) {
        long timeStemp = timeStemp(dateString);
        return showTime(timeStemp);
    }

    public static String getTimeMYD(String dateString){
        long timeStemp = timeStemp(dateString);
        return showDate(timeStemp);
    }

    public static String getStringDateByFormat(long lTime, String format) {
        Date currentTime = new Date(lTime);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    public static boolean isYestoday(long lTime) {
        Calendar c = Calendar.getInstance();
        Calendar data = Calendar.getInstance();
        data.setTimeInMillis(lTime);
        if (c.get(Calendar.YEAR) != data.get(Calendar.YEAR)) {
            return false;
        }
        int cDay = c.get(Calendar.DAY_OF_YEAR);
        int dataDay = data.get(Calendar.DAY_OF_YEAR);
        return 1 == (cDay - dataDay);

    }

    public static boolean isToday(long lTime) {
        Calendar c = Calendar.getInstance();
        Calendar data = Calendar.getInstance();
        data.setTimeInMillis(lTime);
        if (c.get(Calendar.YEAR) != data.get(Calendar.YEAR)) {
            return false;
        } else if (c.get(Calendar.DAY_OF_YEAR) != data
                .get(Calendar.DAY_OF_YEAR)) {
            return false;
        }
        return true;
    }

    public static boolean isYesterday(Date date) {
        Calendar c = Calendar.getInstance();
        Calendar data = Calendar.getInstance();
        data.setTime(date);
        if (c.get(Calendar.YEAR) != data.get(Calendar.YEAR)) {
            return false;
        }
        int cDay = c.get(Calendar.DAY_OF_YEAR);
        int dataDay = data.get(Calendar.DAY_OF_YEAR);
        return 1 == (cDay - dataDay);
    }

    public static boolean isToday(Date date) {
        Calendar c = Calendar.getInstance();
        Calendar data = Calendar.getInstance();
        data.setTime(date);
        if (c.get(Calendar.YEAR) != data.get(Calendar.YEAR)) {
            return false;
        } else if (c.get(Calendar.DAY_OF_YEAR) != data
                .get(Calendar.DAY_OF_YEAR)) {
            return false;
        }
        return true;
    }

    public static boolean isThisWeek(long lTime) {
        Calendar data = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        data.setTimeInMillis(lTime);
        if (data.get(Calendar.YEAR) != c.get(Calendar.YEAR)) {
            return false;
        } else if (data.get(Calendar.WEEK_OF_YEAR) == c
                .get(Calendar.WEEK_OF_YEAR)) {
            return true;
        }
        return false;
    }

    public static String getWeekString(long lTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(lTime);
        String week = null;
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                week = UIUtils.getString(R.string.sunday);
                break;
            case Calendar.MONDAY:
                week = UIUtils.getString(R.string.monday);
                break;
            case Calendar.TUESDAY:
                week = UIUtils.getString(R.string.tuesday);
                break;
            case Calendar.WEDNESDAY:
                week = UIUtils.getString(R.string.wednesday);
                break;
            case Calendar.THURSDAY:
                week = UIUtils.getString(R.string.thursday);
                break;
            case Calendar.FRIDAY:
                week = UIUtils.getString(R.string.friday);
                break;
            case Calendar.SATURDAY:
                week = UIUtils.getString(R.string.saturday);
                break;
            default:
                break;
        }
        return week;
    }

    /**
     * @param lTime
     * @return
     */
    public static String showTime(long lTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(lTime);
        if (isToday(lTime)) {
            return getStringDateByFormat(lTime, "HH:mm");
        } else if (isYestoday(lTime)) {
            return UIUtils.getString(R.string.yesterday);
        } else {

            if (isThisWeek(lTime))
                return getWeekString(lTime);
            else
                return getStringDateByFormat(lTime, "yyyy-MM-dd");
        }

    }

    public static String showDate(long lTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(lTime);
        return getStringDateByFormat(lTime, "yyyy-MM-dd HH:mm");
    }

    //Returns the user's preferred locale
    private static Locale getPreferredLocale() {
        return Locale.getDefault();
    }


    public static String getStringDateByFormat(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, getPreferredLocale());
        String dateString = formatter.format(date);
        return dateString;
    }


    public static String formattedTimeFromDate(Date date) {
        return getStringDateByFormat(date, "h:mm a");
    }

    public static String formattedDateFromDate(Date date) {
        return getStringDateByFormat(date, "MM/dd");
    }

    //it is doing same as formattedTimeFromDate
    // just adding this to sync with iOS interfaces
    public static String formattedTimeFromDateForMessageCell(Date date) {
        return formattedTimeFromDate(date);
    }

    public static String formattedStringFromDateForChatRoom(Context context, Date date) {

        if (isToday(date)) {
            return context.getString(R.string.today);
        } else if (isYesterday(date)) {
            return context.getString(R.string.yesterday);
        }

        return formattedDateFromDate(date);
    }

    public static String formattedStringFromDateForChatList(Context context, Date date) {

        //TODO: needs to localized these strings
        if (isToday(date)) {
            return formattedTimeFromDate(date);
        } else if (isYesterday(date)) {
            return context.getString(R.string.yesterday);
        }

        return formattedDateFromDate(date);
    }

    public static boolean checkIfDateChangeIn(Date date1, Date date2) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        return (!((calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)) &&
                (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) &&
                (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) &&
                (calendar1.get(Calendar.ERA) == calendar2.get(Calendar.ERA))));
    }


    public static Date acsDateToDate(String acsDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;

        try {
            date = dateFormat.parse(acsDate);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        return date;

    }

    public static String dateToAcsDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    /**
     * 将指定时间字符串转换为对应的格式
     *
     * @param date       时间字符串 如："2008-10-19 10:11:30"
     * @param fromFormat 与 date 对应的字符串格式 如："yyyy-MM-dd HH:mm:ss"
     * @param toFormat   要转换成的格式 如："yyyy年MM月dd日 HH时mm分ss秒"
     * @return 2008年10月19日 10时11分30秒
     */
    public static String dateFormat(String date, String fromFormat, String toFormat) {
        SimpleDateFormat sdf1 = new SimpleDateFormat(fromFormat); // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(toFormat); // 实例化模板对象
        Date d = null;
        try {
            d = sdf1.parse(date); // 将给定的字符串中的日期提取出来
        } catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace(); // 打印异常信息
        }
        return sdf2.format(d);// 将日期变为新的格式
    }
}
