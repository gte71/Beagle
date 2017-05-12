package com.teljstedt.beagle.model;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teljstedt.beagle.model.db.WeatherDatabaseHandler;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSSGNE on 2017-04-12.
 */
public class WeatherData {

    public String lon,lat;
    public Prognos prognos;

    private static String lTag="WeatherData";
    public RequestQueue requestQueue;

    public WeatherData(Context context,String lon, String lat){

        // call db and store prognos
        final WeatherDatabaseHandler weatherDatabaseHandler = WeatherDatabaseHandler.getInstance(context);
        //weatherDatabaseHandler.deleteContent();

        requestQueue = Volley.newRequestQueue(context);
        this.lon=lon;
        this.lat=lat;
        String prognosUrl = "http://opendata-download-metfcst.smhi.se/api/category/pmp2g/version/2/geotype/point/";
        prognosUrl = prognosUrl + "lon/" + lon + "/lat/" + lat + "/data.json";
        Log.d(lTag,"prognosURL: " + prognosUrl);

        JsonObjectRequest jsObjReqPrognos = new JsonObjectRequest(Method.GET, prognosUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                lTag = "onResponse";
                Log.d(lTag, "onResponse: " + jsonObject.toString());
                try {
                    Gson gson = new Gson();
                    prognos = gson.fromJson(jsonObject.toString(), Prognos.class);
                    //Log.d(lTag, prognos.toString());
                    //Log.d(lTag, prognos.geometry.getType() );

                    //Double p1 = prognos.geometry.getCoordinateList().get(0);
                    //Double p2 = prognos.geometry.getCoordinateList().get(1);
                    //Log.d(lTag, "Longitude:" + String.valueOf(p1) + " ,Latitude:" + String.valueOf(p2) );

                    //Log.d(lTag,String.valueOf(prognos.getApprovedTime()) );
                    if ( ! weatherDatabaseHandler.prognosExists(prognos.referenceTime) ){
                        Log.d("onResponse","new prognos");
                        weatherDatabaseHandler.setPrognos(prognos);
                    }else {
                        Log.d("onResponse","prognos already exists for refTime: " + prognos.referenceTime);
                    }

                } catch (JsonParseException e) {
                    e.printStackTrace();
                    Log.e(lTag, "onResponse catch error: " + e.toString());
                }
            } // onResponse
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lTag = "onErrorResponse";
                Log.d(lTag,error.toString() );
            }
        }); // new JsonObjectRequest
        Log.d(lTag,"Add JsonObjectRequest to requestQueue");
        requestQueue.add(jsObjReqPrognos);
    }

    public WeatherData(Context context) {
        this(context,"17.675529","59.185522");
    }
}
