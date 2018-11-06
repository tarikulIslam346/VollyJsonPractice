package com.example.tarikul.volleyjson.data;

import android.provider.BaseColumns;

public class WeatherInfoListContract {
    public static class WeatherInfoListEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_TEMPERATURE= "temperature";

    }
}
