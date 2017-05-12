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
public class TimeSeries {

    protected static final String datePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @SerializedName("validTime")
    public String validTime;

    @SerializedName("parameters")
    public List<Parameters> parametersList = new ArrayList<Parameters>();

    public Date getValidTime() {
        SimpleDateFormat sdf=new SimpleDateFormat(datePattern);
        Date dt;
        try {
            dt = sdf.parse(this.validTime);
            //Log.d("TimeSeries","getValidTime: " + dt.toString());
        }catch (ParseException e) {
            dt= new Date();
            Log.e("TimeSeries","validTime: " + this.validTime + " unparseable using format " + datePattern + " which formats to: " + sdf.format(dt) );
        }
        return dt;
    }

    public TimeSeries(){
    };

    @Override
    public String toString(){
        return "TimeSeries validTime: " + validTime + " , parametersList.size:" + parametersList.size();
    };
}
