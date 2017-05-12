package com.teljstedt.beagle.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.teljstedt.beagle.model.Geometry;
import com.teljstedt.beagle.model.Parameters;
import com.teljstedt.beagle.model.Prognos;
import com.teljstedt.beagle.model.TimeSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by sssgne on 2017-04-03.
 */
public class WeatherDatabaseHandler extends SQLiteOpenHelper {

    private static WeatherDatabaseHandler sInstance;

    private static final String TEXT_TYPE = " TEXT ";
    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String NUMBER_TYPE = " REAL ";
    private static final String DATE_TYPE = " INTEGER ";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "weatherdata";

    private static SQLiteDatabase db;

    public static synchronized WeatherDatabaseHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new WeatherDatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */

    private WeatherDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    ///////////////////////////////////////////

    public void deleteContent(){
        Log.d("WeatherDatabaseHandler","deleteContent");
        db.delete(WeatherDbModel.ParametersTable.TABLE_NAME,null,null);
        db.delete(WeatherDbModel.TimeSeriesTable.TABLE_NAME,null,null);
        db.delete(WeatherDbModel.GeometryTable.TABLE_NAME,null,null);
        db.delete(WeatherDbModel.PrognosTable.TABLE_NAME,null,null);
    }

    public Parameters getParameters(int id){
        Parameters parameters = new Parameters();
        return parameters;
    }

    public void setParameters(long timeId, Parameters parameters){
        //Log.d("setParameters",parameters.toString() );
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WeatherDbModel.ParametersTable.FK_TIMESERIES_ID,timeId);
        values.put(WeatherDbModel.ParametersTable.COLUMN_LEVEL,parameters.level);
        values.put(WeatherDbModel.ParametersTable.COLUMN_LEVELTYPE,parameters.levelType);
        values.put(WeatherDbModel.ParametersTable.COLUMN_NAME ,parameters.name);
        values.put(WeatherDbModel.ParametersTable.COLUMN_UNIT ,parameters.unit);
        values.put(WeatherDbModel.ParametersTable.COLUMN_VALUE ,parameters.getValue());

        long paramId = db.insert(WeatherDbModel.ParametersTable.TABLE_NAME,null,values);

