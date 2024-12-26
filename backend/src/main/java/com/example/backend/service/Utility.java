package com.example.backend.service;

import java.sql.Timestamp;

public class Utility {
    public static double getMins(Timestamp t1 , Timestamp t2){
        long diff = Math.abs( t1.getTime() - t2.getTime() );
        double diffMinutes = diff / (60.0 * 1000);
        return Math.round(diffMinutes * 100.0) / 100.0;
    }
    public static double getHours(Timestamp t1 , Timestamp t2){
        long diff = Math.abs( t1.getTime() - t2.getTime() );
        double diffHours = diff / (1000.0 * 60 * 60);
        return Math.round(diffHours * 1000.0) / 1000.0;
    }
    public static double getHourOfTimeStamp(Timestamp t1 ){
        Timestamp t2 = new Timestamp(System.currentTimeMillis());
        long diff = Math.abs( t1.getTime() - t2.getTime() );
        double diffHours = diff / (1000.0 * 60 * 60);
        return Math.round(diffHours * 1000.0) / 1000.0;
    }
    public static double getMinOfTimeStamp(Timestamp t1 ){
        Timestamp t2 = new Timestamp(System.currentTimeMillis());
        long diff = Math.abs( t1.getTime() - t2.getTime() );
        double diffMinutes = diff / (60.0 * 1000);
        return Math.round(diffMinutes * 100.0) / 100.0;
    }

}
