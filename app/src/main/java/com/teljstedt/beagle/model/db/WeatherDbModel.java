package com.teljstedt.beagle.model.db;

import android.provider.BaseColumns;

/**
 * Created by SSSGNE on 2017-04-13.
 */
public class WeatherDbModel {

    public static abstract class PrognosTable implements BaseColumns {
        public static final String TABLE_NAME = "prognos";
        public static final String COLUMN_APPROVEDTIME = "approvedtime";
        public static final String COLUMN_REFERENCETIME = "referencetime";
    }

    public static abstract class GeometryTable implements BaseColumns {
        public static final String TABLE_NAME = "geometry";
        public static final String FK_PROGNOS_ID = "prognosid";
        public static final String COLUMN_TYPE = "geoType";
        public static final String COLUMN_P1 = "p1";
        public static final String COLUMN_P2 = "p2";
    }

    public static abstract class TimeSeriesTable implements BaseColumns {
        public static final String TABLE_NAME = "timeseries";
        public static final String FK_PROGNOS_ID = "prognosid";
        public static final String COLUMN_VALIDTIME = "validtime";
    }

    public static abstract class ParametersTable implements BaseColumns {
        public static final String TABLE_NAME = "parameters";
        public static final String FK_TIMESERIES_ID = "timeseriesid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LEVELTYPE = "leveltype";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_VALUE = "value";
    }

}
