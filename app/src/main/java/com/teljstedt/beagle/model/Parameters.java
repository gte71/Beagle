package com.teljstedt.beagle.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSSGNE on 2017-04-12.
 */
public class Parameters {

    @SerializedName("name")
    public String name;

    @SerializedName("levelType")
    public String levelType;

    @SerializedName("level")
    public int level;

    @SerializedName("unit")
    public String unit;

    @SerializedName("values")
    List<Double> valuesList = new ArrayList<Double>();

    public double getValue(){
        return (Double) Double.valueOf(valuesList.get(0));
    }

    public Parameters() {
    }

    @Override
    public String toString() {
        return "Parameters, name: " + name + " ,value: " + valuesList.get(0);
    }
}
