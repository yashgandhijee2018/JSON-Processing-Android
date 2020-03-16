package com.example.hp.jsonprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String result;
        DownloadTask downloadTask=new DownloadTask();
        try
        {
            result=downloadTask.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22").get();
            Log.i("JSON main:",result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class DownloadTask extends AsyncTask<String,Void,String>
    {
        URL url;
        String result="";
        @Override
        protected String doInBackground(String... urls) {
            try
            {
                url=new URL(urls[0]);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                InputStream in=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);

                int data=reader.read();
                while(data!=-1)
                {
                    char c= (char) data;
                    result+=c;
                    data=reader.read();
                }
                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        /*
            The result of the above override method is JSON hence it is processed
            in the onPostExecute now...
            Processing of json comes into picture...
        */
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            try
            {
                //create JSON object or converting the read content to a JSON format.
                JSONObject jsonObjectMaster=new JSONObject(s);

                //Reading value stored in a particular key and store it as a string
                String weatherInfo=jsonObjectMaster.getString("weather");
                Log.i("Weather cotent: ",weatherInfo);

                /*
                    Now that string can be array of sub JSON objects..
                    So we assign each element as an array element
                 */
                JSONArray array=new JSONArray(weatherInfo);

                //Parsing that JSON array
                for(int i=0;i<array.length();i++)
                {
                    JSONObject jsonObjectWorker=array.getJSONObject(i);
                    Log.i("main",jsonObjectWorker.getString("main"));
                    Log.i("description",jsonObjectWorker.getString("description"));
                    Log.i("id",jsonObjectWorker.getString("id"));
                    Log.i("main",jsonObjectWorker.getString("main"));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
