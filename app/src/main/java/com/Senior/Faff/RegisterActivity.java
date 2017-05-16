package com.Senior.Faff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.UserProfile.InsertUserProfile;
import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.utils.DatabaseManager;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class RegisterActivity extends ActionBarActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mRegister;
    private int check;
    private Context mContext;
    private DatabaseManager mManager;
    private TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       // mManager = new DatabaseManager(this);
        mContext = this;

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mRegister = (Button) findViewById(R.id.button_register);
        login = (TextView)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(goLogin);
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mUsername.getText().toString().trim().toLowerCase();
                String password = mPassword.getText().toString();
                String confirmPassword = mConfirmPassword.getText().toString();

                if (password.equals(confirmPassword)) {
                  /*  UserAuthen userAuthen = new UserAuthen(username, password);
                    long rowId = mManager.registerUser(userAuthen);*/
                        //userAuthen = mManager.getUserID(username);
                    String userid = username+"1412";
                    UserAuthen user = new UserAuthen(username,password, userid);
                    new addData().execute(user);


                } else {
                    String message = getString(R.string.register_password_error);
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class addData extends AsyncTask<UserAuthen, String, UserAuthen> {

        int responseCode;
        HttpURLConnection connection;


        @Override
        protected UserAuthen doInBackground(UserAuthen... params) {


            try {

                JSONObject postDataParams = new JSONObject();
                postDataParams.put(UserAuthen.Column.USERNAME, params[0].getUsername());
                postDataParams.put(UserAuthen.Column.PASSWORD, params[0].getPassword());
                postDataParams.put(UserAuthen.Column.USERID, params[0].getUserid());


                URL url = new URL("https://faff-1489402013619.appspot.com/login/register");
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
                e.printStackTrace();
            }
            if (responseCode == 200) {
                Log.i("Request Status", "This is success response status from server: " + responseCode);

                return params[0];
            } else {
                Log.i("Request Status", "This is failure response status from server: " + responseCode);
                return null;

            }


        }

        @Override
        protected void onPostExecute(UserAuthen s) {
            super.onPostExecute(s);
            if(s != null){
                 Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                check = 1;
                Intent intent = new Intent(RegisterActivity.this, InsertUserProfile.class);
                intent.putExtra(UserAuthen.Column.USERID, s.getUserid());
                startActivity(intent);
                //((Activity)mContext).finish();
            }else{
                Toast.makeText(mContext, "Username was used", Toast.LENGTH_SHORT).show();
                check = 0;
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


}
