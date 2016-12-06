package com.devahoy.sample.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devahoy.sample.login.model.User;
import com.devahoy.sample.login.utils.UserManager;


public class LoginActivity extends ActionBarActivity {

    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mRegister;
    private Context mContext;

    private UserManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mManager = new UserManager(this);

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

        User user = new User(username, password);

        User validateUser = mManager.checkUserLogin(user);

        if (null == validateUser) {
            String message = getString(R.string.login_error_message);
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra(User.Column.USERNAME, validateUser.getUsername());
            intent.putExtra(User.Column.ID, validateUser.getId());
            startActivity(intent);
            finish();
        }
    }
}
