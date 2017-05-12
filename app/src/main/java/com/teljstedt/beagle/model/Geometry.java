package com.teljstedt.beagle.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSSGNE on 2017-04-12.
 */
public class Geometry {

    @SerializedName("type")
    private String type;

    @SerializedName("coordinates")
    private List<List<Double>> coordinateTwinList=new ArrayList<>();

    private List<Double> coordinateList = new ArrayList<>();

    public Geometry(){
    }

    public List<List<Double>> getCoordinateTwinList() {
        return coordinateTwinList;
    }

    public void setCoordinateTwinList(List<List<Double>> coordinateTwinList) {
        this.coordinateTwinList = coordinateTwinList;
        this.coordinateList = this.coordinateTwinList.get(0);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinateList(){
        this.coordinateList = this.coordinateTwinList.get(0);
        return this.coordinateList;
    }

    @Override
    public String toString(){
        this.coordinateList = this.coordinateTwinList.get(0);
        return "Geometry type: " + type + " , Lat: " + coordinateList.get(0) + ", Long: " + coordinateList.get(1) ;
    }
}
