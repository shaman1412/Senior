package com.Senior.Faff.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Senior.Faff.LoginActivity;
import com.Senior.Faff.R;
import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.model.UserProfile;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class ChangePassword extends AppCompatActivity {


    private EditText oldpass, newpass, confirmpass;
    private String old, pass, compass;
    private Button submit;
    private String userid;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldpass = (EditText) findViewById(R.id.old_password);
        newpass = (EditText) findViewById(R.id.password);
        confirmpass = (EditText) findViewById(R.id.confirm_password);
        submit = (Button) findViewById(R.id.button_submit);
        Bundle args = getIntent().getExtras();
        userid = args.getString(UserProfile.Column.UserID);
        mContext = this;

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                old = oldpass.getText().toString();
                pass = newpass.getText().toString();
                compass = confirmpass.getText().toString();

                if (old != null && pass != null && compass != null) {
                    if (pass.equals(compass)) {
                        new check_old().execute(userid);

                    } else {
                        Toast.makeText(ChangePassword.this, "new password and confirm password are not match", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ChangePassword.this, "Please  complete all field", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class check_old extends AsyncTask<String, String, UserAuthen> {
        int responseCode;
        HttpURLConnection connection;

        @Override
        protected UserAuthen doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/login/userid/" + args[0];
            try {
                URL url = new URL(url_api);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                responseCode = connection.getResponseCode();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (responseCode == 200) {
                Log.i("Request Status", "This is success response status from server: " + responseCode);
                Gson gson = new Gson();
                UserAuthen userAuthen = gson.fromJson(result.toString(), UserAuthen.class);
                return userAuthen;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null;

            }
        }

        @Override
        protected void onPostExecute(UserAuthen userAuthen) {
            super.onPostExecute(userAuthen);
            if (userAuthen != null) {
                if (old.equals(userAuthen.getPassword())) {
                    new set_password().execute(userid,pass);
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    String message = getString(R.string.login_error_message);
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    //mLogin.setEnabled(true);
                }
            } else {
                String message = getString(R.string.login_error_message);
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                //mLogin.setEnabled(true);
            }

        }

    }
    private class set_password extends AsyncTask<String , String , Void>{

        @Override
        protected Void doInBackground(String... args) {
            HttpURLConnection connection;
            int responseCode = 0;
            String url_api = "https://faff-1489402013619.appspot.com/login/change_pass/" + args[0];

            try {
                JSONObject postDataParams = new JSONObject();
                postDataParams.put(UserAuthen.Column.PASSWORD ,args[1]);

                URL url = new URL(url_api);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);

                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                bufferedWriter.write(getPostDataString(postDataParams));
                bufferedWriter.flush();
                bufferedWriter.close();
                out.close();
                responseCode = connection.getResponseCode();


            }catch (Exception e){
                e.printStackTrace();
            }
            if ( responseCode == 200) {
                Log.i("Request Status", "New Password is updated. Please re-login" + responseCode);
                return  null;
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null;

            }
        }
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
