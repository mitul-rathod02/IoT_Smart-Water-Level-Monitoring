package com.example.swlm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WaterLevelDB {
    private double level;
//    private Date timestamp;
    private String datetime;

    public WaterLevelDB() {
        // Default constructor required for Firebase
    }

//    public WaterLevelDB(double level, Date timestamp) {
//        this.level = level;
//        this.timestamp = timestamp;
//    }

    public WaterLevelDB(double level, String datetime) {
        this.level = level;
        this.datetime = datetime;
    }
    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

//    public Date getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Date timestamp) {
//        this.timestamp = timestamp;
//    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
