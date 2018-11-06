package com.example.tarikul.volleyjson;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tarikul.volleyjson.data.WeatherInfoListContract;
import com.example.tarikul.volleyjson.data.WeatherInfoListDbHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;
    private SQLiteDatabase mdb;
    private String temperature,countryName;
    private TextView mDisplay;
    WeatherInfoListDbHelper dbHelper = new WeatherInfoListDbHelper(this);
    Cursor cursor;
    //private String urlJsonObj = "https://api.androidhive.info/volley/person_object.json";

    // json array response url
    //private String urlJsonArry = "https://api.androidhive.info/volley/person_array.json";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // btnMakeObjectRequest = (Button) findViewById(R.id.btnObjRequest);
        mTextViewResult = findViewById(R.id.text_view_result);
        Button buttonParse = findViewById(R.id.button_parse);
        mDisplay = (TextView) findViewById(R.id.text_view_result);



        mdb = dbHelper.getWritableDatabase();


        mQueue = Volley.newRequestQueue(this);
         cursor = getAlldata();
        //how to retrive crsor data




        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
                String rs = dbHelper.getData();
                mDisplay.setText(rs);
            }
        });


    }


// Start the queue
    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void jsonParse() {

        String url = "https://samples.openweathermap.org/data/2.5/weather?" +
                "q=London,uk&appid=b6907d289e10d714a6e88b30761fae22";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        mTextViewResult.setText("Response: " + response.toString());
//                    }
                        try {
                            JSONObject coord = response.getJSONObject("main");
                            double temp = coord.getDouble("temp");
                            temperature = String .valueOf(temp);
                            //JSONObject country = response.getJSONObject("name");
                           // countryName = response.getString("name");

                            //mTextViewResult.append(String .valueOf(temp) + "\n\n");
                            JSONArray jsonArray = response.getJSONArray("weather");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);

                                String main = employee.getString("main");
                                String description = employee.getString("description");
                                //String mail = employee.getString("mail");

                               mTextViewResult.append(main + ", " + description  + "\n\n");
                            }
                            addinfo(temperature,"london");
                            dbHelper = new WeatherInfoListDbHelper(MainActivity.this);
                            mdb = dbHelper.getReadableDatabase();
                            cursor = mdb.rawQuery("SELECT *from " + WeatherInfoListContract.WeatherInfoListEntry.TABLE_NAME, null);
                            if (cursor != null) {
                                if (cursor.getCount() > 0) {

                                    cursor.moveToFirst();
                                    //DataFish fishData = new DataFish();

                                    //Retrieving User FullName and Email after successfull login and passing to LoginSucessActivity
                                    mDisplay.setText(cursor.getString(cursor.getColumnIndex(WeatherInfoListContract.WeatherInfoListEntry.COLUMN_TEMPERATURE)));
                                    //fishData.catName = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_EMAIL));

                                    //    Log.d("NAME", cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_FULLNAME)));
                                    //f=cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_FULLNAME));
                                    //Log.d("NAMEE",f);
                                    //data.add(fishData);

                                }



                            }
                            // Setup and Handover data to recyclerview
                            mRVFishPrice = (RecyclerView) findViewById(R.id.fishPriceList);
                            mAdapter = new AdapterFish(MainActivity.this, data);
                            mRVFishPrice.setAdapter(mAdapter);
                            mRVFishPrice.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private Cursor getAlldata(){
        // TODO (6) Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
        return mdb.query(
                WeatherInfoListContract.WeatherInfoListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WeatherInfoListContract.WeatherInfoListEntry.COLUMN_TEMPERATURE
        );
    }

 private  long addinfo(String temperature, String country){
     // Gets the data repository in write mode
    // SQLiteDatabase db = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
     ContentValues values = new ContentValues();
     values.put(WeatherInfoListContract.WeatherInfoListEntry.COLUMN_COUNTRY,  country);
     values.put(WeatherInfoListContract.WeatherInfoListEntry.COLUMN_TEMPERATURE, temperature);


// Insert the new row, returning the primary key value of the new row
     return mdb.insert(WeatherInfoListContract.WeatherInfoListEntry.TABLE_NAME, null, values);
 }



// ...
}
