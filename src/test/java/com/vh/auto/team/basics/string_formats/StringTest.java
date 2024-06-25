package com.vh.auto.team.basics.string_formats;

import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;

public class StringTest {

    @Test
    public void date_format_test() {

        Date now = new Date();

        // Print the full date and time
        String fullDateTime = String.format("Full Date and Time: %tc", now);
        System.out.println(fullDateTime);

        // Print the date in YYYY-MM-DD format
        String yearMonthDay = String.format("Year-Month-Day: %tF", now);
        System.out.println(yearMonthDay);

        // Print the time in HH:MM:SS AM/PM format
        String timeAmPm = String.format("Time (AM/PM): %tr", now);
        System.out.println(timeAmPm);

        // Print the time in 24-hour format
        String time24Hour = String.format("Time (24-hour): %tT", now);
        System.out.println(time24Hour);

        // Print the abbreviated weekday name
        String dayOfWeek = String.format("Day of Week: %ta", now);
        System.out.println(dayOfWeek);

        // Print the full month name
        String monthName = String.format("Month: %tB", now);
        System.out.println(monthName);

        // Print the day of the month
        String dayOfMonth = String.format("Day of Month: %td", now);
        System.out.println(dayOfMonth);

        // Use Calendar to set a specific date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JUNE, 25, 15, 30, 45);
        Date specificDate = calendar.getTime();

        // Print specific date and time
        String specificDateTime = String.format("Specific Date and Time: %tc", specificDate);
        System.out.println(specificDateTime);

    }
}
