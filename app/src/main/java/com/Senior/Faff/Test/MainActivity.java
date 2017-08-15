package com.Senior.Faff.Test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.LoginActivity;
import com.Senior.Faff.MapsActivity;
import com.Senior.Faff.Promotion.PromotionActivity;
import com.Senior.Faff.R;
import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.utils.DatabaseManager;

public class MainActivity extends ActionBarActivity {

    Button mChangePassword,Go_to_map,Go_to_promotion,Go_to_provider;
    Button mAddPromotion;
    Button mLogout;
    Button mShowPromotion;
    TextView mUsername;
    private DatabaseManager mManager;
    UserAuthen mUserAuthen;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = new DatabaseManager(this);
        mContext = this;
        mUserAuthen = new UserAuthen();

        mChangePassword = (Button) findViewById(R.id.change_password);
        mUsername = (TextView) findViewById(R.id.say_hi);
        mAddPromotion = (Button) findViewById(R.id.add_promotion);
        mLogout = (Button) findViewById(R.id.logout);
        mShowPromotion = (Button) findViewById(R.id.show_promotion);
        Go_to_provider = (Button)findViewById(R.id.lopro);
        Go_to_map = (Button)findViewById(R.id.goTomap);

        Bundle args = getIntent().getExtras();

        if (null == args) {
            Toast.makeText(this, getString(R.string.welcome_error_message),
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            mUsername.setText(getString(R.string.say_hi) + " " +
                    args.getString(UserAuthen.Column.USERNAME));

            //mUserAuthen.setId(args.getInt(UserAuthen.Column.ID));
            mUserAuthen.setUsername(args.getString(UserAuthen.Column.USERNAME));
        }

        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPassword();
            }
        });

        mAddPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PromotionActivity.class);
                startActivity(intent);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });

//        mShowPromotion.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, PromotionView.class);
//                startActivity(intent);
//            }
//        });
        Go_to_map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mContext,MapsActivity.class);
                startActivity(intent);
            }
        });
        Go_to_provider.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,TestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDialogPassword() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        final EditText newPassword = (EditText) view.findViewById(R.id.password);
        builder.setView(view);

        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = newPassword.getText().toString();
                if(!TextUtils.isEmpty(password)) {
                    mUserAuthen.setPassword(password);
                    mManager.changePassword(mUserAuthen);
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.change_password_message),
                            Toast.LENGTH_SHORT).show();
                    goToLogin();
                }
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), null);
        builder.show();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
