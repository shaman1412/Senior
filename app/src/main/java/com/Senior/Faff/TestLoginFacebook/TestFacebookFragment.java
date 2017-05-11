package com.Senior.Faff.TestLoginFacebook;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Senior.Faff.R;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.DeviceLoginButton;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFacebookFragment extends Fragment {

    private String img_path = "";
    private String name = "";
    private TextView tv;
    private ImageView iv;
    private LoginButton logout;
    private LinearLayout user_profie;

    public TestFacebookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_facebook, container, false);

        ((TestLoginFacebook)getActivity()).hideLogin();

        tv = (TextView) root.findViewById(R.id.textView_testFacebok);
        iv = (ImageView) root.findViewById(R.id.imageView_testFacebok);
        logout = (LoginButton) root.findViewById(R.id.facebookLogouButton);
        user_profie = (LinearLayout) root.findViewById(R.id.linear_user_facebook);

        try {
            tv.setText(name);
            Picasso.with(getActivity()).load(img_path).into(this.iv);
        } catch (Exception e) {
            Log.i("TEST: ", "Load picture facebook error : "+e.toString());
        }

        user_profie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TEST:", "logout click:");
                try {
                    ((TestLoginFacebook)getActivity()).showLogin();
                    ((TestLoginFacebook)getActivity()).getSupportFragmentManager().popBackStackImmediate();
//                    ((TestLoginFacebook)getActivity()).logout();
                } catch (Exception e) {
                    Log.i("TEST:", " logout error : "+e.toString());
                }
                Log.i("TEST:", "logout click1:");
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    public void setTv(String txt) {
        this.name = txt;
    }

    public void setIv(String img_path) {
        this.img_path = img_path;
    }
}
