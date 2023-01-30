package com.bitheads.braincloud.services;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

    public static long UTCDateTimeToUTCMillis(Date utcDate) {
        return utcDate.getTime();
    }

    public static Date UTCMillisToUTCDateTime(long utcMillis) {
        return new Date(utcMillis);
    }

    public static Date LocalTimeToUTCTime(Date localDate)
    {
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date utcDate = new Date(localDate.getTime() - TimeZone.getTimeZone(timeZone).getOffset(localDate.getTime())); //subtract for utcTime
        return utcDate;
    }

    public static Date UTCTimeToLocalTime (Date utcDate)
    {
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date localDate = new Date(utcDate.getTime() + TimeZone.getTimeZone(timeZone).getOffset(utcDate.getTime())); //add to get localTime
        return localDate;
    }
}
