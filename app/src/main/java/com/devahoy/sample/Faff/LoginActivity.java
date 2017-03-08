package com.devahoy.sample.Faff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devahoy.sample.Faff.model.UserAuthen;
import com.devahoy.sample.Faff.utils.DatabaseManager;


public class LoginActivity extends ActionBarActivity {

    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mRegister;
    private Context mContext;

    private DatabaseManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mManager = new DatabaseManager(this);

        mContext = this;

        mLogin = (Button) findViewById(R.id.button_login);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mRegister = (TextView) findViewById(R.id.register);

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

    private void checkLogin() {
        String username = mUsername.getText().toString().trim().toLowerCase();
        String password = mPassword.getText().toString().trim();

        UserAuthen userAuthen = new UserAuthen(username, password);

        UserAuthen validateUserAuthen = mManager.checkUserLogin(userAuthen);

        if (null == validateUserAuthen) {
            String message = getString(R.string.login_error_message);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(mContext, Main2Activity.class);
            intent.putExtra(UserAuthen.Column.USERNAME, validateUserAuthen.getUsername());
            intent.putExtra(UserAuthen.Column.ID, validateUserAuthen.getId());
            startActivity(intent);
            //finish();
        }
    }
}
