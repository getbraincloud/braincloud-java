package com.bitheads.braincloud.services;

import com.bitheads.braincloud.client.BrainCloudClient;

import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.Date;

/**
 * Created by prestonjennings on 15-09-02.
 */
public class TimeServiceTest extends TestFixtureBase
{

    @Test
    public void testReadServerTime() throws Exception
    {
        TestResult tr = new TestResult(_wrapper);

        _wrapper.getTimeService().readServerTime(
                tr);

        tr.Run();
    }

    @Test
    public void testTimeUtils() throws Exception
    {
        Date dateBefore = new Date();
        System.out.println(dateBefore);
        long ms = TimeUtil.UTCDateTimeToUTCMillis(dateBefore);
        Date dateAfter = TimeUtil.UTCMillisToUTCDateTime(ms);
        System.out.println(dateAfter);
        assert(dateBefore.getTime() == dateAfter.getTime()); //fail if theyre not the same. 

        Date utcDateBefore = new Date();
        System.out.println("before " + utcDateBefore);
        Date localDate = TimeUtil.UTCTimeToLocalTime(utcDateBefore);
        System.out.println("localdate " + localDate);
        Date utcDateAfter = TimeUtil.LocalTimeToUTCTime(localDate);
        System.out.println("after " + utcDateAfter);
        assert(utcDateBefore.getTime() == utcDateAfter.getTime()); //fail if theyre not the same. 
    }
}
