package gov.smart.health.activity.vr.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Date 2017/5/16
 * @Author wenzheng.liu
 * @Description 心率，可按时间排序
 */
public class SHHeartRate implements Serializable {
    public String time;
    public String value;

    public Calendar strDate2Calendar(String strDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "SHHeartRate{" +
                "time='" + time + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
