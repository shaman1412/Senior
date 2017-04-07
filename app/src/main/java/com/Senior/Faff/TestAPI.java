package com.Senior.Faff;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestAPI extends AppCompatActivity {
    private TextView pee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api2);
        pee = (TextView) findViewById(R.id.pee);
        new getData().execute();
       // new addData().execute();
    }


    private class getData extends AsyncTask<String, String, String> {


        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {
            int code;
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("https://faff-1489402013619.appspot.com/user");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                code = urlConnection.getResponseCode();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
             /*   InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);
*/

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
       /*     finally {
                urlConnection.disconnect();
            }*/


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            pee.setText(result);

        }

    }

    private class addData extends AsyncTask<String, String, String> {

        int responseCode;
        HttpURLConnection connection;

        @Override
        protected String doInBackground(String... params) {


            try {


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userid", "ฉันชื่อ");
                postDataParams.put("name", "abc@gmail.com");
                postDataParams.put("address", "abc@gmail.com");
                postDataParams.put("email", "abc@gmail.com");
                postDataParams.put("telephone", "0123213123");
                postDataParams.put("dof", "abc@gmail.com");
                postDataParams.put("gender", "abc@gmail.com");
                postDataParams.put("age", 23); //int


                URL url = new URL("https://faff-1489402013619.appspot.com/user/new_user");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStream out = new BufferedOutputStream(connection.getOutputStream());

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                bufferedWriter.write(getPostDataString(postDataParams));
                bufferedWriter.flush();
                bufferedWriter.close();
                out.close();

                responseCode = connection.getResponseCode();

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
            if (responseCode == 200) {
                Log.i("Request Status", "This is success response status from server: " + responseCode);

            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);

            }

            return null;
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }
}
