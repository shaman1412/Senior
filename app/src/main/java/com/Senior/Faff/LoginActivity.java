package com.Senior.Faff;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.TestLoginFacebook.TestFacebookFragment;
import com.Senior.Faff.UserProfile.InsertUserProfile;
import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.DatabaseManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends ActionBarActivity {

    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mRegister;
    private Context mContext;
    private LoginButton loginButton;

    private DatabaseManager mManager;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private TestFacebookFragment frag;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("CHECK_LOGIN", Context.MODE_PRIVATE);
        String user_id = sp.getString(UserProfile.Column.UserID, "nothing");
        if (!(user_id.equals("nothing"))) {
            Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
            intent.putExtra(UserProfile.Column.UserID, user_id);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);

        mManager = new DatabaseManager(this);

        mContext = this;

        mLogin = (Button) findViewById(R.id.button_login);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mRegister = (TextView) findViewById(R.id.register);
        loginButton = (LoginButton) findViewById(R.id.facebookLoginButton);
        callbackManager = CallbackManager.Factory.create();


        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {

            Profile profile = Profile.getCurrentProfile();
            String userID = accessToken.getUserId();
            String profileImgUrl = "https://graph.facebook.com/" + userID + "/picture?type=large";

            try {
                frag = new TestFacebookFragment();
                frag.setTv(profile.getName());
                frag.setIv(profileImgUrl);
                getSupportFragmentManager().beginTransaction().replace(R.id.test_facebook_container, frag).addToBackStack(null).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                debug("Success");

                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        // Set the access token using
                        // currentAccessToken when it's loaded or set.
                    }
                };
                // If the access token is available already assign it.
                accessToken = AccessToken.getCurrentAccessToken();

                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        // App code

                        debug("Change User");

                        Profile profile = currentProfile;
                        String userID = accessToken.getUserId();
                        String profileImgUrl = "https://graph.facebook.com/" + userID + "/picture?type=large";

                        try {
                            if (profile != null) {
                                frag = new TestFacebookFragment();
                                frag.setTv(profile.getName());
                                frag.setIv(profileImgUrl);
                                getSupportFragmentManager().beginTransaction().replace(R.id.test_facebook_container, frag).addToBackStack(null).commit();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
            accessTokenTracker.stopTracking();
            profileTracker.stopTracking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

    }

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
}
