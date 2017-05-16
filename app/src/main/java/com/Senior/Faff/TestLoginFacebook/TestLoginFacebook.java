package com.Senior.Faff.TestLoginFacebook;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.DeviceLoginButton;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

public class TestLoginFacebook extends FragmentActivity {

    private CallbackManager callbackManager;
    private TextView tv;
    private ImageView iv;
    private TestFacebookFragment frag;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login_facebook);
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

                        Profile profile = currentProfile;
                        String userID = accessToken.getUserId();
                        String profileImgUrl = "https://graph.facebook.com/" + userID + "/picture?type=large";

                        try {
                            if(profile!=null){
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void logout() {
        if(accessTokenTracker!=null)
        {
            if(accessTokenTracker.isTracking())
            {
                accessTokenTracker.stopTracking();
            }
        }
        if(profileTracker!=null)
        {
            if(profileTracker.isTracking())
            {
                profileTracker.stopTracking();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
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

}
