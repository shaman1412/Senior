package com.Senior.Faff.RestaurantProfile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.R;
import com.Senior.Faff.model.Restaurant;

public class AddRes_respro extends AppCompatActivity {


    private String get_user_id;
    private String user_id;
    private int type_food;
    private EditText name,detail,picture;
    private Button location;
    private Context mcontext;
    private Toolbar toolbar;
    private String getlocation;
    private TextView showlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_res_respro);

        String value[] = new String[]{"Food on order", "Steak", "Pizza", "Noodle"};
        Spinner spinner = (Spinner) findViewById(R.id.type_select);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, value);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        mcontext = this;
        //user_id = Integer.parseInt(get_user_id);
        showlocation  = (TextView)findViewById(R.id.showlocation);
        if(getIntent().getStringExtra("Position") != null) {
            getlocation = getIntent().getStringExtra("Position");
            if (getlocation != null) {
                showlocation.setText(getlocation);
            }
        }
        if(getIntent().getStringExtra("userid") != null) {
            user_id = getIntent().getStringExtra("userid");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = (EditText) findViewById(R.id.addName);
        detail = (EditText) findViewById(R.id.addDetail);

        location = (Button) findViewById(R.id.addLocation);
        picture = (EditText) findViewById(R.id.addPicture);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RestaurantMapsActivity.class);
                intent.putExtra("userid",user_id);
                startActivity(intent);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {

                switch (pos) {
                    case 0:
                        type_food = 0;
                        Toast.makeText(AddRes_respro.this,"0",Toast.LENGTH_SHORT);
                        break;
                    case 1:
                        type_food = 1;
                        Toast.makeText(AddRes_respro.this,"1",Toast.LENGTH_SHORT);
                        break;
                    case 2:
                        type_food = 2;
                        Toast.makeText(AddRes_respro.this,"2",Toast.LENGTH_SHORT);
                        break;
                    case 3:
                        type_food = 3;
                        break;

                }

               /* Toast.makeText(parent.getContext(),
                        "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        Button addre = (Button) findViewById(R.id.add);
        addre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurant restaurant = new Restaurant(name.getText().toString(), picture.getText().toString(), getlocation, detail.getText().toString(), 0, user_id, type_food);
                Restaurant_manager manager = new Restaurant_manager(mcontext);
                long result = manager.AddRestaurant(restaurant);
                if (result == -1) {
                    Toast.makeText(mcontext, "Fail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mcontext, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mcontext, Main2Activity.class);
                    intent.putExtra("userid",user_id);
                    startActivity(intent);

                }
            }
        });
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                    // if this doesn't work as desired, another possibility is to call `finish()` here.
                    finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

}
