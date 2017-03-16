package com.Senior.Faff.Fragment.Party;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.Senior.Faff.utils.CreatePartyManager;

import com.Senior.Faff.R;

public class Party_CreateNewParty extends AppCompatActivity {

    private EditText name,description;
    private TextView people,require;
    private Spinner spinner1;
    private Button add_require,create;
    private CreatePartyManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party__create_new_party);
        name = (EditText)findViewById(R.id.name);
        description = (EditText)findViewById(R.id.description);
        people = (TextView)findViewById(R.id.people);
        require = (TextView)findViewById(R.id.require);
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        add_require = (Button)findViewById(R.id.addrequire);
        create = (Button)findViewById(R.id.create);


        add_require.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(Party_CreateNewParty.this,Party_CreateNewParty_map.class);
                intent.putExtra("userid","pee");
                intent.putExtra("Roomid","1sdad");
                intent.putExtra("Name",name.getText().toString().trim());
                intent.putExtra("Description",description.getText().toString().trim());
                intent.putExtra("People",20);
                intent.putExtra("rule",new int[]{1,2});
                startActivity(intent);
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