        //Log.d("setParameters","_id:" + paramId);
    }

    public TimeSeries getTimeseries(int id){
        TimeSeries timeSeries = new TimeSeries();
        return timeSeries;
    }

    public void setTimeseries(long progId, TimeSeries timeseries){
        //Log.d("setTimeseries",timeseries.toString());
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WeatherDbModel.TimeSeriesTable.FK_PROGNOS_ID,progId);
        values.put(WeatherDbModel.TimeSeriesTable.COLUMN_VALIDTIME,timeseries.getValidTime().getTime() );

        long timeId = db.insert(WeatherDbModel.TimeSeriesTable.TABLE_NAME,null,values);

        List<Parameters> paramList=timeseries.parametersList;
        for (ListIterator<Parameters> paramListIterator = paramList.listIterator(); paramListIterator.hasNext(); ){
            setParameters(timeId,paramListIterator.next() );
        }

        //Log.d("setTimeseries","_id:" + timeId);
    }

    public Geometry getGeometry(int id){
        Geometry geometry = new Geometry();
        return geometry;
    }

    public void setGeometry(long progId, Geometry geometry){
        Log.d("setGeometry",geometry.toString());
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WeatherDbModel.GeometryTable.FK_PROGNOS_ID,progId);
        values.put(WeatherDbModel.GeometryTable.COLUMN_TYPE,geometry.getType());
        values.put(WeatherDbModel.GeometryTable.COLUMN_P1,geometry.getCoordinateList().get(0) );
        values.put(WeatherDbModel.GeometryTable.COLUMN_P2,geometry.getCoordinateList().get(1) );

        long geoId = db.insert(WeatherDbModel.GeometryTable.TABLE_NAME,null,values);

        Log.d("setGeometry","_id:" + geoId);
    }

    public Boolean prognosExists(String refTime){
        Boolean exists = Boolean.FALSE;
        Cursor cursor = db.query(WeatherDbModel.PrognosTable.TABLE_NAME
                ,new String[] {WeatherDbModel.PrognosTable.COLUMN_REFERENCETIME}
                ,WeatherDbModel.PrognosTable.COLUMN_REFERENCETIME + "=?"
                ,new String[] {refTime}
                ,null,null,null);
        //exists = cursor.moveToFirst();
        if ( cursor.moveToFirst() ){
            // exists
            exists=Boolean.TRUE;
            Log.d("prognosExists","search for: " + refTime + " found");
        } else {
            // does not exist
            Log.d("prognosExists","search for: " + refTime + " NOT found");
            exists=Boolean.FALSE;
        }
        cursor.close();
        return exists;
    }

    public Prognos getPrognos(String progId){
        Prognos prognos = new Prognos();

        Cursor cursor = db.query( WeatherDbModel.PrognosTable.TABLE_NAME
                ,null   // all colummns
                ,"_id = ?"
                ,new String[]{progId}
                ,null   // group by
                ,null   // having
                ,null   // order by
                );
        if (cursor.moveToFirst()){
            prognos.setApprovedTime(cursor.getLong(cursor.getColumnIndexOrThrow(WeatherDbModel.PrognosTable.COLUMN_APPROVEDTIME)));
            prognos.setReferenceTime(cursor.getLong(cursor.getColumnIndexOrThrow(WeatherDbModel.PrognosTable.COLUMN_REFERENCETIME)));
            Log.d("getPrognos","get prognos for _id:"+ progId + ", referenceTime:" + prognos.referenceTime );


        } else {
            Log.d("getPrognos","could not find prognos for _id:" + progId);
        }
        cursor.close();
        return prognos;
    }

    public Prognos getLatestPrognos() {
        Prognos prognos = new Prognos();
        String sqlStmt = "SELECT * from " + WeatherDbModel.PrognosTable.TABLE_NAME + " ORDER BY " + WeatherDbModel.PrognosTable.COLUMN_REFERENCETIME + " DESC";
        Log.d("getLatestPrognos",sqlStmt);
        Cursor cursor = db.rawQuery(sqlStmt,null);
        if ( cursor.moveToFirst() ) {
            String progId = cursor.getString( cursor.getColumnIndexOrThrow(WeatherDbModel.PrognosTable._ID) );
            prognos = getPrognos(progId);
        }
        cursor.close();
        return prognos;
    }

    public void setPrognos(Prognos prognos){
        Log.d("setPrognos",prognos.toString() );
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WeatherDbModel.PrognosTable.COLUMN_APPROVEDTIME,prognos.getApprovedTime().getTime());
        values.put(WeatherDbModel.PrognosTable.COLUMN_REFERENCETIME,prognos.getReferenceTime().getTime());

        long progId = db.insert(WeatherDbModel.PrognosTable.TABLE_NAME,null,values);

        setGeometry(progId,prognos.geometry);

        List<TimeSeries> timeSeriesList= prognos.timeSeriesList;
        for (ListIterator<TimeSeries> timeSeriesListIterator = timeSeriesList.listIterator(); timeSeriesListIterator.hasNext(); ){
            setTimeseries(progId,timeSeriesListIterator.next());
        }

        Log.d("setPrognos","_id:" + progId);
    }

    ///////////////////////////////////////////

    @Override
    public void onOpen(SQLiteDatabase db) {super.onOpen(db);
    Log.d("WeatherDatabaseHandler","onOpen");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  // changes to datamodel
        // Drop older tables if they exists
        Log.d("WeatherDatabaseHandler", "onUpgrade old->new version " + Integer.toString(oldVersion) + " -> " + Integer.toString(newVersion));
        db.execSQL("DROP TABLE IF EXISTS " + WeatherDbModel.PrognosTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WeatherDbModel.GeometryTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WeatherDbModel.ParametersTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WeatherDbModel.TimeSeriesTable.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // create table statements
        // create table Prognos
        Log.d("WeatherDatabaseHandler", "onCreate");

        String CREATE_TABLE_CMD = "CREATE TABLE " + WeatherDbModel.PrognosTable.TABLE_NAME + " ("
                + WeatherDbModel.PrognosTable.COLUMN_APPROVEDTIME + DATE_TYPE + ", "
                + WeatherDbModel.PrognosTable.COLUMN_REFERENCETIME + DATE_TYPE + " "
                + ")";
        Log.d("WeatherDatabaseHandler", "onCreate, cmd: " + CREATE_TABLE_CMD);
        db.execSQL(CREATE_TABLE_CMD);

        CREATE_TABLE_CMD = "CREATE TABLE " + WeatherDbModel.GeometryTable.TABLE_NAME + " ("
                + WeatherDbModel.GeometryTable.COLUMN_TYPE + TEXT_TYPE + ", "
                + WeatherDbModel.GeometryTable.COLUMN_P1 + TEXT_TYPE + ", "
                + WeatherDbModel.GeometryTable.COLUMN_P2 + TEXT_TYPE + ", "
                + WeatherDbModel.GeometryTable.FK_PROGNOS_ID + INTEGER_TYPE + ", "
                + "FOREIGN KEY( " + WeatherDbModel.GeometryTable.FK_PROGNOS_ID + " ) REFERENCES " + WeatherDbModel.PrognosTable.TABLE_NAME + " ( _ID ) "
                + ")";
        Log.d("WeatherDatabaseHandler", "onCreate, cmd: " + CREATE_TABLE_CMD);
        db.execSQL(CREATE_TABLE_CMD);

        CREATE_TABLE_CMD = "CREATE TABLE " + WeatherDbModel.TimeSeriesTable.TABLE_NAME + " ("
                + WeatherDbModel.TimeSeriesTable.COLUMN_VALIDTIME + DATE_TYPE + ", "
                + WeatherDbModel.TimeSeriesTable.FK_PROGNOS_ID + INTEGER_TYPE + ", "
                + "FOREIGN KEY( " + WeatherDbModel.TimeSeriesTable.FK_PROGNOS_ID + " ) REFERENCES " + WeatherDbModel.PrognosTable.TABLE_NAME + " ( _ID ) "
                + ")";
        Log.d("WeatherDatabaseHandler", "onCreate, cmd: " + CREATE_TABLE_CMD);
        db.execSQL(CREATE_TABLE_CMD);

        CREATE_TABLE_CMD = "CREATE TABLE " + WeatherDbModel.ParametersTable.TABLE_NAME + " ("
                + WeatherDbModel.ParametersTable.COLUMN_NAME + TEXT_TYPE + ", "
                + WeatherDbModel.ParametersTable.COLUMN_LEVELTYPE + TEXT_TYPE + ", "
                + WeatherDbModel.ParametersTable.COLUMN_LEVEL + TEXT_TYPE + ", "
                + WeatherDbModel.ParametersTable.COLUMN_UNIT + TEXT_TYPE + ", "
                + WeatherDbModel.ParametersTable.COLUMN_VALUE + NUMBER_TYPE + ", "
                + WeatherDbModel.ParametersTable.FK_TIMESERIES_ID + INTEGER_TYPE + ", "
                + "FOREIGN KEY( " + WeatherDbModel.ParametersTable.FK_TIMESERIES_ID + " ) REFERENCES " + WeatherDbModel.TimeSeriesTable.TABLE_NAME + " ( _ID ) "
                + ")";
        Log.d("WeatherDatabaseHandler", "onCreate, cmd: " + CREATE_TABLE_CMD);
        db.execSQL(CREATE_TABLE_CMD);
    }

}
