package com.Senior.Faff.Fragment.Party;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Senior.Faff.Main2Activity;
import com.Senior.Faff.model.Party;
import com.Senior.Faff.model.Restaurant;
import com.Senior.Faff.model.UserProfile;
import com.Senior.Faff.utils.CreatePartyManager;

import com.Senior.Faff.R;

import java.util.ArrayList;

public class Party_CreateNewParty extends AppCompatActivity {

    private EditText name,description,people,address,appointment,telephone;
    private Spinner spinner1;
    private Button add_rule,create,rule;
    private CreatePartyManager manager;
    private String roomid,userid;
    private Party party;
    private ArrayList<String> rule_gender;
    private String rule_age;
    private String createby;
    private String getrule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party__create_new_party);
        name = (EditText)findViewById(R.id.name);
        description = (EditText)findViewById(R.id.description);
        people = (EditText)findViewById(R.id.people);
        address = (EditText)findViewById(R.id.address);
        appointment = (EditText)findViewById(R.id.period);
        telephone = (EditText)findViewById(R.id.telephone);
        add_rule = (Button)findViewById(R.id.rule);
        create = (Button)findViewById(R.id.next);
        rule_gender = new ArrayList<>();
        Bundle args = getIntent().getExtras();
        if(args != null){
          userid  = args.getString(UserProfile.Column.UserID);
            createby = args.getString(UserProfile.Column.Name);

        }
        roomid = userid+ "ppap";

        add_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(Party_CreateNewParty.this,Party_CreateNewParty_map.class);
                if(rule_gender != null) {
                    for (int i = 0; i < rule_gender.size(); i++) {
                        if (i == 0) {
                            getrule = rule_gender.get(i);
                        } else
                            getrule = getrule + rule_gender.get(i);
                        if (i != rule_gender.size() - 1) {
                            getrule = getrule + ",";
                        }
                    }
                }
                intent.putExtra(Party.Column.RoomID, roomid );
                intent.putExtra(Party.Column.Create_by_name, name.getText().toString());
                intent.putExtra(Party.Column.Description,description.getText().toString());
                intent.putExtra(Party.Column.People, Integer.parseInt(people.getText().toString()));
                intent.putExtra(Party.Column.Address,address.getText().toString());
                intent.putExtra(Party.Column.Appointment,appointment.getText().toString());
                intent.putExtra(Party.Column.Telephone,telephone.getText().toString());
                intent.putExtra(Party.Column.Rule_gender,getrule);
                intent.putExtra(Party.Column.Rule_age,rule_age);
                intent.putExtra(UserProfile.Column.UserID,userid);
                intent.putExtra(UserProfile.Column.Name,createby);
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
    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Party_CreateNewParty.this);
        View promptView = layoutInflater.inflate(R.layout.rule_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Party_CreateNewParty.this);
        alertDialogBuilder.setView(promptView);
        final  AlertDialog alert = alertDialogBuilder.create();
        // final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
      /*  alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Main2Activity.this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });*/

        Button btn_1 = (Button)promptView.findViewById(R.id.btn1);
        Button btn_2 = (Button) promptView.findViewById(R.id.btn2);
     final  CheckBox chk1 = (CheckBox)promptView.findViewById(R.id.chk1);
     final  CheckBox chk2 = (CheckBox)promptView.findViewById(R.id.chk2);
      final  EditText age = (EditText)promptView.findViewById(R.id.age);



        btn_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.cancel();

            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chk1.isChecked()){
                    rule_gender.add("Female");
                }
                if(chk2.isChecked()){
                    rule_gender.add("Male");
                }
                if(!(chk1.isChecked() || chk2.isChecked()))
                {
                    rule_gender.add("Male");
                    rule_gender.add("Female");
                }
                if(age.getText().toString() != null) {
                    rule_age = age.getText().toString();
                }
                alert.cancel();
            }
        });
        // create an alert dialog

        alert.show();

    }
    public void kk(CheckBox chk1, CheckBox chk2){


    }
}
