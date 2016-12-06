package com.devahoy.sample.login;

import android.app.AlertDialog;
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

import com.devahoy.sample.login.model.User;
import com.devahoy.sample.login.utils.UserManager;

public class MainActivity extends ActionBarActivity {

    Button mChangePassword;
    TextView mUsername;
    private UserManager mManager;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = new UserManager(this);
        mUser = new User();

        mChangePassword = (Button) findViewById(R.id.change_password);
        mUsername = (TextView) findViewById(R.id.say_hi);

        Bundle args = getIntent().getExtras();

        if (null == args) {
            Toast.makeText(this, getString(R.string.welcome_error_message),
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            mUsername.setText(getString(R.string.say_hi) + " " +
                    args.getString(User.Column.USERNAME));

            mUser.setId(args.getInt(User.Column.ID));
            mUser.setUsername(args.getString(User.Column.USERNAME));
        }

        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPassword();
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
                    mUser.setPassword(password);
                    mManager.changePassword(mUser);
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
