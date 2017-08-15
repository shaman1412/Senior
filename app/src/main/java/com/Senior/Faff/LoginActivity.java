package com.Senior.Faff;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.TestLoginFacebook.TestFacebookFragment;
import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.DatabaseManager;
import com.Senior.Faff.utils.Helper;
import com.Senior.Faff.utils.LoadingFragment;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class LoginActivity extends ActionBarActivity {

    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mRegister;
    private Context mContext;
    private LoginButton loginButton;

    private static FrameLayout loading;
    private LoadingFragment loadingFragment;

    private DatabaseManager mManager;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private TestFacebookFragment frag;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private JSONObject obj;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        final String user_id = sp.getString(UserProfile.Column.UserID, "nothing");
        if (!(user_id.equals("nothing"))) {
            Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
            intent.putExtra(UserProfile.Column.UserID, user_id);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);

        mContext = this;

        mLogin = (Button) findViewById(R.id.button_login);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mRegister = (TextView) findViewById(R.id.register);
        loading = (FrameLayout) findViewById(R.id.loading);
        hideLoading();

        LoginManager.getInstance().logOut();
        loginButton = (LoginButton) findViewById(R.id.facebookLoginButton);
        callbackManager = CallbackManager.Factory.create();

        ArrayList<String> permiss = new ArrayList<String>();
        permiss.add("email");
        permiss.add("user_birthday");
        permiss.add("user_location");
        permiss.add("user_hometown");

        loginButton.setReadPermissions(permiss);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                debug("Success");

                try {
                    loadingFragment = new LoadingFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.loading, loadingFragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        // Set the access token using
                        // currentAccessToken when it's loaded or set.
                    }
                };
                // If the access token is available already assign it.
                accessToken = AccessToken.getCurrentAccessToken();

                if (accessToken != null) {
                    String userID = accessToken.getUserId();
                    Log.i("TEST:", "user_id : "+userID);
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "email,birthday,link,picture,location,gender,name,id");  //no favorite type and phone
                    new GraphRequest(
                            AccessToken.getCurrentAccessToken(),
                            userID,
                            parameters,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                                    obj = response.getJSONObject();
                                    try {
                                        Log.i("TEST:", "response is : "+obj.toString());
                                        if(obj.has("location"))
                                        {
                                            String location_id = obj.getJSONObject("location").getString("id");
                                            new GraphRequest(AccessToken.getCurrentAccessToken(),
                                                    location_id + "?fields=location{latitude,longitude}",
                                                    null,
                                                    HttpMethod.GET,
                                                    new GraphRequest.Callback() {
                                                        @Override
                                                        public void onCompleted(GraphResponse response) {
                                                            JSONObject obj1 = response.getJSONObject();
                                                            try {
                                                                obj1 = obj1.getJSONObject("location");
                                                                obj.put("location", obj1.getString("latitude") + "," + obj1.getString("longitude"));
                                                                obj1 = obj.getJSONObject("picture").getJSONObject("data");
                                                                obj.put("picture", obj1.getString("url"));
                                                                Log.i("TEST:", obj.toString());

                                                                int rand = (int)(Math.random()*999);
                                                                String key_user = "fb"+obj.getString("id")+String.valueOf(rand);
                                                                rand = (int)(Math.random()*999);
                                                                String key_pass = "fb"+obj.getString("id")+String.valueOf(rand);
                                                                String key_user_id = "fb"+obj.getString("id");
                                                                UserAuthen user = new UserAuthen(key_user, key_pass, key_user_id);
                                                                int gender, age;
                                                                if(obj1.has("url")){
                                                                    obj.put("picture", obj1.getString("url"));
                                                                }
                                                                else
                                                                {
                                                                    obj.put("picture", "");
                                                                }
                                                                if(obj.has("gender")){
                                                                    gender = obj.getString("gender").equals("male")?1:0;
                                                                }
                                                                else
                                                                {
                                                                    gender = 1;
                                                                }
                                                                if(obj.has("birthday")){
                                                                    age = Integer.parseInt(getAge(obj.getString("birthday")));
                                                                }
                                                                else
                                                                {
                                                                    age = 0;
                                                                }
                                                                Log.i("TEST:", obj.toString());
                                                                UserProfile profile = new UserProfile(
                                                                        key_user_id,
                                                                        obj.getString("name"),
                                                                        obj.getString("location"),
                                                                        obj.getString("email"),
                                                                        "",
                                                                        "",
                                                                        gender,
                                                                        age,
                                                                        obj.getString("picture")
                                                                );
                                                                facebookToUserAuthen fb_tmp = new facebookToUserAuthen(profile);
                                                                showLoading();
                                                                fb_tmp.execute(user);

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }).executeAsync();
                                        }
                                        else
                                        {
                                            obj.put("location", "");
                                            try {
                                                JSONObject obj1 = obj.getJSONObject("picture").getJSONObject("data");
                                                if(obj1.has("url")){
                                                    obj.put("picture", obj1.getString("url"));
                                                }
                                                else
                                                {
                                                    obj.put("picture", "");
                                                }
                                                Log.i("TEST:", obj.toString());

                                                int rand = (int)(Math.random()*999);
                                                String key_user = "fb"+obj.getString("id")+String.valueOf(rand);
                                                rand = (int)(Math.random()*999);
                                                String key_pass = "fb"+obj.getString("id")+String.valueOf(rand);
                                                String key_user_id = "fb"+obj.getString("id");
                                                UserAuthen user = new UserAuthen(key_user, key_pass, key_user_id);
                                                int gender, age;
                                                if(obj.has("gender")){
                                                    gender = obj.getString("gender").equals("male")?1:0;
                                                }
                                                else
                                                {
                                                    gender = 1;
                                                }
                                                if(obj.has("birthday")){
                                                    age = Integer.parseInt(getAge(obj.getString("birthday")));
                                                }
                                                else
                                                {
                                                    age = 0;
                                                }
                                                UserProfile profile = new UserProfile(
                                                        key_user_id,
                                                        obj.getString("name"),
                                                        obj.getString("location"),
                                                        obj.getString("email"),
                                                        "",
                                                        "",
                                                        gender,
                                                        age,
                                                        obj.getString("picture")
                                                );
                                                facebookToUserAuthen fb_tmp = new facebookToUserAuthen(profile);
                                                showLoading();
                                                fb_tmp.execute(user);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                    ).executeAsync();
                }

                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        // App code
                        debug("Change User");
                    }
                };
            }

            @Override
            public void onCancel() {
                debug("Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                debug("Error");
                error.printStackTrace();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(accessTokenTracker!=null)
            {
                accessTokenTracker.stopTracking();
            }
            if(profileTracker!=null)
            {
                profileTracker.stopTracking();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onBackPressed() {
//
//    }

    public void showLogin() {
        this.loginButton.setVisibility(View.VISIBLE);
    }

    public void hideLogin() {
        this.loginButton.setVisibility(View.GONE);
    }

    public void debug(String str) {
        Log.d("TEST:", " debug : " + str);
    }

    private void checkLogin() {
        String username = mUsername.getText().toString().trim().toLowerCase();
        String password = mPassword.getText().toString().trim();
        mLogin.setEnabled(false);
        new getData().execute(username, password);
    }

    private String getAge(String str)
    {
        try {
            String[] arr = str.split("\\/");
            int MM = Integer.parseInt(arr[0]);
            int DD = Integer.parseInt(arr[1]);
            int YYYY = Integer.parseInt(arr[2]);

            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            dob.set(YYYY, MM, DD);

            if(today.get(Calendar.YEAR) > dob.get(Calendar.YEAR))
            {
                int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                    age--;
                }

                return String.valueOf(age);
            }
            else
            {
                return "-1";
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "-1";
    }

    private class getData extends AsyncTask<String, String, UserAuthen> {

        String pass;
        int responseCode;
        HttpURLConnection connection;
        String resultjson;

        @Override
        protected UserAuthen doInBackground(String... args) {
            StringBuilder result = new StringBuilder();
            String url_api = "https://faff-1489402013619.appspot.com/login/" + args[0];
            pass = args[1];
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
                if (pass.equals(userAuthen.getPassword())) {
                    Intent intent = new Intent(mContext, Main2Activity.class);
                    //intent.putExtra(UserAuthen.Column.USERNAME, userAuthen.getUsername());
                    intent.putExtra(UserAuthen.Column.USERID, userAuthen.getUserid());
                    startActivity(intent);
                    finish();
                } else {
                    String message = getString(R.string.login_error_message);
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    mLogin.setEnabled(true);
                }
            } else {
                String message = getString(R.string.login_error_message);
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                mLogin.setEnabled(true);
            }

        }


    }

    private class facebookToUserAuthen extends AsyncTask<UserAuthen, String, UserAuthen> {

        int responseCode;
        HttpURLConnection connection;
        UserProfile user_profile;

        public facebookToUserAuthen(UserProfile profile) {
            user_profile = profile;
        }

        @Override
        protected UserAuthen doInBackground(UserAuthen... params) {
            try {
                JSONObject postDataParams = new JSONObject();
                postDataParams.put(UserAuthen.Column.USERNAME, params[0].getUsername());
                postDataParams.put(UserAuthen.Column.PASSWORD, params[0].getPassword());
                postDataParams.put(UserAuthen.Column.USERID, params[0].getUserid());


                URL url = new URL("https://faff-1489402013619.appspot.com/login/registerIfNotExists/"+params[0].getUserid());
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
                facebookToUserProfile fb_to_profile = new facebookToUserProfile();
                if(user_profile!=null)
                {
                    fb_to_profile.execute(user_profile);
                }
                else
                {
                    Log.i("TEST:", " LoginActivity : user_profile is null");
                }
//                Intent intent = new Intent(mContext, Main2Activity.class);
//                intent.putExtra(UserAuthen.Column.USERID, s.getUserid());
//                startActivity(intent);
//                finish();
            }else{
                Toast.makeText(mContext, "Username was used", Toast.LENGTH_SHORT).show();
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

    private class facebookToUserProfile extends AsyncTask<UserProfile, String, String> {

        private ArrayList<String> imgPath = new ArrayList<>();
        String result = "";

        @Override
        protected String doInBackground(UserProfile... params) {
            try {
                Map<String, String> paras = new HashMap<>();
                paras.put(UserProfile.Column.UserID, params[0].getUserid());
                paras.put(UserProfile.Column.Name, params[0].getName());
                paras.put(UserProfile.Column.Address, params[0].getAddress());
                paras.put(UserProfile.Column.Email, params[0].getEmail());
                paras.put(UserProfile.Column.Gender, String.valueOf(params[0].getGender()));
                paras.put(UserProfile.Column.Favourite_type, params[0].getFavourite_type());
                paras.put(UserProfile.Column.Age, String.valueOf(params[0].getAge()));
                paras.put(UserProfile.Column.Telephone, params[0].getTelephone());

                String img_path_tmp = params[0].getPicture();
                debug(" img path tmp : " + img_path_tmp);
                LoginActivity.facebookToUserProfile.this.imgPath.add(img_path_tmp);
                URL url = new URL("https://faff-1489402013619.appspot.com/user/newUserIfNotExists/"+params[0].getUserid());

                Helper hp = new Helper();
                hp.setLocal(false);
                hp.setRequest_method("POST");
                result = hp.multipartRequest(url.toString(),paras, LoginActivity.facebookToUserProfile.this.imgPath, "image", "image/jpeg");

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != "") {
                Intent intent = new Intent(mContext, Main2Activity.class);
                UserProfile user_profile = null;
                try {
                    user_profile = new Gson().fromJson(result, UserProfile.class);
                    Log.i("TEST:", "result is : "+result);
                    intent.putExtra(UserProfile.Column.UserID, user_profile.getUserid());
                    if(loadingFragment !=null)
                    {
                        loadingFragment.onStop();
                    }
                    startActivity(intent);
                    finish();
                } catch (JsonSyntaxException e) {
                    intent.putExtra(UserProfile.Column.UserID, result);
                    if(loadingFragment !=null)
                    {
                        loadingFragment.onStop();
                    }
                    startActivity(intent);
                    finish();
                }
                //Toast.makeText(mcontext, result, Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(mcontext, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void showLoading(){
        if(loading!=null)
        {
            if(loading.getVisibility()==View.GONE)
            {
                loading.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void hideLoading(){
        if(loading!=null)
        {
            if(loading.getVisibility()==View.VISIBLE)
            {
                loading.setVisibility(View.GONE);
            }
        }
    }

}
