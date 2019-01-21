package com.example.a10008881.weatherdemo;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    Button button;
    TextView t1,t2,t3,fc1day,fc1hi,fc1lo,fc2day,fc2hi,fc2lo,fc3day,fc3lo,fc3hi,fc4day,fc4lo,fc4hi,fc5day,fc5hi,fc5lo,qb;
    EditText eT;
    String degrees = "deg";
    String code = "code";
    String city="city";
    String state="st";
    String condition="cond";
    ImageView imageView;
    final ArrayList<String> quotes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       //WeatherThread downloadWeather = new WeatherThread();
       // downloadWeather.execute();


        button = (Button)findViewById(R.id.button_id);
        t1= (TextView)findViewById(R.id.textView_id);
        t2=(TextView)findViewById(R.id.textView2_id);
        t3=(TextView)findViewById(R.id.textView3_id);
        qb=(TextView)findViewById(R.id.quotebox);
        eT = (EditText)findViewById(R.id.editText_id);
        imageView = (ImageView)findViewById(R.id.imageView_id);
        fc1day=(TextView)findViewById(R.id.fc1_day);
        fc1hi=(TextView)findViewById(R.id.fc1_hi);
        fc1lo=(TextView)findViewById(R.id.fc1_lo);
        fc2day=(TextView)findViewById(R.id.fc2_day);
        fc2hi=(TextView)findViewById(R.id.fc2_hi);
        fc2lo=(TextView)findViewById(R.id.fc2_lo);
        fc3day=(TextView)findViewById(R.id.fc3_day);
        fc3hi=(TextView)findViewById(R.id.fc3_hi);
        fc3lo=(TextView)findViewById(R.id.fc3_lo);
        fc4day=(TextView)findViewById(R.id.fc4day);
        fc4hi=(TextView)findViewById(R.id.fc4hi);
        fc4lo=(TextView)findViewById(R.id.fc4lo);
        fc5day=(TextView)findViewById(R.id.fc5day);
        fc5hi=(TextView)findViewById(R.id.fc5hi);
        fc5lo=(TextView)findViewById(R.id.fc5lo);

        quotes.add("The weather will be interesting tomorrow");
        quotes.add("It be dark at night");
        quotes.add("Dancing in the rain");
        quotes.add("Get wet when it rains");
        quotes.add("When snow falls, nature listens");
        quotes.add("A rainy day is the perfect time for a walk in the woods");
        quotes.add("Rainbow apologizes for an angry sky");
        quotes.add("Let the rain kiss you");
        quotes.add("Let the rain sing you a lullaby");
        quotes.add("Weather is the metaphor for life");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    WeatherThread downloadWeather = new WeatherThread();
                    downloadWeather.execute(eT.getText().toString());
                 //   Log.d("INCLICK_DEG ", degrees);
                //    Log.d("INCLICK_CITY", city);
                //    t2.setText(city+", "+state+" "+condition);
               //     t3.setText(degrees+" °");
                   imageView.setImageResource(0);



            }
        });


    }

    public class WeatherThread extends AsyncTask<String, Void, Void> {
        JSONObject obj;
        String output;

        @Override
        protected Void doInBackground(String... params) {
            try {

                String qstr = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")",params[0]);
                String urlstr = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(qstr));
               // URL myURL = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%2208852%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
                Log.d("DOBACK URL ", urlstr);
                    URL myURL = new URL(urlstr);
                URLConnection connect;
                connect = myURL.openConnection();
                InputStream input = connect.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                output = reader.readLine();








            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                obj = new JSONObject(output);
                Log.d("WeatherData",obj.toString());
                // JSON object that holds weather info

                JSONObject obj2 =obj.getJSONObject("query"); //getting city of api
                Log.d("Query",obj2.toString());
                JSONObject obj3=obj2.getJSONObject("results");
                Log.d("Result",obj3.toString());
                JSONObject obj4=obj3.getJSONObject("channel");
                Log.d("Channel",obj4.toString());
                JSONObject obj5=obj4.getJSONObject("location");
                Log.d("Location",obj5.toString());
                city=obj5.getString("city");
                state=obj5.getString("region");
                Log.d("CITY",city);
                Log.d("STATE",state);

                JSONObject obj6 =obj4.getJSONObject("item");
                JSONObject obj7= obj6.getJSONObject("condition");
                degrees = obj7.getString("temp");
                condition = obj7.getString("text");
                code=obj7.getString("code");



                Log.d("TEMP",degrees);
                Log.d("CONDITION",condition);

                t2.setText(city+", "+state+" "+condition);
                t3.setText(degrees+"°");

                int imgid = getResources().getIdentifier("drawable/w" + code, null, getPackageName());
              //  imageView.setImageResource(R.drawable.pcloudy);

                imageView.setImageResource(imgid);

                Random randquote = new Random();
                int ind = randquote.nextInt(quotes.size()-1);
                String qot= quotes.get(ind);
                Log.d("QUOTE", qot);
                qb.setText(qot);

                JSONArray objfc=obj6.getJSONArray("forecast");
                for (int i=0; i<objfc.length(); i++) {
                    Log.d("FORECAST", objfc.getJSONObject(i).getString("day"));
                    Log.d("HIGH",objfc.getJSONObject(i).getString("high"));
                }

                fc1day.setText(objfc.getJSONObject(0).getString("day"));
                fc1hi.setText(objfc.getJSONObject(0).getString("high"));
                fc1lo.setText(objfc.getJSONObject(0).getString("low"));
                fc2day.setText(objfc.getJSONObject(1).getString("day"));
                fc2hi.setText(objfc.getJSONObject(1).getString("high"));
                fc2lo.setText(objfc.getJSONObject(1).getString("low"));
                fc3day.setText(objfc.getJSONObject(2).getString("day"));
                fc3hi.setText(objfc.getJSONObject(2).getString("high"));
                fc3lo.setText(objfc.getJSONObject(2).getString("low"));
                fc4day.setText(objfc.getJSONObject(3).getString("day"));
                fc4hi.setText(objfc.getJSONObject(3).getString("high"));
                fc4lo.setText(objfc.getJSONObject(3).getString("low"));
                fc5day.setText(objfc.getJSONObject(4).getString("day"));
                fc5hi.setText(objfc.getJSONObject(4).getString("high"));
                fc5lo.setText(objfc.getJSONObject(4).getString("low"));

            }catch (JSONException e){}




        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

    }
}


