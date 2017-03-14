package com.Senior.Faff;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Senior.Faff.model.UserAuthen;
import com.Senior.Faff.utils.DatabaseManager;

public class RegisterActivity extends ActionBarActivity {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mRegister;

    private Context mContext;
    private DatabaseManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mManager = new DatabaseManager(this);
        mContext = this;

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mRegister = (Button) findViewById(R.id.button_register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mUsername.getText().toString().trim().toLowerCase();
                String password = mPassword.getText().toString();
                String confirmPassword = mConfirmPassword.getText().toString();

                if (password.equals(confirmPassword)) {
                    UserAuthen userAuthen = new UserAuthen(username, password);
                    long rowId = mManager.registerUser(userAuthen);
                        //userAuthen = mManager.getUserID(username);
                    if (rowId == -1) {
                        String message = getString(R.string.register_error_message);
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = getString(R.string.register_success);
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();/*

                        Intent intent = new Intent(mContext, InsertUserProfile.class);
                        intent.putExtra(UserAuthen.Column.ID,userAuthen.getId());
                        startActivity(intent);*/
                        finish();
                    }

                } else {
                    String message = getString(R.string.register_password_error);
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
