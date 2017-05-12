package com.teljstedt.beagle.model;

import android.util.Log;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SSSGNE on 2017-04-12.
 */
public class Prognos {

    protected static final String datePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @SerializedName("approvedTime")
    public String approvedTime;

    @SerializedName("referenceTime")
    public String referenceTime;

    @SerializedName("geometry")
    public Geometry geometry;

    @SerializedName("timeSeries")
    public List<TimeSeries> timeSeriesList = new ArrayList<TimeSeries>();

    public Date getApprovedTime(){
        SimpleDateFormat sdf=new SimpleDateFormat(datePattern);
        Date dt;
        try {
            dt = sdf.parse(this.approvedTime);
            Log.d("Prognos","getApprovedTime: " + dt.toString());
        }catch (ParseException e) {
            dt= new Date();
            Log.e("Prognos","approvedTime: " + this.approvedTime + " unparseable using format " + datePattern + " which formats to: " + sdf.format(dt) );
        }
        return dt;
    }

    public Date getReferenceTime(){
        SimpleDateFormat sdf=new SimpleDateFormat(datePattern);
        Date dt;
        try {
            dt = sdf.parse(this.referenceTime);
            Log.d("Prognos","getReferenceTime: " + dt.toString());
        }catch (ParseException e) {
            dt= new Date();
            Log.e("Prognos","referenceTime: " + this.referenceTime + " unparseable using format " + datePattern + " which formats to: " + sdf.format(dt) );
        }
        return dt;
    }

    public void setReferenceTime(long timeLong){
        SimpleDateFormat sdf=new SimpleDateFormat(datePattern);
        Date dt = new Date(timeLong);
        referenceTime = sdf.format(dt);
        Log.d("Prognos","setReferenceTime as " + referenceTime);
    }

    public void setApprovedTime(long timeLong){
        SimpleDateFormat sdf=new SimpleDateFormat(datePattern);
        Date dt = new Date(timeLong);
        approvedTime = sdf.format(dt);
        Log.d("Prognos","setApprovedTime as " + approvedTime);
    }


    public Prognos() {
    }

    @Override
    public String toString() {
        return "Prognos approvedTime: " + approvedTime + " ,timeSeries size " + timeSeriesList.size();
    }

}
