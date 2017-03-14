package com.Senior.sample.Faff.UserProfile;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.Senior.sample.Faff.R;

public class InsertUserProfile extends AppCompatActivity {
    private ViewPager pager;
    private int genderid;
    private int Userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user_profile);
        String[] values = {"Femail","Male"};
        EditText name = (EditText)findViewById(R.id.name);
        EditText email = (EditText)findViewById(R.id.email);
        EditText Tel = (EditText)findViewById(R.id.telephone);
        EditText Age = (EditText)findViewById(R.id.age);
        EditText location = (EditText)findViewById(R.id.address);
        Spinner gender = (Spinner)findViewById(R.id.spinner1);
        Button submit = (Button)findViewById(R.id.submit);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        gender.setAdapter(adapter);
        Bundle arg  = getIntent().getExtras();
        name.getText().toString();
        email.getText().toString();
        Tel.getText().toString();
        Age.getText().toString();
        location.getText().toString();
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {

                switch(pos){
                    case 0:
                        genderid = 0;
                    case 1:
                        genderid = 1;

                }

               /* Toast.makeText(parent.getContext(),
                        "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

       //Userid  = arg.getInt(UserAuthen.Column.ID);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}