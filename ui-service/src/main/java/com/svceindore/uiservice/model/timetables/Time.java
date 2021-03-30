package com.svceindore.uiservice.model.timetables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Date: 16/03/21
 * Time: 7:31 AM
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Time {
    private String start;
    private String end;

    @Override
    public String toString() {
        return "Time{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }

    public String inAmPm(String time) {
        String[] split = time.split(":");
        int h = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        String res = "";
        boolean am = false;
        if (h >= 13)
            h = h - 12;
        else am = true;

        if (h <= 9) res = res + "0";
        res = res + h + ":";

        if (m <= 9) res = res + "0";
        res += m+(am?"AM":"PM");

        return res;
    }

}
